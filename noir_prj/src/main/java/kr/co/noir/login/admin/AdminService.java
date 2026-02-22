package kr.co.noir.login.admin;

import org.apache.ibatis.exceptions.PersistenceException;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import kr.co.noir.dao.MyBatisHandler;

@Service
public class AdminService {

    @Autowired(required = false)
    private AdminMapper adminMapper;

    // PasswordEncoder는 한 번 생성해서 재사용하는 것이 효율적입니다.
    private final BCryptPasswordEncoder bce = new BCryptPasswordEncoder();

    /**
     * 관리자 로그인 로직
     */
    public AdminDomain adminLogin(AdminDTO aDTO) {
        AdminDomain ad = null;

        try {
            // 1. DB에서 아이디로 관리자 정보 조회
            ad = adminMapper.adminLogin(aDTO.getAdminId());

            if (ad == null) {
                // 2. 아이디가 존재하지 않는 경우
                ad = new AdminDomain();
                ad.setResultMsg("아이디가 존재하지 않습니다.");
                aDTO.setResult("F");
            } else {
                // 3. 아이디가 존재하는 경우 비밀번호 검증
                String rawPw = aDTO.getAdminPass();   // 사용자가 입력한 비번
                String dbPw = ad.getAdminPass();      // DB에 저장된 비번
                boolean isMatch = false;

                // 암호화된 비밀번호(BCrypt 60자)인지 확인
                if (dbPw != null && dbPw.length() == 60) {
                    isMatch = bce.matches(rawPw, dbPw);
                } else {
                    // 평문 비교 (개발 단계나 이전 데이터 대응)
                    isMatch = rawPw.equals(dbPw);
                }

                if (isMatch) {
                    ad.setResultMsg("로그인 성공");
                    aDTO.setResult("S");
                } else {
                    ad.setResultMsg("비밀번호가 일치하지 않습니다.");
                    aDTO.setResult("F");
                }
            }
        } catch (PersistenceException pe) {
            // DB 에러 발생 시
            pe.printStackTrace();
            if (ad == null) ad = new AdminDomain();
            ad.setResultMsg("데이터베이스 오류가 발생했습니다.");
            aDTO.setResult("E");
        }

        return ad;
    }

	
	public AdminDomain selectAdmin(AdminDTO aDTO) throws PersistenceException {
		AdminDomain ad=null;
		
		SqlSession ss=MyBatisHandler.getInstance().getMyBatisHandler(false);
		
		ad=ss.selectOne("kr.co.noir.login.AdminMapper.selectAdmin", aDTO);
		if( ss!= null) {ss.close(); } //end if
		
		return ad;
		
	}//selecOneMember
	
	
}//AdminService



