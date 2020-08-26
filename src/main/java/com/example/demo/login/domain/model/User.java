package com.example.demo.login.domain.model;

import java.util.Date;

import lombok.Data;

// このクラスの役割
// ・データベースから取得した値を、コントローラークラスやサービスクラスなどの間でやりとりするためのクラス

@Data
public class User {

	private String userId;
	private String password;
	private String userName;
	private Date birthday;
	private int age;
	private boolean marriage;
	private String role;


}
