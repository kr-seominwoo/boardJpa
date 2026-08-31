package com.board.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import com.board.entity.Member;

public class LoginCheck {
	public static Member getMemberFromSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			return null;
		}
		
		Member member = (Member) session.getAttribute("member");
		return member;
	}
	
	public static boolean isLoggedIn(Member member) {
		if (member == null || member.getId() == null) {
			return false;
		}
		
		return true;
	}
}
