package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;

// このクラスの役割
// ・JdbcTemplateではPreparedStatementを使う時、メソッドの引数の順番に注意を払わないといけない
// ・ ∟ NamedParameterJdbcTemplateでは、その心配がない
// ・NamedParameterJdbcTemplateでもRowMapperが使えるが、BeanPropertyRowMapperが使えない。
// ・今回は基本的なCRUD操作のみ実装


@Repository("UserDaoNamedJdbcImpl")
public class UserDaoNamedJdbcImpl implements UserDao{

	@Autowired
	private NamedParameterJdbcTemplate jdbc;

//	==============
//	count()メソッド
//	==============
//	User(table)の件数を取得
	@Override
	public int count() {

//		SQL文
		String sql = "SELECT COUNT(*) FROM m_user";

//		パラメーター生成
		SqlParameterSource params = new MapSqlParameterSource();

//		全件取得してカウント
//		queryForObject：SQLの取得結果を指定したクラスで返す
		return jdbc.queryForObject(sql, params, Integer.class);
	}


//	==================
//	insertOne()メソッド
//	==================
//	User(table)にデータを1件挿入
	@Override
	public int insertOne(User user) {

//		NamedJdbcTemplateではPreparedStatementに?ではなくキー名を使う。「:キー名」
		String sql = "INSERT INTO m_user(user_id, password, user_name, birthday, age, marriage, role)"
				+ " VALUES(:userId, :password, :userName, :birthday, :age, :marriage, :role)";

//		SqlParameterSourceクラス：Sql文に入れるパラメーターを設定する
//		クラスをnewして、そのまま.addValue()メソッドにキーと値をセット。第一引数はキー名、第二引数は値。
//		 ∟ このような呼び出し方をメソッドチェーンという
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId",user.getUserId())
				.addValue("password",user.getPassword())
				.addValue("userName",user.getUserName())
				.addValue("birthday",user.getBirthday())
				.addValue("age",user.getAge())
				.addValue("marriage",user.isMarriage())
				.addValue("role",user.getRole());

//		NamedJdbcParameterのupdateメソッドにSQLとSqlParameterSource(params)を渡す
		return jdbc.update(sql, params);

	}


//	===================
//	selectOne()メソッド
//	===================
//	User(table)のデータを1件取得 ※ユーザーの詳細用
	@Override
	public User selectOne(String userId) {

//		SQL文
		String sql = "SELECT * FROM m_user WHERE user_id = :userId";

//		パラメーター
		SqlParameterSource params = new MapSqlParameterSource().addValue("userId", userId);

//		SQL実行
		Map<String, Object> map = jdbc.queryForMap(sql, params);

//		結果返却用のインスタンスを生成
		User user = new User();

//		取得データをインスタンスにセットしていく
		user.setUserId((String)map.get("user_id"));
		user.setPassword((String)map.get("password"));
		user.setUserName((String)map.get("user_name"));
		user.setBirthday((Date)map.get("birthday"));
		user.setAge((Integer)map.get("age"));
		user.setMarriage((Boolean)map.get("marriage"));
		user.setRole((String)map.get("role"));

		return user;

	}


//	===================
//	selectMany()メソッド
//	===================
//	User(table)の全データを取得
	@Override
	public List<User> selectMany(){

//		SQL文
		String sql = "SELECT * FROM m_user";

//		パラメーター
		SqlParameterSource params = new MapSqlParameterSource();

//		SQL実行
		List<Map<String,Object>> getList = jdbc.queryForList(sql, params);

//		結果返却用のList
		List<User> userList = new ArrayList<>();

//		取得データ分だけループする
		for(Map<String, Object> map : getList) {

//			Userインスタンスの生成
			User user = new User();

//			取得データをインスタンスにセットしていく
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setAge((Integer)map.get("age"));
			user.setMarriage((Boolean)map.get("marriage"));
			user.setRole((String)map.get("role"));

			userList.add(user);
		}
		return userList;
	}


//	===================
//	updateOne()メソッド
//	===================
//	User(table)を1件更新
	@Override
	public int updateOne(User user) {

		String sql = "UPDATE m_user SET password = :password, user_name = :userName, birthday = :birthday, age = :age, marriage = :marriage WHERE user_id = :userId";

//		パラメーター
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId",user.getUserId())
				.addValue("password",user.getPassword())
				.addValue("userName",user.getUserName())
				.addValue("birthday",user.getBirthday())
				.addValue("age",user.getAge())
				.addValue("marriage",user.isMarriage());

//		SQL実行
		return jdbc.update(sql, params);
	}

//	===================
//	deleteOne()メソッド
//	===================
//	User(table)を1件削除
	@Override
	public int deleteOne(String userId) {

		String sql = "DELETE FROM m_user WHERE user_id = :userId";

//		パラメーター
		SqlParameterSource params = new MapSqlParameterSource()
				.addValue("userId", userId);

//		SQL実行
		int rowNumber = jdbc.update(sql, params);

		return rowNumber;

	}


//	===================
//	userCsvOut()メソッド
//	===================
//	User(table)の全データをcsvに出力する
//	SQL取得結果をサーバーにcsvで保存する
	@Override
	public void userCsvOut() {

//		m_userテーブルのデータを全件取得するSQL
		String sql = "SELECT * FROM m_user";

//		ResultSetExtractor の生成
		UserRowCallbackHandler handler = new UserRowCallbackHandler();

//		クエリーの実行&csv出力
		jdbc.query(sql, handler);
	}




}
