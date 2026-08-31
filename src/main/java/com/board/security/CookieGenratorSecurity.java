package com.board.security;

import java.security.SecureRandom;

import jakarta.servlet.http.Cookie;

public class CookieGenratorSecurity {
	final static int LENGTH = 8;
	
	public static Cookie makeCookie(String cookieName, String id) {
		SecureRandom random = new SecureRandom();
		
		byte[] bytes = new byte[LENGTH];
		random.nextBytes(bytes);
		StringBuilder builder = new StringBuilder(id);
		builder.append(bytes);
		
		Cookie cookie = new Cookie(cookieName, builder.toString());
		cookie.setPath("/");
		return cookie;
	}
	
	public static void setAge(Cookie cookie, int maxAgeSec) {
		cookie.setMaxAge(maxAgeSec);
	}
}