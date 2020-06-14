package com.example.demo.service;

import java.util.List;

import com.example.demo.entity.Inquiry;

public interface InquiryService {
	
	//DBにへの登録
	void save(Inquiry inquiry);
	
	void update(Inquiry inquiry);
	
	//ListのInquiryを返す
	List<Inquiry> getAll();

}
