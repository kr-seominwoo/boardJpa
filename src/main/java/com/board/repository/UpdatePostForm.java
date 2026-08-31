package com.board.repository;

public class UpdatePostForm {
	
	private Long postId;
	private String memberId;
	private String title;
	private String content;
	
	public UpdatePostForm(Long postId, String memberId, String title, String content) {
		this.postId = postId;
		this.memberId = memberId;
		this.title = title;
		this.content = content;
	}

	public Long getPostId() {
		return postId;
	}
	
	public String getMemberId() {
		return memberId;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}
}
