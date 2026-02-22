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
public class NonMemberService {

    @Autowired 
    private NonMemberMapper nonMemberMapper;
    
    @Value("${user.crypto.key}") 
    private String key;
    @Value("${user.crypto.salt}") 
    private String salt;

    public Map<String, Object> getNonMemberPage(int page, String searchType, String keyword) {
        int pageSize = 10;
        Map<String, Object> params = new HashMap<>();
        params.put("start", (page - 1) * pageSize + 1);
        params.put("end", page * pageSize);
        params.put("searchType", searchType);
        params.put("keyword", keyword);

        // 검색어가 전화번호일 경우 암호화된 키워드 추가
        if ("tel".equals(searchType) && keyword != null && !keyword.isEmpty()) {
            TextEncryptor te = Encryptors.text(key, salt);
            params.put("encryptedKeyword", te.encrypt(keyword));
        }

        List<NonMemberDTO> list = nonMemberMapper.selectNonMemberList(params);
        int totalCount = nonMemberMapper.selectTotalCount(params);

        if (list != null) {
            TextEncryptor te = Encryptors.text(key, salt);
            for (NonMemberDTO dto : list) {
                // 이메일 및 전화번호 복호화 처리
                dto.setNonUserEmail(processDecrypt(te, dto.getNonUserEmail()));
                dto.setNonUserTel(formatTel(processDecrypt(te, dto.getNonUserTel())));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("nonMemberList", list);
        // 페이지 수 계산 (데이터가 없을 때 0이 아닌 1페이지로 처리)
        int totalPage = (totalCount == 0) ? 0 : (int) Math.ceil((double) totalCount / pageSize);
        result.put("totalPage", totalPage);
        return result;
    }

    public boolean removeNonMembers(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return false;
        return nonMemberMapper.deleteNonMembers(ids) > 0;
    }

    /**
     * 길이를 체크하여 복호화 여부를 결정하는 메서드
     */
    private String processDecrypt(TextEncryptor te, String cipherText) {
        if (cipherText == null || cipherText.isEmpty()) {
            return cipherText;
        }

        // 암호화된 데이터(16진수 32자 이상) 패턴 확인
        boolean isEncrypted = cipherText.length() >= 32 && cipherText.matches("^[0-9a-fA-F]+$");

        if (isEncrypted) {
            try {
                return te.decrypt(cipherText);
            } catch (Exception e) {
                return cipherText; // 복호화 실패 시 원본 반환
            }
        }
        return cipherText;
    }

    /**
     * 11자리 숫자를 010-0000-0000 포맷으로 변환
     */
    private String formatTel(String tel) {
        if (tel == null || tel.length() != 11 || tel.contains("-")) {
            return tel;
        }
        return tel.replaceAll("(\\d{3})(\\d{4})(\\d{4})", "$1-$2-$3");
    }
}