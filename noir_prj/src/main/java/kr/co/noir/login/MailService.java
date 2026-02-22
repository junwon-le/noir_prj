package kr.co.noir.login;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	private final JavaMailSender javaMailSender;
    private static final String SENDER_EMAIL = "sjdbreader5@gmail.com";

    // @Qualifier("mailSender")를 사용하여 application-dev.properties의 
    // spring.mail 설정을 따르는 기본 빈(Gmail)을 주입받음.
    @Autowired
    public MailService(@Qualifier("mailSender") JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }    
    
    
    public String createNumber() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            key.append(random.nextInt(10));
        }
        return key.toString();
    }
    
    public String sendMail(String mail) {
        String number = createNumber();
        
        // 1. 현재 주입된 Sender의 설정을 강제로 점검
        if (javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl impl = (JavaMailSenderImpl) javaMailSender;
            
            // 만약 여기서 host가 smtp.gmail.com이 아니라면 설정 파일이 무시된 것
            System.out.println("현재 연결된 SMTP Host: " + impl.getHost()); 
            System.out.println("현재 연결된 SMTP Port: " + impl.getPort());

            // 코드에서 강제로 디버그와 프로퍼티를 다시 세팅
            impl.getJavaMailProperties().put("mail.debug", "true");
            impl.getJavaMailProperties().put("mail.smtp.auth", "true");
            impl.getJavaMailProperties().put("mail.smtp.ssl.enable", "true");
        }
        
        MimeMessage message = javaMailSender.createMimeMessage();
          
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(SENDER_EMAIL);
            helper.setTo(mail);
            helper.setSubject("[NOIR] 비밀번호 찾기 인증 번호입니다.");

            // HTML 양식 적용 (제공해주신 디자인)
            String content = "<!DOCTYPE html>"
                + "<html lang='ko'>"
                + "<body style='margin: 0; padding: 0; background-color: #0f1218; font-family: sans-serif;'>"
                + "    <table width='100%' border='0' cellspacing='0' cellpadding='0' style='background-color: #0f1218;'>"
                + "        <tr>"
                + "            <td align='center' style='padding: 40px 10px;'>"
                + "                <table width='100%' border='0' cellspacing='0' cellpadding='0' style='max-width: 500px; background-color: #2c2926; border: 1px solid #7c6a52; border-radius: 8px;'>"
                + "                    <tr>"
                + "                        <td align='center' style='padding: 40px 0 20px 0;'>"
                + "                            <h1 style='margin: 0; color: #ffffff; font-size: 28px; letter-spacing: 2px; text-transform: uppercase;'>"
                + "                                <span style='display:block; font-size:40px;'>NOIR</span>"
                + "                                <span style='font-size:12px; color:#888; letter-spacing: 5px;'>BOUTIQUE HOTEL</span>"
                + "                            </h1>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td align='center' style='padding: 0 40px;'>"
                + "                            <p style='margin: 0 0 10px 0; color: #C4A16F; font-size: 18px; font-weight: bold;'>PASSWORD RESET</p>"
                + "                            <p style='margin: 0 0 30px 0; color: #dddddd; font-size: 14px; line-height: 1.6;'>"
                + "                                안녕하세요, <strong>NOIR Hotel</strong> 입니다.<br>"
                + "                                요청하신 비밀번호 찾기 인증번호를 안내해 드립니다.<br>"
                + "                                아래 번호를 입력창에 입력해 주세요."
                + "                            </p>"
                + "                        </td>"
                + "                    </tr>"
                + "                    <tr>"
                + "                        <td align='center' style='padding: 0 40px 40px 40px;'>"
                + "                            <div style='background-color: rgba(0,0,0,0.3); border: 1px dashed #7c6a52; padding: 20px; border-radius: 4px;'>"
                + "                                <span style='font-size: 32px; font-weight: bold; color: #C4A16F; letter-spacing: 8px; font-family: monospace;'>"
                +                                   number // 생성된 인증번호 삽입
                + "                                </span>"
                + "                            </div>"
                + "                            <p style='margin-top: 20px; color: #888888; font-size: 12px;'>"
                + "                                * 이 코드는 5분간 유효합니다.<br>"
                + "                                * 본인이 요청하지 않았다면 이 메일을 무시해 주세요."
                + "                            </p>"
                + "                        </td>"
                + "                    </tr>"
                + "                </table>"
                + "                <table width='100%' border='0' cellspacing='0' cellpadding='0' style='max-width: 500px; margin-top: 20px;'>"
                + "                    <tr>"
                + "                        <td align='center' style='color: #555555; font-size: 11px; line-height: 1.4;'>"
                + "                            © 2026 NOIR BOUTIQUE HOTEL. All rights reserved.<br>"
                + "                            서울시 중구 SIST, ZIZonHotel"
                + "                        </td>"
                + "                    </tr>"
                + "                </table>"
                + "            </td>"
                + "        </tr>"
                + "    </table>"
                + "</body>"
                + "</html>";

            helper.setText(content, true); // HTML 전송 설정
            // 실제 전송 시도
            javaMailSender.send(message);
            System.out.println("메일 전송 성공: " + mail);

        } catch (Exception e) { // MessagingException 대신 광범위한 Exception 사용
        	System.err.println("메일 발송 중 오류 발생: " + e.getMessage());
        	e.printStackTrace(); 
        	return null; // 실패 시 확실하게 null을 반환하여 컨트롤러가 FAIL을 알게 함
        }
        return number;
    }
    
}