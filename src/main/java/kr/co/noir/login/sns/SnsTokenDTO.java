package kr.co.noir.login.sns;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SnsTokenDTO {
    private int memberNum;
//    private String memberId;
    private String provider;
    private String accessToken;
    private String refreshToken;
    private LocalDateTime tokenExpiresAt; // Oracle DATE와 매핑
    private LocalDateTime updateDate;
}