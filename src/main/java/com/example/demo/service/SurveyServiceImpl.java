package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.SurveyDao;
import com.example.demo.entity.Survey;

@Service
public class SurveyServiceImpl implements SurveyService {
	
	//まずは先程作成したDaoを読み込んでおく
	//注意：インターフェース名にしておく必要がある。
	private final SurveyDao dao;
	
	//デフォルトコンストラクタの用意
	//これでDaoを使用できるようになる
	@Autowired
	SurveyServiceImpl(SurveyDao dao) {
		this.dao = dao;
	}

	@Override
	public void save(Survey survey) {
		dao.insertSurvey(survey);
	}

	@Override
	public List<Survey> getAll() {
		return dao.getAll();
	}

}
