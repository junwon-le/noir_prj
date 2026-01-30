package kr.co.noir.dao;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class ImaportConfig {

	@Bean
    public IamportClient iamportClient() {
        // 본인의 관리자 페이지에서 확인한 API 키와 시크릿을 넣으세요.
        return new IamportClient("1446525105288383", "tu5aUdfMTW0ZyHzfSrJG0ICLR2gtcNnNmL2QvM6wg9h1fHBMA5EamrozKTWMEdlyDvNuvyNsHi3a4MVb");
    }
	
}
