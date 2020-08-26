package com.example.demo.login.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

	// ============
	//	GETメソッド
	// ============

	@GetMapping("/login")
	public String getLogin(Model model) {

	//	loginフォルダのlogin.htmlに画面遷移
		return "login/login";
	}


	// =============
	//	POSTメソッド
	// =============
	@PostMapping("/login")
	public String postLogin(Model model) {


	//	一旦、ログインボタンを押すと、無条件でホーム画面に遷移
		return "redirect:/home";

	}
}
