package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Survey;

public interface SurveyService {
	
	//DBへの登録処理
	void save(Survey survey);
	
	//ListのSurveyを返す
	List<Survey> getAll();

}
