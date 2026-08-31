package com.board.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {

	@GetMapping(value = {"index",""})
	public String index() {
		return "redirect:/board/free";
	}

//	@GetMapping(value = {"index",""})
//	public String index(Model model, Integer page, String field, String query) {
//		System.out.println("home");
//		return "redirect:/board/free";
//	}

}