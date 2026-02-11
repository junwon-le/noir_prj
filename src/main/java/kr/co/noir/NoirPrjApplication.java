package kr.co.noir;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class NoirPrjApplication {

	public static void main(String[] args) {
	    // 1. .env 로드 로직 (기존 코드 유지)
	    io.github.cdimascio.dotenv.Dotenv dotenv = io.github.cdimascio.dotenv.Dotenv.configure()
	            .filename("noir.env")
	            .load();
	    dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

	    // 2. JMX를 코드로 직접 비활성화하여 충돌 방지
	    SpringApplication app = new SpringApplication(NoirPrjApplication.class);
	    app.setAddCommandLineProperties(false); // 선택 사항
	    System.setProperty("spring.jmx.enabled", "false"); 
	    System.setProperty("spring.application.admin.enabled", "false");

	    app.run(args);
	}

}
