package com.example.demo;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// このクラスの役割
// ・セキュリティ用
// ・直リンクを禁止させる（直接アクセスしようとすると403エラー（閲覧禁止）になる）
// ・※ webjarsやcssなどの静的リソースはセキュリティの対象とする
//     ∟ login画面とユーザー登録画面（=ログインしてなくても表示可）はwebjarsやcssを有効にしたいから。


// @EnableWebSequrity：セキュリティ設定用クラスにつけるアノテーション
// ・WebSecurityConfigurerAdapterクラスを継承する
//  ∟ このクラスの各メソッドをオーバーライドすることで、セキュリティの設定を行っていくことができる
// ・セキュリティ用にBean定義を行うこともあるので@Configurationアノテーションをつける。
@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	PasswordEncoder：パスワードを暗号化したり、復号するインターフェース
//	PasswordEncoderを実装したBCryptPasswordEncoderのインスタンスを返すBean定義を行っている。
//	 ∟ ユーザー登録のリポジトリークラス（DB直触り）などで使う(= Autowiredする）から。
//	※ SpringBootのver.2以降パスワードの暗号化が必須
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	SQLを実行できるようにするためにAutowiredする。
//	= SpringがBeanを自動で用意
	@Autowired
	private DataSource dataSource;



//	==============
//	直リンクの禁止
//	==============

//	== ▼ ソースへのアクセスに関する設定 ▼ ==
	@Override
	public void configure(WebSecurity web) throws Exception{

//		静的リソースへのアクセスには、セキュリティを適用しない。
//		**（アスタリスク2つ）：正規表現で「いずれかのファイル」という意味。
//		 ∟ /webjars配下と/css配下のファイルはセキュリティの対象外
		web.ignoring().antMatchers("/webjars/**","/css/**");
	}

//	== ▼ 直リンクに関する設定 ▼ ==
	@Override
	protected void configure(HttpSecurity http) throws Exception{

//		ログイン不要ページの設定
//		authorizeRequests()メソッド：メソッドチェーンで直リンク禁止先の条件を追加する
//		メソッドチェーンは上から順番に設定がされていく。
//		 ∟ anyRequest.authenticated()を一番上に設定すると、全て認証が必要になるから順番注意。
		http.authorizeRequests()

//		antMatchers("リンク先").permitAll()：リンク先は直リンクOK
		.antMatchers("webjars/**").permitAll()  //webjarsへのアクセス許可
		.antMatchers("css/**").permitAll()  //cssへのアクセス許可
		.antMatchers("/login").permitAll()  //ログインページは直リンクOK
		.antMatchers("/signup").permitAll()  //ユーザー登録画面は直リンクOK

//		antMatchers("パス")：アクセス制限するパスを設定（正規表現OK）
//		hasAuthority("ロール")：有効にするロール（データベースから取得してきたロール名で）を指定
//		Springでは、ロール名の先端に"ROLE_"をつけるというルールがある。
//		データベースにロール名を登録するときは、"ROLE_"をつけるようにする！
//		 ※ メソッドによっては"ROLE_"を省略して引数にセットするものがあるので注意が必要
		.antMatchers("/admin").hasAuthority("ROLE_ADMIN")

//		anyRequest()：全てのリンク先
//		authenticated()：認証しないとアクセスできないように設定
		.anyRequest().authenticated();  //それ以外は直リンク禁止


//	=================
//	ログイン処理
//	=================

//		http.formLogin()メソッド：ログイン処理用のメソッド。チェーンメソッドで条件を指定。
//		loginProcessingUrl("リンク先")：ログイン処理をするURLを指定。
//		ログイン画面のhtmlにあるフォームタグのaction="/login"の部分と一致させる
		http.formLogin().loginProcessingUrl("/login")

//		loginPage("リンク先")：ログインページのリンク先を設定。
//		指定しない場合、自分で用意したログインページでなく、Springセキュリティのデフォルトログインページが表示される
//		ログイン画面のコントローラークラスにある @GetMapping("/login") の部分に一致させる。
		.loginPage("/login")

//		failureUrl("リンク先")：ログインが失敗した場合の遷移先
		.failureUrl("/login")

//		~Parameter(”パラメーター名)：ログインページのユーザーID入力エリアのパラメーター名を指定
//		指定するパラメーター名はhtml（今回はlogin.html）のname属性から探す。例：name = "userId"
		.usernameParameter("userId")
		.passwordParameter("password")

//		ログイン成功後の遷移先
		.defaultSuccessUrl("/home",true);



//	=================
//	ログアウト処理
//	=================

	http.logout()
//	logoutRequestMatcher()：ログアウト処理をGETメソッドで送る場合（デフォルトはPOST）
//	↓ 参考まで。
//	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))

//	logoutUrl("パス"):POSTメソッドでログアウトする場合の設定
	.logoutUrl("/logout")

//	ログアウト成功時の遷移先を指定
	.logoutSuccessUrl("/login");



//	=================
//	CSRF対策
//	=================

//	CSRF対策を無効に設定（一時的）
//	http.csrf().disable();
}


//	===================
//	ユーザーデータの取得
//	===================

//		ユーザーIDとパスワードと使用可否（Enable。本サンプルは全てtrue）を取得するSQL文
		private static final String USER_SQL = "SELECT user_id, password, true FROM m_user WHERE user_id = ?";

//		ユーザーのロール（管理者か一般か）を取得するSQL文
		private static final String ROLE_SQL = "SELECT user_id, role FROM m_user WHERE user_id = ?";

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception{

			auth.jdbcAuthentication()
			.dataSource(dataSource)

//			usersByUsernameQuery()メソッド：ユーザーID、パスワード、ENABLEDを検索するクエリを定義する
//			 ∟ ↓入力されたユーザーID、パスワードがDBの値と一致しているかを確認
			.usersByUsernameQuery(USER_SQL)
//
//			↓ユーザー名でAuthority（権限）を検索する
			.authoritiesByUsernameQuery(ROLE_SQL)
//			※このやり方ができるのは、ユーザーIDとパスワードの入力エリアをログイン処理で定義しているから。

//			ログイン時のパスワード復号のため、passwordEncoder()メソッドにBean定義したPasswordEncoderをセット。
//			 ∟ これで、ログイン時にパスワードをSpringが複合してくれる
			.passwordEncoder(passwordEncoder());
		}







}
