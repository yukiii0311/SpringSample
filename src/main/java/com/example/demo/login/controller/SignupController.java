package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignupController {

//	ラジオボタンの実装用Mapの宣言
	private Map<String, String> radioMarriage;

// ============================
//	initRadioMarriage()メソッド
// ============================
//	ラジオボタンの初期化メソッド
//	タイムリーフでラジオボタンの値を動的に変更するためには、Mapを用意する。
	private Map<String, String> initRadioMarriage(){

//		LinkedHashMap:順序保証がある（挿入順）HashMap。通常のHashMapだとHashコード順に並んでしまう。
//					（Mapインターフェースを実装したオブジェクトの一種。）
	Map<String, String> radio = new LinkedHashMap<>();

//	既婚、未婚をMapに格納
	radio.put("既婚","true");
	radio.put("未婚","false");

	return radio;
}


// ============
//	GETメソッド
// ============

	@GetMapping("/signup")
	public String getSignUp(Model model) {

//		ラジオボタンの初期化メソッド呼び出し（既婚、未婚のMapを準備）
		radioMarriage = initRadioMarriage();

//		ラジオボタン用のMapをModelに登録
		model.addAttribute("radioMarriage",radioMarriage);

//		signup.hemlに画面遷移
		return "login/signup";

	}

// =============
//	POSTメソッド
// =============

	@PostMapping("/signup")
	public String postSignUp(Model model) {

//		"/login"(LoginController)のgetメソッドに飛ぶ
		return "redirect:/login";

	}




}