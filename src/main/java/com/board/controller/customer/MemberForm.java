package com.board.controller.customer;

import java.util.Date;

public class MemberForm {
	private String name;
	private String nickname;
	private String id;
	private String password;
	private String confirmationPassword;
	private String email;
	private Date birthday;
    
	public MemberForm(String name, String nickname, String id, String password, String confirmationPassword,
			String email, Date birthday) {
		this.name = name;
		this.nickname = nickname;
		this.id = id;
		this.password = password;
		this.confirmationPassword = confirmationPassword;
		this.email = email;
		this.birthday = birthday;
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

	public String getPassword() {
		return password;
	}

	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public String getEmail() {
		return email;
	}

	public Date getBirthday() {
		return birthday;
	}
}
