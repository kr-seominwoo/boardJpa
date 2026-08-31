package com.board.controller.comment;

public class CommentForm {
	private Long postId;
	private Long parentId;
	private String category;	
	private String content;
	
	public CommentForm(Long postId, Long parentId, String category, String content) {
		this.postId = postId;
		this.parentId = parentId;
		this.category = category;
		this.content = content;
	}

	public Long getPostId() {
		return postId;
	}

	public Long getParentId() {
		return parentId;
	}

	public String getCategory() {
		return category;
	}

	public String getContent() {
		return content;
	}
}
