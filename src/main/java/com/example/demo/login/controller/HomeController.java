package com.example.demo.login.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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


//  ユーザー一覧のcsv出力用
//	このメソッドでやりたいこと
//	・ユーザー画面でCSV出力できるようにする
	@GetMapping("/userList/csv")
//	戻り値をResponseEntity型にすると、ファイル（byte型の配列）を呼び出し元に返却できる。
	public ResponseEntity<byte[]> getUserListCsv(Model model) {

//		ユーザーを全件取得して、CSVをサーバーに保存する
		userService.userCsvOut();

//		byte型配列を宣言
		byte[] bytes = null;

		try {
//			サーバーに保存されているsample.csvファイルをbyteで取得する
			bytes = userService.getFile("sample.csv");

		}catch (IOException e) {
//			もしエラーが起きたら、エラー内容表示
			e.printStackTrace();

		}

//		HTTPヘッダーの設定
		HttpHeaders header = new HttpHeaders();

//		ダウンロード時の文字コード設定
		header.add("Content-Type", "text/csv; charset = UTF-8");

//		ダウンロード時のファイルの名前
		header.setContentDispositionFormData("filename","sample.csv");

		return new ResponseEntity<>(bytes, header, HttpStatus.OK);


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



//	== ▼ ユーザー詳細画面からの変更（更新）用POSTメソッド ▼ ==

//	更更新ボタンと削除ボタンのどちらを押しても /userDetail にPOSTする
//	↑を区別するためにparams属性を使う。htmlの更新ボタンに「name = "update"」を足す。
	@PostMapping(value = "/userDetail", params = "update")

	public String postUserDetailUpdate(@ModelAttribute SignupForm form, Model model) {

		System.out.println("更新ボタンの処理");

//		変更内容を詰めるためのuserオブジェクトを生成
		User user = new User();

//		formクラスオブジェクト（入力フォーム用）からUserクラスオブジェクト（データベースとのやりとり用）に変換
		user.setUserId(form.getUserId());
		user.setPassword(form.getPassword());
		user.setUserName(form.getUserName());
		user.setBirthday(form.getBirthday());
		user.setAge(form.getAge());
		user.setMarriage(form.isMarriage());

//	↓トランザクション処理勉強のためのtry
//		try {

//	更新実行
//  更新できたら、resultにtrueが入る
	boolean result = userService.updateOne(user);

//	結果に合わせて、表示メッセージを登録（model）
	if(result == true) {

		model.addAttribute("result","更新成功");

	}else {
		model.addAttribute("result","更新失敗");
	}

//	トランザクション処理（UserDaoJdbcImplと対応）
//		}catch(DataAccessException e) {
//			model.addAttribute("result","更新失敗(トランザクションテスト)");
//		}

//	上の結果を詰めたmodelを引数にgetUserList()へ
	return getUserList(model);

	}

//	== ▼ ユーザー詳細画面からの削除用POSTメソッド ▼ ==

//	更更新ボタンと削除ボタンのどちらを押しても /userDetail にPOSTする
//	↑を区別するためにparams属性を使う。htmlの更新ボタンに「name = "delete"」を足す。
	@PostMapping(value = "/userDetail", params = "delete")
	public String postUserDetailDelete(@ModelAttribute SignupForm form, Model model) {

		System.out.println("削除ボタンの処理");

//		削除実行
//		更新できたら、resultにtrueが入る
		boolean result = userService.deleteOne(form.getUserId());

//		結果に合わせて、表示メッセージを登録（model）
		if(result == true) {

			model.addAttribute("result","削除成功");

		}else {

			model.addAttribute("result","削除失敗");
			}

		return getUserList(model);

	}







}
