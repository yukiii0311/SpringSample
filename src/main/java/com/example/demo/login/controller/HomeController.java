package com.example.demo.login.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.service.UserService;

@Controller
public class HomeController {

	@Autowired
	UserService userService;

	// ============
	//	GETメソッド
	// ============

	@GetMapping("/home")
	public String getHome(Model model) {

//		model領域にコンテンツ部分にホーム画面を表示するための文字列を登録
//		homeLayout.htmlの「th:include="__${contents}__"」が「th:include="login/home :: home_contents"」になる
		model.addAttribute("contents","login/home :: home_contents");

//		homeLayout.htmlに遷移
//		( = ホーム画面)
		return "login/homeLayout";
	}

	// =============
	//	POSTメソッド
	// =============

	@PostMapping("/logout")
	public String postLogout() {

//		LoginControllerのgetメソッドに遷移
//		（ = ログイン画面）
		return "redirect:/login";
	}



}
