package com.board.controller.customer;

public class EmailForm {
	private String id;
	private String email;
	private String password;
	
	public EmailForm(String id, String email, String password) {
		this.id = id;
		this.email = email;
		this.password = password;
	}

	public String getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}
}
