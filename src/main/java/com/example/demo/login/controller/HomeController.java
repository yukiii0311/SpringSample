package com.example.demo.login.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.login.domain.model.SignupForm;
import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.service.UserService;

@Controller
public class HomeController {

	@Autowired
	UserService userService;

//	ラジオボタンの実装用Mapの宣言
	private Map<String, String> radioMarriage;

//		==========================
//		initRadioMarriage()メソッド
// 		==========================
//		ラジオボタンの初期化メソッド
//		タイムリーフでラジオボタンの値を動的に変更するためには、Mapを用意する。
		private Map<String, String> initRadioMarriage(){

//			LinkedHashMap:順序保証がある（挿入順）HashMap。通常のHashMapだとHashコード順に並んでしまう。
//						（Mapインターフェースを実装したオブジェクトの一種。）
		Map<String, String> radio = new LinkedHashMap<>();

//		既婚、未婚をMapに格納
		radio.put("既婚","true");
		radio.put("未婚","false");

		return radio;
	}



	// ===============
	//	GETメソッド各種
	// ===============

//	== ▼ ホーム画面用のGETメソッド ▼ ==
	@GetMapping("/home")
	public String getHome(Model model) {

//		model領域に、コンテンツ部分にホーム画面を表示するための文字列を登録
//		homeLayout.htmlの「th:include="__${contents}__"」が「th:include="login/home :: home_contents"」になる
		model.addAttribute("contents","login/home :: home_contents");

//		homeLayout.htmlに遷移
//		( = ホーム画面)
		return "login/homeLayout";
	}



//	== ▼ ユーザー一覧画面のGET用メソッド ▼ ==
	@GetMapping("/userList")
//	このメソッドでやりたいこと
//	・ホーム画面の「ユーザー管理」というリンクをクリックすると、データベースから全ユーザーの情報をとってくる
	public String getUserList(Model model) {

//		コンテンツ部分にユーザー一覧を表示させるための文字列を登録。
//		第二引数は"<ファイルパス>::<th:fragment属性の値>"
		model.addAttribute("contents","login/userList :: userList_contents");

//		ユーザー一覧の生成（右辺は全件取得、それをUser型のListに突っ込む）
		List<User> userList = userService.selectMany();

//		Modelにユーザー一覧を登録
		model.addAttribute("userList", userList);

//		データの件数を取得
		int count = userService.count();

//		Modelに件数を登録
		model.addAttribute("userListCount",count);

//		homeLayout.htmlに遷移
//		( = ユーザー一覧画面)
		return "login/homeLayout";

	}

//	== ▼ ユーザー詳細画面のGET用メソッド ▼ ==

//	動的URLに対応したメソッド（選択されたuserIdに合わせてurlが変わる）を作る時
//	@GetMappingに/{<変数名>}とする
//	・∟ 通常は/userDetail/{id}とすればいいが、今回はuserIdがメールアドレス形式のためうまくいかない
//	・例：yamada@xxx.co.jpというuserIdが渡されると、yamada@xxx.coしか受け取れない → 正規表現を使用
//	@PathVariable：渡されてきたパス（URL）の値を引数の変数に入れることができる
//	・例：「~userDetail/yamada@xxx.co.jp」というURLでリクエストがきた時、yamada@xxx.co.jpという値が引数のuserId（変数）に入れられる
	@GetMapping("/userDetail/{id:.+}")
	public String getUserDetail(@ModelAttribute SignupForm form, Model model, @PathVariable("id") String userId) {

//		ユーザーのID確認（コンソールに表示）
		System.out.println("userId = " + userId);

//		model領域に、コンテンツ部分にユーザー詳細を表示するための文字列を登録
//		homeLayout.htmlの「th:include="__${contents}__"」が「th:include="login/userDetail :: userDetail_contents"」になる
		model.addAttribute("contents","login/userDetail :: userDetail_contents");

//		結婚ステータス用ラジオボタンの初期化
		radioMarriage = initRadioMarriage();

//		↑ラジオボタンをModelに登録
		model.addAttribute("radioMarriage",radioMarriage);

//		ユーザーIDチェック
		if(userId !=null && userId.length() > 0) {

//			ユーザー情報を取得（引数のuserIdで1件調べてきて、userに格納）
			User user = userService.selectOne(userId);

//			Userクラス(データベースから）をformクラス（従業員登録フォーム）に変換
			form.setUserId(user.getUserId());
			form.setUserName(user.getUserName());
			form.setBirthday(user.getBirthday());
			form.setAge(user.getAge());
			form.setMarriage(user.isMarriage());

//			Modelに登録
			model.addAttribute("signupForm", form);
		}

		return "login/homeLayout";
	}






	@GetMapping("/userList/csv")
//	このメソッドでやりたいこと
//	・ユーザー画面でCSV出力できるようにする（あとで）
	public String getUserListCsv(Model model) {

		return getUserList(model);
	}







	// ================
	//	POSTメソッド各種
	// ================

	@PostMapping("/logout")
	public String postLogout() {

//		LoginControllerのgetメソッドに遷移
//		（ = ログイン画面）
		return "redirect:/login";
	}



}
