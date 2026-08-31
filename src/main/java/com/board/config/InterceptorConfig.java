package com.board.config;

import com.board.interceptor.CacheControlInterceptor;
import com.board.interceptor.LoginCheckInterceptor;
import com.board.interceptor.RefreshCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.handler.MappedInterceptor;

import com.board.repository.MemberRepository;

@Configuration
public class InterceptorConfig {
	
	private MemberRepository memberRepository;
	
	public InterceptorConfig (MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}
	
	@Bean
	public MappedInterceptor loginCheckInterceptor() {
		String[] includePatterns = {"/**"};
		String[] excludePatterns = {"/members","/members/login", "/members/regist", "/css/**", "/js/**", "/fonts/**", "/images/**", "/refresh/**", "/empty/**"};
	    return new MappedInterceptor(includePatterns, excludePatterns, new LoginCheckInterceptor());
	}
	
	@Bean
	public MappedInterceptor refreshCheckInterceptor() {
		String[] includePatterns = {"/refresh"};
	    return new MappedInterceptor(includePatterns, new RefreshCheckInterceptor(memberRepository));
	}
	
	@Bean
    public MappedInterceptor webContentInterceptor() {
        String[] includePatterns = {"/board/edit/**", "/board/new/**", "/members/mypage/**"};
        return new MappedInterceptor(includePatterns, new CacheControlInterceptor());
    }
}
