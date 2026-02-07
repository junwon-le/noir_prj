package kr.co.noir.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;
    
    @Autowired
    private RestTemplate restTemplate;    
    
    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;
    
    // 네이버 연동 해제용 정보 (properties에서 가져옴)
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;
    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;
  
    
    public Map<String, Object> getMemberPage(int page, String searchType, String keyword) {
        int pageSize = 10;
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("searchType", searchType);
        params.put("keyword", keyword); // 기본적으로 평문 키워드 저장

        // 전화번호 검색일 경우 암호화된 경우라면 키워드도 별도로 담기
        if ("tel".equals(searchType) && keyword != null && !keyword.isEmpty()) {
            TextEncryptor te = Encryptors.text(key, salt);
            String encryptedKeyword = te.encrypt(keyword);
            params.put("encryptedKeyword", encryptedKeyword); // 암호화된 버전 추가
        }

        List<MemberDTO> list = memberMapper.selectMemberList(params);
        int totalCount = memberMapper.selectTotalCount(params);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // [복호화 및 이름 합치기 시작]
        if (list != null) {
            TextEncryptor te = Encryptors.text(key, salt);

            for (MemberDTO member : list) {
                // 1. 이름 합치기 (성 + 이름)
                // LastName이나 FirstName이 null일 경우를 대비해 방어 코드를 넣는 것이 좋습니다.
                String lastName = (member.getMemberLastName() != null) ? member.getMemberLastName() : "";
                String firstName = (member.getMemberFirstName() != null) ? member.getMemberFirstName() : "";
                
                // 합쳐진 이름을 MemberName에 
                member.setMemberName(lastName + firstName);
                
                // 2. 이메일 복호화 처리
                member.setMemberEmail(processDecrypt(te, member.getMemberEmail()));
                
                // 3. 전화번호 복호화 및 포맷팅 처리
                member.setMemberTel(formatTel(processDecrypt(te, member.getMemberTel())));
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("memberList", list);
        result.put("totalPage", totalPage);
        return result;
    }//getMemberPage
    
    
    //여러명 Y로 바꾸기
    public boolean removeMembers(List<String> memberIds) {
        return memberMapper.updateMembersWithdraw(memberIds) > 0;
    }//removeMembers

    // 1명 Delflag Y 로 바꾸기
    public boolean withdrawMember(String memberId) {
        return memberMapper.updateMemberWithdraw(memberId) == 1 ;
    }//removeMembers
        
    
    
    /**
     * 길이를 체크하여 복호화 여부를 결정하는 유틸리티 메서드
     */
    private String processDecrypt(TextEncryptor te, String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }

        // TextEncryptor(AES) 결과는 보통 최소 32자 이상의 16진수 문자열
        // 평문 이메일이나 전화번호는 암호화된 16진수(0-9, a-f) 패턴과 다르므로 이를 활용
        boolean isEncrypted = cipherText.length() >= 32 && cipherText.matches("^[0-9a-fA-F]+$");

        if (isEncrypted) {
            try {
                return te.decrypt(cipherText);
            } catch (Exception e) {
                // 복호화 실패 시(패턴은 맞지만 암호문이 아닌 경우) 원본 반환
                return cipherText;
            }
        }
        
        return cipherText; // 암호화된 형식이 아니면 평문으로 간주하고 그대로 반환
        
    }//processDecrypt
    
    
    /**
     * 전화 번호 출력시 하이픈이 없는 숫자를 자동으로 3-4-4 구조로 바꿔줌
     * @param tel
     * @return
     */
    private String formatTel(String tel) {
        if (tel == null || tel.length() != 11 || tel.contains("-")) {
            return tel;
        }
        // 01012345678 -> 010-1234-5678
        return tel.replaceFirst("(^02|[0-9]{3})([0-9]{3,4})([0-9]{4})$", "$1-$2-$3");
    }    
    

    /**
     * 최종 회원 탈퇴 로직
     */
    @Transactional
    public boolean processWithdrawal(String memberId, String provider) {
        // 1. DB에서 암호화된 토큰 정보 조회
        SnsTokenDTO snsToken = memberMapper.selectSnsToken(memberId, provider.toUpperCase());
        
        if (snsToken == null) return false;

        // 2. 토큰 복호화
        TextEncryptor te = Encryptors.text(key, salt);
        String decryptedToken = te.decrypt(snsToken.getAccessToken());

        boolean apiResult = false;

        // 3. SNS 제공자별 연동 해제 API 호출
        try {
            switch (provider.toUpperCase()) {
                case "KAKAO":
                    apiResult = unlinkKakao(decryptedToken);
                    break;
                case "NAVER":
                    apiResult = unlinkNaver(decryptedToken);
                    break;
                case "GOOGLE":
                    apiResult = unlinkGoogle(decryptedToken);
                    break;
            }
        } catch (Exception e) {
            // 토큰 만료 등으로 API 호출 실패 시에도 서비스 탈퇴는 진행할지 정책 결정 필요
            System.err.println("SNS 연동 해제 API 호출 중 오류 발생: " + e.getMessage());
        }

        // 4. API 호출 성공 시(또는 실패와 무관하게 탈퇴 처리 시) DB 정리
        if (apiResult) {
            // 회원 상태 변경 (DEL_FLAG = 'Y')
            memberMapper.updateMemberWithdraw(memberId);
            // 연동 토큰 정보 삭제
            memberMapper.deleteSnsToken(memberId, provider.toUpperCase());
            return true;
        }

        return false;
    }

    // --- 각 SNS별 연동 해제 구현 (이전 코드 활용) ---
    private boolean unlinkKakao(String token) {
        String url = "https://kapi.kakao.com/v1/user/unlink";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return restTemplate.postForEntity(url, new HttpEntity<>(headers), String.class)
                           .getStatusCode() == HttpStatus.OK;
    }

    private boolean unlinkNaver(String token) {
        String url = "https://nid.naver.com/oauth2.0/token?grant_type=delete&client_id=" 
                     + naverClientId + "&client_secret=" + naverClientSecret + "&access_token=" + token;
        return restTemplate.getForEntity(url, String.class).getBody().contains("success");
    }

    private boolean unlinkGoogle(String token) {
        String url = "https://oauth2.googleapis.com/revoke?token=" + token;
        return restTemplate.postForEntity(url, null, String.class).getStatusCode() == HttpStatus.OK;
    }
    
    
}//class