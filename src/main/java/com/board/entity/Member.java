package com.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.util.Date;

@Entity
public class Member {
	private String name;
	private String nickname;

	@Id
	private String id;
	private String email;
	private Date birthday;
	private String encryptedPassword;
	
	public Member() {
		this.name = null;
		this.nickname = null;
		this.id = null;
		this.email = null;
		this.birthday = null;
	}
	
	public Member(String name, String nickname, String id, String email, Date birthday, String encryptedPassword) {
		this.name = name;
		this.nickname = nickname;
		this.id = id;
		this.email = email;
		this.birthday = birthday;
		this.encryptedPassword = encryptedPassword;
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
	
	public Date getBirthday() {
		return birthday;
	}
	
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	public void updatePassword(String newEncryptedPassword) {
		this.encryptedPassword = newEncryptedPassword;
	}

	public void updateEmail(String newEmail) {
		this.email = newEmail;
	}

	public void updateNickname(String newNickname) {
		this.nickname = newNickname;
	}
}
