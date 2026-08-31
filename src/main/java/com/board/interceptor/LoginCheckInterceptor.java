package com.board.interceptor;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.board.entity.Member;

public class LoginCheckInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession(false);
        if(session != null) {
        	Member member = (Member)session.getAttribute("member");
        	if(member != null) {        		
        		return true;
        	}
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("/empty"); // /빼면 상대주소인지 확인하기
      	dispatcher.forward(request, response);
        return false;
	}
}
