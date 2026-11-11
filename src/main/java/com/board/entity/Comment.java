package com.board.entity;

import com.board.repository.jpa.comment.CommentDTO;
import jakarta.persistence.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@SqlResultSetMapping(
		name = "CommentDtoMapping",
		classes = @ConstructorResult(
				targetClass = CommentDTO.class,
				columns = {
						@ColumnResult(name="id", type = Long.class),
						@ColumnResult(name="parent_id", type = Long.class),
						@ColumnResult(name="member_id", type = String.class),
						@ColumnResult(name="writer", type = String.class),
						@ColumnResult(name="content", type = String.class),
						@ColumnResult(name="regdate", type = Date.class),
						@ColumnResult(name="likes" , type = Integer.class),
						@ColumnResult(name="dislike", type = Integer.class),
						@ColumnResult(name="level", type = Integer.class),
						@ColumnResult(name="blind", type = Integer.class)
				}
		)
)
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
	private int dislike;
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

	public int getDislike() {
		return dislike;
	}

	public int getBlind() {
		return blind;
	}

	public void incrementLikes() {
		this.likes += 1;
	}

	public void incrementDisLike() {
		this.dislike += 1;
	}

	public void blindComment() {
		this.blind = 1;
	}
}
