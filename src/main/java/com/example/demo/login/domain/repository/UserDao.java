package com.example.demo.login.domain.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.example.demo.login.domain.model.User;

// このインターフェースの役割
// ・リポジトリークラスのインターフェース（リポジトリークラス：DBへのCRUD操作を行い、その結果を返す）
// ・中身の実装クラスをJdbcTemplateとNamedParameterJdbcTemplateで簡単に切り替えられるように

public interface UserDao {

//	===== 全項目共通 =====
//  DataAccessException
//	・Springでは、データベース操作で例外が発生した場合、上記Exceptionに投げる


//	User(table)の件数を取得
	public int count() throws DataAccessException;

//	User(table)にデータを1件挿入
	public int insertOne(User user) throws DataAccessException;

//  User(table)のデータを1件取得
	public User selectOne(String userId) throws DataAccessException;

//	User(table)の全データを取得
	public List<User> selectMany() throws DataAccessException;

//	User(table)を1件更新
	public int updateOne(User user) throws DataAccessException;

//	User(table)を1件削除
	public int deleteOne(String userId) throws DataAccessException;

//	SQL取得結果をサーバーにCSVで保存する
	public void userCsvOut() throws DataAccessException;




}
