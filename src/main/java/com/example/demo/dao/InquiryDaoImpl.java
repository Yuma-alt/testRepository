package com.example.demo.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Inquiry;

@Repository
public class InquiryDaoImpl implements InquiryDao {
	
	//データベース操作用のクラス
	private final JdbcTemplate jdbcTemplate;
	
	//これでデータベースの操作ができるようになる(操作毎に初期化する)
	@Autowired
	public InquiryDaoImpl (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	//プリペアードステートメントになっているので、SQLインジェクションを防げる状態になっている
	@Override
	public void insertInquiry(Inquiry inquiry) {
		jdbcTemplate.update("INSERT INTO inquiry(name, email, contents, created) VALUES(?, ?, ?, ?)",
				inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getCreated());
	}

	@Override
	public List<Inquiry> getAll() {
		String sql = "SELECT id, name, email, contents, created FROM inquiry";
		//Listの中にMapが入ってくる形になる
		List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
		//次にInquiryに移し替えていく作業(Mapの中身をinquiryに詰め直していく作業)
		List<Inquiry> list = new ArrayList<Inquiry>();
		//resultListの中からMapを繰り返し取り出していく作業
		for(Map<String, Object> result : resultList) {
			//インスタンス化したInquiryにデータを詰めていく
			Inquiry inquiry = new Inquiry();
			inquiry.setId((int)result.get("id"));
			inquiry.setName((String)result.get("name"));
			inquiry.setEmail((String)result.get("email"));
			inquiry.setContents((String)result.get("contents"));
			inquiry.setCreated(((Timestamp)result.get("created")).toLocalDateTime());
			list.add(inquiry);
		}
		return list;
	}

	@Override
	public int updateInquiry(Inquiry inquiry) {
		return jdbcTemplate.update("UPDATE inquiry SET name = ?, email = ?, contents = ? WHERE id = ?",
				inquiry.getName(), inquiry.getEmail(), inquiry.getContents(), inquiry.getId());
	}
}
