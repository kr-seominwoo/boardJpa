package com.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class PersistenceLogin {

	public PersistenceLogin() {
	}

	@Id
	private String refreshToken;

	@OneToOne
	@JoinColumn(name = "id")
	private Member member;

	public PersistenceLogin(String refreshToken, Member member) {
		this.refreshToken = refreshToken;
		this.member = member;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public Member getMember() {
		return member;
	}
}
