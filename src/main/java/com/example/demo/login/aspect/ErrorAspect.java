package com.example.demo.login.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

//DataAccessException発生時にログを出力
@Aspect
@Component
public class ErrorAspect {
//	引数は全てのコントローラー、サービス、リポジトリーを対象にしてる
	@AfterThrowing(value = "execution( * *..*..*(..))"+"&& (bean( * Controller) || bean( * Service) || bean( * Repository)) ", throwing = "ex")
	public void throwingNull(DataAccessException ex) {

		System.out.println("=======================");
		System.out.println("DataAccessExceptionが発生しました。:"+ex);
		System.out.println("=======================");


	}

}
