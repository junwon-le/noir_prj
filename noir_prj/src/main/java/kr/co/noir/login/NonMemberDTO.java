package kr.co.noir.login;

import lombok.Data;

@Data
public class NonMemberDTO {
    private int nonUserNum;             // NON_USER_NUM (PK)
    private String nonUserResLastName;  // NON_USER_RES_LAST_NAME
    private String nonUserResFirstName; // NON_USER_RES_FIRST_NAME
    private String nonUserEmail;        // NON_USER_EMAIL
    private String nonUserTel;          // NON_USER_TEL
    private String nonUserPass;         // NON_USER_PASS
    private String nonUserIp;           // NON_USER_IP
    private String nonUserInputDate;    // NON_USER_INPUTDATE (String으로 받으면 타임리프 처리가 쉽습니다)
    private int num;                    // NUM (예약 번호 등으로 추정)
    
    // 성+이름 결합 필드
    private String nonMemberName; 
}