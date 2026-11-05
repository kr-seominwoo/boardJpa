package com.board.service.comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.board.controller.comment.CommentForm;
import com.board.entity.Comment;
import com.board.entity.Member;
import com.board.entity.Post;

import com.board.repository.jpa.comment.CommentDTO;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import com.board.repository.CommentRepository;

@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public CommentService(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}

	@Transactional
	public Comment createComment(Member member, CommentForm commentForm) {
		Comment comment = null;

		Long id = commentRepository.insertComment(member, commentForm);
		if (id != 0) {
			comment = commentRepository.findOne(id);
		}

		return comment;
	}

	public List<CommentDTO> findComments(Long postId, int page) {
		return commentRepository.findComments(postId, page);
	}

	public List<CommentDTONode> findReplyComments(Long postId, Long parentId) {
		List<CommentDTONode> nodes = new ArrayList<>();
		List<CommentDTO> list = commentRepository.findReplyComments(postId, parentId);
		for (int idx = 0, SIZE = list.size(); idx < SIZE; ++idx) {
			CommentDTO comment = list.get(idx);
			int level = comment.getLevel();
			if (level == 1) {
				CommentDTONode node = new CommentDTONode(comment);
				nodes.add(node);
			} else {
				addCommentDTO(nodes.get(nodes.size() - 1), comment);
			}
		}

		return nodes;
	}

	private void addCommentDTO(CommentDTONode parent, CommentDTO comment) {
		int parentLevel = parent.getCommentDTO().getLevel();
		int level = comment.getLevel();

		List<CommentDTONode> children = parent.getChildren();
		if (level == parentLevel + 1) {
			CommentDTONode node = new CommentDTONode(comment);
			children.add(node);
		} else {
			int lastIndex = children.size() - 1;
			addCommentDTO(children.get(lastIndex), comment);
		}
	}

	public Map<Long, Integer> findCommentCounts(List<Post> postList, String category) {
		if (postList.isEmpty()) {
			return new HashMap<>();
		}

		return commentRepository.findCounts(postList, category);
	}

	public Long findCommentTotal(String category, Long postId) {
		return commentRepository.findTotalCount(postId);
	}

	public Long findTopCommentTotal(String category, Long postId) {
		return commentRepository.findTopTotalCount(postId);
	}

	public CommentDTO updateContent(Member member, String category, Long commentId, String content) {
		CommentDTO result = null;
		if(commentRepository.updateContent(category, commentId, content) == 1) {
			Comment comment = commentRepository.findOne(commentId);
			result = new CommentDTO(comment.getId(), comment.getMemberId(), comment.getWriter(), comment.getContent(), comment.getRegdate(), comment.getLikes(), comment.getDislike(), 0, comment.getBlind());
		}

		return result;
	}

	@Transactional
	public int like(Long commentId) {
		Comment comment = commentRepository.findOne(commentId);
		comment.incrementLikes();
		return comment.getLikes();
	}

	@Transactional
	public int dislike(Long commentId) {
		Comment comment = commentRepository.findOne(commentId);
		comment.incrementDisLike();
		return comment.getDislike();
	}

	@Transactional
	public int deleteComment(Member member, String category, Long commentId) {
		if (isMyComment(member, commentId)) {
			Comment comment = commentRepository.findOne(commentId);
			comment.blindComment();
			return 1;
		}

		return 0;
	}

	public boolean isMyComment(Member member, Long commentId) {
		Comment comment = commentRepository.findOne(commentId);
        return comment.getMemberId().equals(member.getId());
	}
}
