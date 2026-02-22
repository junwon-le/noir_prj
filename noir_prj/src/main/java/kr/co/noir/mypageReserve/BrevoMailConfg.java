package kr.co.noir.mypageReserve;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;


@Configuration
public class BrevoMailConfg {

    @Value("${brevo.mail.host}")
    private String host;
    @Value("${brevo.mail.port}")
    private int port;
    @Value("${brevo.mail.username}")
    private String username;
    @Value("${brevo.mail.password}")
    private String password;

    @Bean(name = "brevoMailSender") // 빈 이름을 명시적으로 지정
    public JavaMailSender brevoMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", "true");
        // 중요: Brevo(587포트)는 STARTTLS를 사용하며, SSL 강제 옵션이 꺼져야 합니다.
        props.put("mail.smtp.starttls.enable", "true"); 
        props.put("mail.smtp.ssl.enable", "false"); 
        
        return mailSender;
    }
}
