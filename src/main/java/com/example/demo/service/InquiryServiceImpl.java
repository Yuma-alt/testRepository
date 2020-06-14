package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dao.InquiryDao;
import com.example.demo.entity.Inquiry;

@Service
public class InquiryServiceImpl implements InquiryService {

	//先程作成したDaoクラスを読み込んでいく
	//注意：インターフェイス名にしておくことが重要
	private final InquiryDao dao;
	
	//デフォルトコンストラクタの用意
	//これでDaoを使用することができるようになる
	@Autowired InquiryServiceImpl(InquiryDao dao) {
		this.dao = dao;
	}
	
	@Override
	public void save(Inquiry inquiry) {
		dao.insertInquiry(inquiry);
	}

	@Override
	public List<Inquiry> getAll() {
		return dao.getAll();
	}

	@Override
	public void update(Inquiry inquiry) {
		if(dao.updateInquiry(inquiry) == 0) {
			throw new InquiryNotFoundException("can't find the same ID");
		}
		
	}

}
