package com.example.demo.login.domain.model;

import javax.validation.GroupSequence;

// @GroupSequence：バリデーションの実行順序を設定するインターフェースに付ける
// アノテーションのパラメーターに、各フループの.classを指定。左から順に実行される。
@GroupSequence({ValidGroup1.class,ValidGroup2.class,ValidGroup3.class})
public interface GroupOrder {

}
