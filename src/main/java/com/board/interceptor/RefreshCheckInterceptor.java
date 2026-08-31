package com.board.interceptor;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.board.entity.Member;
import com.board.repository.MemberRepository;
public class RefreshCheckInterceptor implements HandlerInterceptor {
	
	private MemberRepository memberRepository;
	
	public RefreshCheckInterceptor(MemberRepository memberRepository) {
		this.memberRepository = memberRepository;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		HttpSession session = null;
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(cookie.getName().equals("REFRESH_TOKEN")) {
					//멤버 서비스
					String refreshToken = cookie.getValue();
					Member member = memberRepository.findMemberByRefreshToken(refreshToken);
					if (member == null) {
						cookie.setMaxAge(0);
						response.addCookie(cookie);
						break;
					}
					
					session = request.getSession();
					session.setAttribute("member", member);
				}
			}
		}
		
		if(session == null) {
			session = request.getSession();
			session.setAttribute("member", new Member());
		}		
		
		return false;
	}
}
