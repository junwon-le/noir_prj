package kr.co.noir.login;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.security.crypto.encrypt.AesBytesEncryptor;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import kr.co.noir.login.sns.SnsTokenDTO;

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

        // [전화번호 검색 로직 수정: AES -> SHA-256]
        if ("tel".equals(searchType) && keyword != null && !keyword.isEmpty()) {
        	// 1. SHA-256 해시 생성 (2926dd58... 방식)
            String hashedKeyword = encryptSHA256(keyword);

            // [중요] 매퍼 XML의 #{encryptedKeyword}와 #{keyword}에 각각 전달
            params.put("keyword", hashedKeyword); 
            params.put("encryptedKeyword", hashedKeyword);
            
            System.out.println("검색어(평문): " + keyword);
            System.out.println("검색어(해시): " + hashedKeyword);

        } else {
            params.put("keyword", keyword);
        }
        
        List<MemberDTO> list = memberMapper.selectMemberList(params);
        int totalCount = memberMapper.selectTotalCount(params);
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);

        // [복호화 및 이름 합치기 로직은 그대로 유지]
        if (list != null) {
            TextEncryptor te = Encryptors.text(key, salt);
            for (MemberDTO member : list) {
                String lastName = (member.getMemberLastName() != null) ? member.getMemberLastName() : "";
                String firstName = (member.getMemberFirstName() != null) ? member.getMemberFirstName() : "";
                member.setMemberName(lastName + firstName);
                
                member.setMemberEmail(processDecrypt(te, member.getMemberEmail()));
                
                // 주의: DB에 해시(SHA-256)만 들어있다면 복호화(원본보기)는 실패합니다.
                // 리스트에 번호가 안 나온다면 DB에 복호화용 별도 컬럼이 있는지 확인해야 합니다.
                try {
                    // 복호화 시도
                    String decryptedTel = processDecrypt(te, member.getMemberTel());
                    member.setMemberTel(formatTel(decryptedTel));
                } catch (Exception e) {
                    // 복호화 실패 시 (해시값인 경우) 그냥 원본(해시)을 보여주거나 마스킹 처리
                    // member.setMemberTel("복호화 불가 데이터"); 
                    System.out.println("전화번호 복호화 실패: 해시 데이터일 가능성 높음");
                }
                
                
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
    
 // SHA-256 암호화 유틸리티 메서드
    private String encryptSHA256(String str) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(str.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }    

 // AES 고정 IV 암호화 (검색용)
    private String encryptAESDeterministic(String plainText) {
        try {
            BytesEncryptor be = new AesBytesEncryptor(key, salt, KeyGenerators.shared(16));
            byte[] encrypted = be.encrypt(plainText.getBytes(StandardCharsets.UTF_8));
            return new String(Hex.encode(encrypted));
        } catch (Exception e) {
            return plainText;
        }
    }    
    
    
}//class