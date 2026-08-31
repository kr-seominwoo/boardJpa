package com.board.controller.customer;

public class PasswordForm {
	private String id;
	private String currentPassword;
	private String newPassword;
	private String confirmationPassword;
	
	public PasswordForm(String id, String currentPassword, String newPassword, String confirmationPassword) {
		this.id = id;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
		this.confirmationPassword = confirmationPassword;
	}

	public String getId() {
		return id;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public String getConfirmationPassword() {
		return confirmationPassword;
	}
}
