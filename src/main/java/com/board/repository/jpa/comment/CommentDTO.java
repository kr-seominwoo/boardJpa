package com.board.repository.jpa.comment;

import com.board.entity.Comment;

import java.util.Date;

public class CommentDTO {
	private Long id;

	private Long parentId;
	private String memberId;
	private String writer;
	private String content;
	private Date regdate;
	private int likes;
	private int dislike;
	private int level;
	private int blind;

	public CommentDTO(Long id, Long parentId, String memberId, String writer, String content, Date regdate, int likes, int dislike, int level, int blind) {
		this.id = id;
		this.parentId = parentId;
		this.memberId = memberId;
		this.writer = writer;
		this.content = content;
		this.regdate = regdate;
		this.likes = likes;
		this.dislike = dislike;
		this.level = level;
		this.blind = blind;
	}

	public CommentDTO(Comment from, int level) {
		this.id = from.getId();
		this.parentId = from.getParentId();
		this.writer = from.getWriter();
		this.content = from.getContent();
		this.regdate = from.getRegdate();
		this.likes = from.getLikes();
		this.dislike = from.getDislike();
		this.blind = from.getBlind();
		this.level = level;
	}

	public Long getId() {
		return id;
	}

	public Long getParentId() {
		return parentId;
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

	public int getDislike() {
		return dislike;
	}

	public int getLevel() {
		return level;
	}

	public int getBlind() {
		return blind;
	}
}
