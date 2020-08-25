package com.example.demo.login.domain.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class SignupForm {

	private String userId;
	private String password;
	private String userName;

//	@DataTimeFormat：画面から渡されてきた文字列を日付型に変換
//	pattern属性でどんなフォーマットでデータが渡されてくるか指定する
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date birthday;

	private int age;
	private boolean marriage;




}
