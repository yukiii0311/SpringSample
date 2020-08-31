package com.example.demo.login.domain.repository.jdbc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.login.domain.model.User;
import com.example.demo.login.domain.repository.UserDao;


//	このクラスの役割
//	・UserDaoの実装
//	・このクラスのメソッドを使って、SQLを実行。データベースから値を引っ張ってくる。

// @RepositoryにBean名をセット
// @Autowiredする時、どのクラスを使用するか指定できる
@Repository("UserDaoJdbcImpl")
public class UserDaoJdbcImpl implements UserDao {

//	JdbcTemplateはspringが用意している (=既にBean定義済み) → @Autowiredだけで使える
	@Autowired
	JdbcTemplate jdbc;


//	==============
//	count()メソッド
//	==============
//	User(table)の件数を取得
	@Override
	public int count() throws DataAccessException{

//		全件取得してカウント
//		カウントの結果や、カラムを1つだけ取得してくる時はqueryForObjectを使用。第一引数にSQL文、第二引数に戻り値のオブジェクトclass
		int count = jdbc.queryForObject("SELECT COUNT(*) FROM m_user",Integer.class);

		return count;
	}

//	==================
//	insertOne()メソッド
//	==================
//	User(table)にデータを1件挿入
	@Override
	public int insertOne(User user) throws DataAccessException {

//		JdbcTemplateクラスを使ってinsertするには、update()メソッドを使う。（更新、削除も）
//		m_user：table名（ユーザーマスタ）
		int rowNumber = jdbc.update("INSERT INTO m_user(user_id, password, user_name, birthday, age, marriage, role) "
				+ "VALUES (?,?,?,?,?,?,?)"
				,user.getUserId(), user.getPassword(), user.getUserName(), user.getBirthday(), user.getAge(), user.isMarriage(), user.getRole());

//		戻り値は、登録した件数が返ってくる
				return rowNumber;

	}



//	===================
//	selectOne()メソッド
//	===================
//	User(table)のデータを1件取得 ※ユーザーの詳細用
	@Override
	public User selectOne(String userId) throws DataAccessException{

//		1件のレコード取得なのでqueryForMap()メソッドを使用。戻り値はMap<String,Object>型（1件なのでListではない）
//		第一引数にSQL分、第二引数にパラメータ
		Map<String, Object> map = jdbc.queryForMap("SELECT * FROM m_user WHERE user_id = ?",userId);

//		結果返却用の変数
		User user = new User();

//		取得したデータを結果返却用の変数にセットしていく
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
	public List<User> selectMany() throws DataAccessException{

//		全件取得
//		複数件のselectを行う場合は、queryForList()メソッドを使う
//		戻り値の型：List<Map<String,Object>>を指定。Listが行（1人分）、Mapが列（カラム名とその値）。
		List<Map<String,Object>> getList = jdbc.queryForList("SELECT * FROM m_user");

//		結果返却用の変数
		List<User> userList = new ArrayList<>();

//		取得したデータを結果返却用のListに格納していく
		for(Map<String,Object> map: getList) {

//			Userインスタンスの生成
			User user = new User();

//			Userインスタンスに取得した値をセットする
//			引数の中：map.get("データベースのカラム名"）で該当の値（※オブジェクト型）を取得し、Userモデルのフィールドの型にキャスト
//			↑1人ずつ行っていく
			user.setUserId((String)map.get("user_id"));
			user.setPassword((String)map.get("password"));
			user.setUserName((String)map.get("user_name"));
			user.setBirthday((Date)map.get("birthday"));
			user.setAge((Integer)map.get("age"));
			user.setMarriage((Boolean)map.get("marriage"));
			user.setRole((String)map.get("role"));

//			結果返却用のListにuser(入力データが格納された1人分のオブジェクト）を追加
			userList.add(user);
		}

		return userList;

	}

//	===================
//	updateOne()メソッド
//	===================
//	User(table)を1件更新
	@Override
	public int updateOne(User user) throws DataAccessException{

//		JdbcTemplateのupdateメソッドを使用
//		引数はSQL文と、PreparedStatementの値
		int rowNumber = jdbc.update("UPDATE m_user SET password = ?, user_name = ?, birthday = ?, age =?, marriage = ? WHERE user_id = ?",
				user.getPassword(), user.getUserName(), user.getBirthday(), user.getAge(), user.isMarriage(), user.getUserId());

		return rowNumber;

	}

//	===================
//	deleteOne()メソッド
//	===================
//	User(table)を1件削除
	@Override
	public int deleteOne(String userId) throws DataAccessException {

//		引数のuserIdのデータを削除し、この件数を返す
		int rowNumber = jdbc.update("DELETE FROM m_user WHERE user_id = ?",userId);

		return rowNumber;
	}

//	===================
//	userCsvOut()メソッド
//	===================
//	User(table)の全データをcsvに出力する
//	SQL取得結果をサーバーにcsvで保存する
	@Override
	public void userCsvOut() throws DataAccessException{

//		全ユーザー情報を取得するsqlを格納
		String sql = "SELECT * FROM m_user";

//		UserRowCallbackHandlerオブジェクトを生成
//		 ∟ ユーザー一覧画面からCSV出力機能を持ったオブジェクト
		UserRowCallbackHandler handler = new UserRowCallbackHandler();

//		SQL実行&CSV出力実行
		jdbc.query(sql, handler);

	}



}




