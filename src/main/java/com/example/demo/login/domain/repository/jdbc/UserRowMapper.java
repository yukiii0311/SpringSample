package com.example.demo.login.domain.repository.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.login.domain.model.User;

// RowMapperについて
// ・データベースのレコード（行）と、Javaオブジェクトのマッピングを行うクラス
// ・Select結果とUserクラスを予めマッピングしておくことができる
// ・リポジトリークラスの記述が楽になる。


// RowMapperを使用するには、RowMapper<?>インターフェースを継承する
// ?の部分には、マッピングに使うjavaオブジェクトのクラスを指定する
public class UserRowMapper implements RowMapper<User>{

//	RowMapperを継承して、mapRowメソッドをオーバーライド
//	引数のResultSetにはSelect結果が入っている → Userオブジェクトにセット

	@Override
//	ResultSetはSQLを実行して抽出した表、rowNumは行番号
	public User mapRow(ResultSet rs, int rowNum) throws SQLException{

//		戻り値のUserインスタンスを生成
		User user = new User();

//		ResultSetの取得結果をUserインスタンスにセット
		user.setUserId(rs.getString("user_id"));
		user.setPassword(rs.getString("password"));
		user.setUserName(rs.getString("user_name"));
		user.setBirthday(rs.getDate("birthday"));
		user.setAge(rs.getInt("age"));
		user.setMarriage(rs.getBoolean("marriage"));
		user.setRole(rs.getString("role"));

		return user;

	}

}
