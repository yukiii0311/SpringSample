package com.example.demo.login.controller;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//このクラスの役割
//・このクラス（１つのクラス）でアプリケーション全体の例外処理を実装

//@ControllerAdviceアノテーションをつけたクラスを用意しておくと、アプリケーション全体で発生した例外処理を実装できる。
//@ExceptionHandlerをつけたメソッドを用意するだけ。
@ControllerAdvice
@Component
public class GlobalControllAdvice {

	@ExceptionHandler(DataAccessException.class)
	public String dataAccsessExceptionHandler(DataAccessException e, Model model) {

//		例外クラスのメッセージをModelに登録
		model.addAttribute("error","内部サーバーエラー（DB）：GlobalControllAdvice");

//		例外クラスのメッセージをModelに登録
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}

	@ExceptionHandler(Exception.class)
	public String exceptionHandler(Exception e, Model model) {

//		例外クラスのメッセージをModelに登録
		model.addAttribute("error","内部サーバーエラー：GlobalControllAdvice");

//		HTTPのエラーコード（500）をModelに登録
		model.addAttribute("status",HttpStatus.INTERNAL_SERVER_ERROR);

		return "error";
	}

}
