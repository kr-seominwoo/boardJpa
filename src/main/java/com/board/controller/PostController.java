package com.board.controller;

import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.board.entity.Member;
import com.board.entity.Post;
import com.board.repository.PostAllSearch;
import com.board.repository.PostForm;
import com.board.repository.PostSearch;
import jdk.swing.interop.SwingInterOpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.common.LoginCheck;
import com.board.repository.UpdatePostForm;
import com.board.service.PostService;
//import com.board.service.comment.CommentService;

@Controller
@RequestMapping("/board")
public class PostController {
	
	private final PostService postService;
//	private CommentService commentService;

	@Autowired
	public PostController(PostService postService) {
		this.postService = postService;
	}
//	@Autowired
//	public PostController(PostService postService, CommentService commentService) {
//		this.postService = postService;
//		this.commentService = commentService;
//	}

	//게시글 목록
	@GetMapping("/{category}")
	public String home(@PathVariable String category,
			@RequestParam(defaultValue = "title") String field, @RequestParam(defaultValue = "")String query, @RequestParam(defaultValue = "regdate")String order,
			@RequestParam(defaultValue = "1")Integer page, Model model) {
		PostAllSearch postAllSearch = new PostAllSearch(field, query, order, category, page);
		long lastPage = (long)Math.ceil((double)postService.findTotalCount(postAllSearch) / PostConst.COUNT_PER_PAGE);
		long begin = ((long)Math.ceil((double)postAllSearch.getPage() / PostConst.PAGER) - 1) * PostConst.PAGER + 1;
		long end = begin + PostConst.PAGER - 1;

		
		List<Post> postList = postService.findPosts(postAllSearch);
//		Map<Long, Integer> commentCntMap = commentService.findCommentCounts(postList, category);
		model.addAttribute("postList", postList);
		model.addAttribute("category", category);
		model.addAttribute("page", postAllSearch.getPage());
		model.addAttribute("begin", begin);
		model.addAttribute("end", Math.min(lastPage, end));
		model.addAttribute("lastPage", lastPage);
//		model.addAttribute("commentCntMap", commentCntMap);
		model.addAttribute("postSearch", postAllSearch);
		return "index";
	}
	
	//게시글 세부 목록
	@GetMapping("/{category}/{subCategory}")
	public String detailHome(@PathVariable String category, @PathVariable String subCategory, 
			@RequestParam(defaultValue = "title") String field, @RequestParam(defaultValue = "")String query, @RequestParam(defaultValue = "regdate")String order,
			@RequestParam(defaultValue = "1")Integer page, Model model) {
		PostSearch postSearch = new PostSearch(field, query, order, category, subCategory, page);

		long lastPage = (long)Math.ceil((double)postService.findTotalCount(postSearch) / PostConst.COUNT_PER_PAGE);
		long begin = ((long)Math.ceil((double)postSearch.getPage() / PostConst.PAGER) - 1) * PostConst.PAGER + 1;
		long end = begin + PostConst.PAGER - 1;
		
		List<Post> postList = postService.findPosts(postSearch);
//		Map<Long, Integer> commentCntMap = commentService.findCommentCounts(postList, category);
		model.addAttribute("postList", postList);
		model.addAttribute("postList", postList);
		model.addAttribute("category", category);
		model.addAttribute("page", postSearch.getPage());
		model.addAttribute("begin", begin);
		model.addAttribute("end", Math.min(lastPage, end));
		model.addAttribute("lastPage", lastPage);
//		model.addAttribute("commentCntMap", commentCntMap);
		model.addAttribute("postSearch", postSearch);
		return "index";
	}
	
	//게시글 상세
	@GetMapping("/{category}/{subCategory}/{id}")
	public String detail(HttpServletRequest request,@PathVariable String category, @PathVariable String subCategory, @PathVariable Long id, Model model) {
		Post post = postService.findPost(category, subCategory, id);
		model.addAttribute("category", category);
		model.addAttribute("subCategory", subCategory);
		model.addAttribute("post", post);
		Member member = LoginCheck.getMemberFromSession(request);
		if(LoginCheck.isLoggedIn(member)) {
			model.addAttribute("memberId", member.getId());
		}
		
		return "detail";
	}
	
	//게시글 작성 페이지 조회
	@GetMapping("/new/{category}")
	public String createForm(HttpServletRequest request, @PathVariable String category, Model model) {
		if(LoginCheck.isLoggedIn(LoginCheck.getMemberFromSession(request))) {
    		model.addAttribute("category", category);
    		return "write";
    	} else {
    		return "redirect:/members/login";
    	}  
	}

	//게시글 작성
	@PostMapping("/new/{category}")
	public String createPost(HttpServletRequest request, @PathVariable String category, String subCategory, String title, String content, Model model) {
		Member member = LoginCheck.getMemberFromSession(request);
		if(LoginCheck.isLoggedIn(member)) {
			PostForm postForm = new PostForm(member.getId(), member.getNickname(), title, content, category, subCategory);
	        postService.savePost(category, postForm);
	        
			return "redirect:/board/" + category;
		} else {
			return "redirect:/members/login";
		}
	}
	
	//게시글 수정 페이지 조회
	@GetMapping("/edit/{category}/{subCategory}/{id}")
	public String updateForm(HttpServletRequest request, @PathVariable String category,@PathVariable String subCategory, @PathVariable Long id, Model model) {
		if(LoginCheck.isLoggedIn(LoginCheck.getMemberFromSession(request))) {
			Post post = postService.findPost(category, subCategory, id);
    		model.addAttribute("post", post);
    		return "edit";
    	} else {
    		return "redirect:/members/login";
    	} 
	}
		
	//게시글 수정
	@PostMapping("/edit/{category}/{subCategory}/{id}")
	public String updatePost(HttpServletRequest request, @PathVariable String category, @PathVariable String subCategory, @PathVariable Long id, Model model, String title, String content) {
		Member member = LoginCheck.getMemberFromSession(request);
		if(LoginCheck.isLoggedIn(member)) {
			UpdatePostForm updatePostForm = new UpdatePostForm(id, member.getId(), title, content);
			postService.updatePost(category, updatePostForm);
			return "redirect:/board/" + category + "/" + subCategory + "/" + id;
		} else {
			return "redirect:/members/login";
		}
	}
	
//	//게시글 좋아요
//	@PostMapping(value = {"/like/{category}/{id}"})
//	@ResponseBody
//	public int updateLike(@PathVariable String category, @PathVariable Long id, Model model) {
//		return postService.updateCount("LIKE", category, id);
//	}
//
//	//게시글 싫어요
//	@PostMapping(value = {"/unlike/{category}/{id}"})
//	@ResponseBody
//	public int updateUnlike(@PathVariable String category, @PathVariable Long id, Model model) {
//		return postService.updateCount("UNLIKE", category, id);
//	}
//
	//게시글 삭제
	@PostMapping("/delete/{category}/{id}")
	public String deletePost(HttpServletRequest request, @PathVariable String category, @PathVariable Long id) {
		System.out.println("delete");
		Member member = LoginCheck.getMemberFromSession(request);
		if(LoginCheck.isLoggedIn(member)) {
			postService.deletePost(member.getId(),category, id);
			return "redirect:/board/" + category;
    	} else {
    		return "redirect:/members/login";
    	} 
	}
}

