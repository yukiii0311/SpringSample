package com.example.demo.login.domain.model;

import java.util.Date;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

// Lombok対応
@Data
public class SignupForm {

//	===== 全項目共通（groups属性) =====
//  groups属性でインターフェースのクラスを指定してフィールドとバリデーショングループの紐付け
//	 ∟ バリデーションの実行順序を指定している


//	フィールド
	@NotBlank(groups = ValidGroup1.class)  // 必須入力（Nullでない、空文字でない、空白スペースのみでない）
	@Email(groups = ValidGroup2.class)  // メールアドレス形式
	private String userId;

	@NotBlank(groups = ValidGroup1.class)  // 必須入力（Nullでない、空文字でない、空白スペースのみでない）
	@Length(min = 4, max = 100, groups = ValidGroup2.class)  // 長さ4〜100桁まで
	@Pattern(regexp = "^[a-zA-Z0-9]+$",groups = ValidGroup3.class)  // 半角英数字のみ
	private String password;

	@NotBlank(groups = ValidGroup1.class)  // 必須入力（Nullでない、空文字でない、空白スペースのみでない）
	private String userName;

	@NotNull(groups = ValidGroup1.class)  // 必須入力（Nullでない）
//	@DataTimeFormat：画面から渡されてきた文字列を日付型に変換
//	pattern属性でどんなフォーマットでデータが渡されてくるか指定する
	@DateTimeFormat(pattern = "yyyy/MM/dd")
	private Date birthday;

	// 値が20〜100まで
	@Min(value = 20, groups = ValidGroup2.class)
	@Max(value = 100, groups = ValidGroup2.class)
	private int age;

	@AssertFalse(groups = ValidGroup2.class)  // falseのみ可能（falseかどうかをチェック）
	private boolean marriage;




}
