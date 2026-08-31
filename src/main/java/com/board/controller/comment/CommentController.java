package com.board.controller.comment;

import java.io.IOException;
import java.util.List;

import com.board.repository.jpa.comment.CommentDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.board.entity.Comment;
import com.board.entity.Member;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.board.common.LoginCheck;
import com.board.service.comment.CommentDTONode;
import com.board.service.comment.CommentService;

@Controller
@RequestMapping("/board/{category}/{postId}/comment/")
public class CommentController {

	private final CommentService commentService;

	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}

	@PostMapping("/create")
	public ModelAndView createComment(@PathVariable String category, @PathVariable Long postId, String content, ModelAndView mv, HttpServletRequest request, HttpServletResponse response) throws IOException {
		Member member = LoginCheck.getMemberFromSession(request);
		if (!LoginCheck.isLoggedIn(member)) {
			mv.addObject("message", CommentErrorMessage.LOGIN_ERROR);
			mv.setViewName("error");
			mv.setStatus(HttpStatus.UNAUTHORIZED);
			return mv;
		}

		CommentForm commentForm = new CommentForm(postId, 0L, category, content);
		Comment comment = commentService.createComment(member, commentForm);
		if (comment == null) {
			mv.addObject("message", CommentErrorMessage.SERVER_ERROR);
			mv.setViewName("error");
			mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			mv.addObject("member", member);
			mv.setViewName("redirect:/board/" + category + "/" + postId + "/comment/list?page=" + 1);
		}

		return mv;
	}

	@PostMapping("/reply/{parentId}")
	public ModelAndView createReplyComment(@PathVariable String category, @PathVariable Long postId,
			@PathVariable Long parentId, String content, HttpServletRequest request, HttpServletResponse response,
			ModelAndView mv) throws IOException {
		Member member = LoginCheck.getMemberFromSession(request);
		if (!LoginCheck.isLoggedIn(member)) {
			mv.addObject("message", CommentErrorMessage.LOGIN_ERROR);
			mv.setViewName("error");
			mv.setStatus(HttpStatus.UNAUTHORIZED);
			return mv;
		}

		CommentForm commentForm = new CommentForm(postId, parentId, category, content);
		Comment comment = commentService.createComment(member, commentForm);
		if (comment == null) {
			mv.addObject("message", CommentErrorMessage.SERVER_ERROR);
			mv.setViewName("error");
			mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
		} else {
			mv.addObject("member", member);
			mv.setViewName("replyComment");
			CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getMemberId(), comment.getWriter(),
					comment.getContent(), comment.getRegdate(), comment.getLikes(), comment.getUnlike(), 0,
					comment.getBlind());
			mv.addObject("comment", commentDTO);
		}

		return mv;
	}

	@GetMapping("/list")
	public String list(@PathVariable String category, @PathVariable Long postId, int page, Model model,
			HttpServletRequest request) {
		Member member = LoginCheck.getMemberFromSession(request);
		model.addAttribute("member", member);

		long total = commentService.findCommentTotal(category, postId);
		long topTotal = commentService.findTopCommentTotal(category, postId);
		List<CommentDTO> list = commentService.findComments(postId, page);

		int last = (int) (topTotal % CommentConst.COUNT_PER_PAGE == 0 ? topTotal / CommentConst.COUNT_PER_PAGE
				: topTotal / CommentConst.COUNT_PER_PAGE + 1);
		int begin = ((int) Math.ceil((double) page / CommentConst.PAGER) - 1) * CommentConst.PAGER + 1;
		int end = last <= begin + 4 ? last : begin + 4;

		model.addAttribute("commentTotal", total);
		model.addAttribute("commentList", list);
		model.addAttribute("page", page);
		model.addAttribute("category", category);
		model.addAttribute("postId", postId);
		model.addAttribute("begin", begin);
		model.addAttribute("end", end);
		model.addAttribute("lastPage", last);
		return "commentList";
	}

	@GetMapping("/reply/{commentId}")
	public String reply(@PathVariable String category, @PathVariable Long postId, @PathVariable long commentId,
			Model model, HttpServletRequest request) {
		Member member = LoginCheck.getMemberFromSession(request);
		model.addAttribute("member", member);

		List<CommentDTONode> list = commentService.findReplyComments(category, postId, commentId);
		model.addAttribute("list", list);
		return "replyCommentList";
	}

	@ResponseBody
	@GetMapping("/total")
	public Long commentTotal(@PathVariable String category, @PathVariable Long postId) {
		return commentService.findCommentTotal(category, postId);
	}

	@ResponseBody
	@PostMapping("/count")
	public int up(@PathVariable String category, String column, Long commentId) {
		return commentService.updateCount(category, column.toUpperCase(), commentId);
	}

	@PostMapping("/update/{commentId}")
	public ModelAndView update(@PathVariable String category, @PathVariable Long commentId, String content, ModelAndView mv,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Member member = LoginCheck.getMemberFromSession(request);
		if (!LoginCheck.isLoggedIn(member)) {
			mv.setStatus(HttpStatus.UNAUTHORIZED);
			mv.addObject("message", CommentErrorMessage.LOGIN_ERROR);
			mv.setViewName("error");
			return mv;
		}

		if (!commentService.isMyComment(member, category, commentId)) {
			mv.setStatus(HttpStatus.UNAUTHORIZED);
			mv.addObject("message", CommentErrorMessage.MY_COMMENT_ERROR);
			mv.setViewName("error");
			return mv;
		}

		CommentDTO comment = commentService.updateContent(member, category, commentId, content);
		if (comment == null) {
			mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			mv.addObject("message", CommentErrorMessage.SERVER_ERROR);
			mv.setViewName("error");
			return mv;
		}

		mv.addObject("comment", comment);
		mv.addObject("member", member);
		mv.setViewName("comment");
		return mv;
	}

	@PostMapping("/delete/{commentId}")
	public ResponseEntity<String> delete(@PathVariable String category, @PathVariable Long commentId, Model model,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		Member member = LoginCheck.getMemberFromSession(request);
		if (!LoginCheck.isLoggedIn(member)) {
			return new ResponseEntity<String>(CommentErrorMessage.LOGIN_ERROR, HttpStatus.UNAUTHORIZED);
		}

		if (!commentService.isMyComment(member, category, commentId)) {
			return new ResponseEntity<String>(CommentErrorMessage.MY_COMMENT_ERROR, HttpStatus.UNAUTHORIZED);
		}

		int result = commentService.deleteComment(member, category, commentId);
		if (result == 0) {
			return new ResponseEntity<String>(CommentErrorMessage.SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<String>("삭제돼었습니다.", HttpStatus.OK);
	}
}
