package com.board.repository;

import java.util.Date;

public class PostForm {
	
	private String memberId;
	private String writer;
	private String title;
	private String content;
	private String category;
	private String subCategory;


	public PostForm(String memberId, String writer, String title, String content, String category, String subCategory) {
		this.memberId = memberId;
		this.writer = writer;
		this.title = title;
		this.content = content;
		this.category = category;
		this.subCategory = subCategory;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getWriter() {
		return writer;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public String getCategory() {
		return category;
	}

	public String getSubCategory() {
		return subCategory;
	}
}
