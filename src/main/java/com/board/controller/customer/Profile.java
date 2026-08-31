package com.board.controller.customer;

public class Profile {
	private String name;
	private String nickname;
	private String id;
	private String email;
	
	public Profile(String name, String nickname, String id, String email) {
		this.name = name;
		this.nickname = nickname;
		this.id = id;
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public String getNickname() {
		return nickname;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}
}
