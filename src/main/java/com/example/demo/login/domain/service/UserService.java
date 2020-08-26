package com.example.demo.login.domain.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.login.domain.repository.UserDao;

//このクラスの役割
//・サービスクラス（リポジトリークラスなどを使った色々なサービスを提供）

@Service
public class UserService {

	@Autowired
	UserDao dao;

}
