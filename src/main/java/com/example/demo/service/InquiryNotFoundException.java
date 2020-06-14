package com.example.demo.service;

public class InquiryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	//ここで作成したExceptionはビジネスロジック上でスローする
	public InquiryNotFoundException (String message) {
		super(message);
	}

}
