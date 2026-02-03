package kr.co.noir.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Service;

@Service
public class MemberService {
    @Autowired
    private MemberMapper memberMapper;
    
    @Value("${user.crypto.key}")
    private String key;
    @Value("${user.crypto.salt}")
    private String salt;

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
                
                // 합쳐진 이름을 FirstName에 덮어씌우거나, DTO에 memberFullName 필드가 있다면 거기 세팅하세요.
                member.setMemberFirstName(lastName + firstName);
                
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
    
    

    public boolean removeMembers(List<Integer> memberNums) {
        return memberMapper.updateMembersDelete(memberNums) > 0;
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
    
}//class