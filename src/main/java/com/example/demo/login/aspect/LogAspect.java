package com.example.demo.login.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

//	このクラスの役割
//	・各コントローラークラス（LoginController、SignupController）のメソッドが呼び出されるたびに、開始ログと終了ログを出力する。
//	・UserDaoクラス（CRUD操作）のメソッドが呼び出された時に、どのクラスのどのメソッドが呼ばれたのかをログ出力

// ↓2つセットで付ける
// @Aspect：AOPのクラスに付ける
@Aspect
// @Component：DIコンテナへBean定義
@Component
public class LogAspect {

//	@Before：メソッドが実行される前にAOPの処理を実行
//	パラメーター
//	・どのクラスのどのメソッドが実行された時にこのメソッドが呼び出されるかを指定
//	・execution：正規表現を使って任意のクラス、メソッドなどを指定（今回はこれ）。※アノテーション名で指定することなども可
//	・*：任意の文字列
//	・..（ドット2つ）：パッケージでは任意（0以上）のパッケージ、メソッドでは任意（0以上）の引数を表す。
//	・基本："excecution(<戻り値> <パッケージ名>.<クラス名>.<メソッド名>(<引数>))"
//	・今回： "excecution(<*> 『<*..*>(=全てのパッケージ)』.『<*Controller>(=末尾にControllerと付くクラス)』.『<*>(<..>)(=全てのメソッド、全ての引数が対象)』)"
//	・ ∟ クラス名の最後にControllerが付くクラスの全てのメソッドをAOPの対象にしている
//	・戻り値（最初の*）の後半角スペースを入れて、以降はスペースなしで続けて書く
	@Before("execution(* *..*.*Controller.*(..))")
	public void startLog(JoinPoint jp) {
//		getSignature()メソッド：ログ出力
		System.out.println("メソッド開始:" + jp.getSignature());

	}

//	@After：メソッドが実行された後にAOPの処理を実行
//	パラメーター
//	・どのクラスのどのメソッドが実行された時にこのメソッドが呼び出されるかを指定
//	・execution：正規表現を使って任意のクラス、メソッドなどを指定（今回はこれ）。※アノテーション名で指定することなども可
//	・*：任意の文字列。パッケージでは1階層のパッケージ名。
//	・..（ドット2つ）：パッケージでは任意（0以上）のパッケージ、メソッドでは任意（0以上）の引数を表す。
//	・基本："excecution(<戻り値> <パッケージ名>.<クラス名>.<メソッド名>(<引数>))"
//	・今回： "excecution(<*> 『<*..*>(=全てのパッケージ)』.『<*Controller>(=末尾にControllerと付くクラス)』.『<*>(<..>)(=全てのメソッド、全ての引数が対象)』)"
//	・ ∟ クラス名の最後にControllerが付くクラスの全てのメソッドをAOPの対象にしている
//  ・戻り値（最初の*）の後半角スペースを入れて、以降はスペースなしで続けて書く
	@After("execution(* *..*.*Controller.*(..))")
//	JoinPoint：処理を実行するタイミングが入ったオブジェクト
	public void endLog(JoinPoint jp) {
//		getSignature()メソッド：ログ出力
		System.out.println("メソッド終了:" + jp.getSignature());
	}

//	UserDaoクラスのログ出力
//	@Around：メソッドの実行前後にAOPの処理を実行
	@Around("execution(* *..*.*UserDao*.*(..))")
//	Aroundの場合は、ProceedingJoinPointを利用して、対象のメソッドの前後に処理を差し込む
	public Object daoLog(ProceedingJoinPoint jp)throws Throwable{

//		開始のログ出力
		System.out.println("メソッド開始:" + jp.getSignature());

//		Aroundの場合、アノテーションをつけたメソッド内で、AOP対象クラス(UserDao実装クラス）のメソッドを直接実行
//		→ メソッド実行の前後で任意の処理をすることができる。
//		※ メソッドを直接実行しているため、returnには実行結果の戻り値を指定
		try {
//			メソッド実行（proceed()メソッド）
			Object result = jp.proceed();

//			終了のログ出力
			System.out.println("メソッド終了:" + jp.getSignature());

//			該当メソッドの実行結果を返す
			return result;

		} catch (Exception e) {
//			エラーがあれば
//			ログ出力
			System.out.println("メソッド異常終了:" + jp.getSignature());

//			エラー内容を出力
			e.printStackTrace();
			throw e;
		}

	}



}
