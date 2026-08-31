package com.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class AppConfig {
	@Bean
	public PasswordEncoder pbkdf2PasswordEncoder() {
		Map<String , PasswordEncoder> encoders = new HashMap<>();

		// encoders 내 키값("sha256"), 등록할 인코더(Pbkdf2PasswordEncoder),
		// 다이제스트에 포함할 secret("sampleSecretKey"), 생성할 바이트 단위의 해시 값 길이(32byte(256bit)),
		// 해싱 반복할 횟수(100000번), 해싱 알고리즘(PBKDF2WithHmacSHA256)
		encoders.put("sha256" , new Pbkdf2PasswordEncoder("sampleSecretKey", 32, 100000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256));

		PasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder("sha256", encoders);

		return delegatingPasswordEncoder;
	}
}
