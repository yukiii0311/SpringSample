package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.GroupOrder;
import com.example.demo.login.domain.model.SignupForm;

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
//	@ModelAttributeをつけると、(SignupForm型のformオブジェクトが）自動でModelクラスに登録される
//	∟ model.addAttribute("signupForm",form）のイメージ
//	∟ デフォルトのキー名はsignupForm（クラス名の最初の文字を小文字に変えた文字列）
	public String getSignUp(@ModelAttribute SignupForm form, Model model) {

//		ラジオボタンの初期化メソッド呼び出し（既婚、未婚のMapを準備）
		radioMarriage = initRadioMarriage();

//		ラジオボタン用のMapをModelに登録
		model.addAttribute("radioMarriage",radioMarriage);

//		signup.htmlに画面遷移
//		（ = ユーザー登録画面）
		return "login/signup";

	}

// =============
//	POSTメソッド
// =============

	@PostMapping("/signup")
//	BindingResult
//	・データバインド：画面入力項目とオブジェクトのフィールドのマッピング
//	・BindingResultクラスのhasErrors()メソッドでデータバインドに失敗したかわかる
//	・= データバインドできなかった場合の処理をhasErrors()メソッドで書く
//	@Validated：バリデーション時に付ける。パラメーターに実行順序のインターフェースを指定すると、バリデーションがグループ実行される。
	public String postSignUp(@ModelAttribute @Validated(GroupOrder.class) SignupForm form, BindingResult bindingResult, Model model) {

//		データバインドの結果の判定
		if(bindingResult.hasErrors()) {

//			失敗したら、getSignUp()メソッドへ
//			直接signup.htmlに遷移させないのは、ラジオボタンの初期化をしないといけないから
			return getSignUp(form, model);
		}

//		↓ データバインドが成功している場合
//		formの中身をコンソールに出して確認
		System.out.println(form);

//		"/login"(LoginController)のgetメソッドに飛ぶ
		return "redirect:/login";

	}


}