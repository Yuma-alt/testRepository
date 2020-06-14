package com.example.demo.dao;

import java.util.List;

import com.example.demo.entity.Inquiry;

public interface InquiryDao {
	
	void insertInquiry(Inquiry inquiry);
	
	//返り値、反応した件数が返ってくるようにする
	int updateInquiry(Inquiry inquiry);
	
	List<Inquiry> getAll();

}
