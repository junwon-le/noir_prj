package kr.co.noir.login;

import java.util.List;

import lombok.Data;

@Data
public class MemberDTO {
	private int memberNum;
	private String memberId;    // MEMBER_ID
	private String memberPass;  // MEMBER_PASS
	private String memberLastName;  // MEMBER_NAME
	private String memberFirstName;  // MEMBER_NAME
	private String memberEmail; // MEMBER_EMAIL
    private String memberTel;
    private String memberBirth;
    private String memberDelFlag;     // 'N or 'Y' 
    private String memberInputDate;
    private String memberIp;
    
    // 조회를 위한 가상 필드
    private String memberName; // 성 + 이름 결합용
    
    // 탈퇴 처리를 위해 체크된 번호들을 담는 리스트
    private List<Integer> selectedMembers;
}