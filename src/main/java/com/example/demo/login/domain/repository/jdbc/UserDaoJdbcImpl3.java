package com.example.demo.login.domain.repository.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;

// UserDaoJdbcImpl3はこれのみで完結（BeanPropertyRowMapperを活用）
@Repository("UserDaoJdbcImpl3")
public class UserDaoJdbcImpl3 extends UserDaoJdbcImpl{

	@Autowired
	private JdbcTemplate jdbc;

//	ユーザー1件取得
	@Override
	public User selectOne(String userId) {

//		1件取得用SQL
		String sql = "SELECT * FROM m_user WHERE user_id = ?";

//		RowMapperの生成
//		BeanPropertyRowMapper
//		・データベースから取得してきたカラム名と同一のフィールド名がクラスにあれば自動でマッピング
//		・ ∟ RowMapperのようにどのカラムとどのフィールドを一致させるか、用意する必要がない
//		・*注意点*
//			・カラム名（データベース）は単語をアンダースコア（_）で区切る。スネークケース。user_idなど
//			・フィールド名（model、今回でいうUserオブジェクト）は2つ目の単語から大文字にする。キャメルケース。userIdなど
//		・↑この2つの条件を満たすのならBeanPropertyRowMapperを使うとよい
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);

//		SQL実行
		return jdbc.queryForObject(sql, rowMapper, userId);


	}


	@Override
	public List<User> selectMany(){

//		m_userテーブルのデータを全件取得するSQL
		String sql = "SELECT * FROM m_user";

//		RowMapperの生成
//		BeanPropertyRowMapper
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);

//		SQL実行
		return jdbc.query(sql, rowMapper);



	}



}
