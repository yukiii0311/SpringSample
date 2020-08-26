package com.example.demo.login.domain.repository.jdbc;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;


//	このクラスの役割
//	・UserDaoの実装
//	・このクラスのメソッドを使って、SQLを実行

@Repository
public class UserDaoJdbcImpl implements UserDao {

//	JdbcTemplateはspringが用意している (=既にBean定義済み) → @Autowiredだけで使える
	@Autowired
	JdbcTemplate jdbc;

//	User(table)の件数を取得
	@Override
	public int count() throws DataAccessException{
		return 0;
	}

//	User(table)にデータを1件挿入
	@Override
	public int insertOne(User user) throws DataAccessException {
		return 0;
	}

//	User(table)のデータを1件取得
	@Override
	public User selectOne(String userId) throws DataAccessException{
		return null;
	}

//	User(table)の全データを取得
	@Override
	public List<User> selectMany() throws DataAccessException{
		return null;

	}

//	User(table)を1件更新
	@Override
	public int updateOne(User user) throws DataAccessException{
		return 0;
	}

//	User(table)を1件削除
	@Override
	public int deleteOne(String userId) throws DataAccessException {
		return 0;
	}

//	User(table)の全データをcsvに出力する
	@Override
	public void userCsvOut() throws DataAccessException{

	}



}
