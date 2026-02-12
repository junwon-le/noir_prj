package kr.co.noir.login.sns;

import java.io.Serializable;

import kr.co.noir.login.MemberDTO;
import lombok.Getter;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;

    public SessionUser(MemberDTO member) {
        this.name = member.getMemberName(); // 성+이름 결합 메서드 활용
        this.email = member.getMemberEmail();
    }
}