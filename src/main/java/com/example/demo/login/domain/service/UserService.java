package com.example.demo.login.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

//このクラスの役割
//・サービスクラス（リポジトリークラスなどを使った色々なサービスを提供）
//・データベースから引っ張ってくるところまでがUserDaoJdbcImple、それを元に実際に使うメソッドとして加工するのがUserService

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

//	===================
//	updateOne()メソッド
//	===================
	public boolean updateOne(User user) {

//		1件更新、rowNumberには更新された件数が入る
		int rowNumber = dao.updateOne(user);

//		判定用変数
		boolean result = false;

//		ちゃんと更新できたか確認
		if(rowNumber > 0) {

//			0より大きい値が返ってきたら成功
			result = true;
		}
		return result;
	}


//	===================
//	deleteOne()メソッド
//	===================
	public boolean deleteOne(String userId) {

//		1件削除、rowNumberには削除された件数が入る
		int rowNumber = dao.deleteOne(userId);

//		判定用変数
		boolean result = false;

//		ちゃんと削除されたか確認
		if(rowNumber > 0) {

//			0より大きい値が返ってきたら成功
			result = true;
		}

		return result;
	}



}
