package com.example.demo.login.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

// このクラスで行うこと
// ・各コントローラークラス（LoginController、SignupController）のメソッドが呼び出されるたびに、開始ログと終了ログを出力する。

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


}
