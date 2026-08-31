package com.board.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Entity(name="comment")
@DynamicInsert
public class Comment {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long parentId;
	private Long postId;
	private String memberId;
	private String writer;
	private String content;
	private Date regdate;

	private int likes;
	private int unlike;
	private int blind;

	public Comment() {
	}

	public Comment(Long parentId, Long postId, String memberId, String writer, String content
				   ) {
		this.parentId = parentId;
		this.postId = postId;
		this.memberId = memberId;
		this.writer = writer;
		this.content = content;
	}

	public Long getId() {
		return id;
	}

	public Long getParentId() {
		return parentId;
	}

	public Long getPostId() {
		return postId;
	}

	public String getMemberId() {
		return memberId;
	}

	public String getWriter() {
		return writer;
	}

	public String getContent() {
		return content;
	}

	public Date getRegdate() {
		return regdate;
	}

	public int getLikes() {
		return likes;
	}

	public int getUnlike() {
		return unlike;
	}

	public int getBlind() {
		return blind;
	}
}
