package com.example.demo.login.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

//このクラスの役割
//・サービスクラス（リポジトリークラスなどを使った色々なサービスを提供）

@Service
public class UserService {

	@Autowired
	UserDao dao;

//	===============
//	insert()メソッド
//	===============
	public boolean insert(User user) {

//		insert実行。登録した件数が返ってくる
		int rowNumber = dao.insertOne(user);

//		判定用変数（デフォルトではfalse）
		boolean result = false;

//		登録した件数が0件より大きければ
		if(rowNumber > 0) {

//			trueを返す
			result = true;
		}
//		登録できていなければ、falseを返す
		return result;
	}


//	===============
//	count()メソッド
//	===============
	public int count() {
		return dao.count();
	}


//	===================
//	selectMany()メソッド
//	===================
	public List<User> selectMany(){

//		全件取得
		return dao.selectMany();
	}


//	===================
//	selectOne()メソッド
//	===================
	public User selectOne(String userId) {
		return dao.selectOne(userId);
	}




}
