package com.example.demo.config;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.example.demo.service.InquiryNotFoundException;


/**
 * 全てのControllerで共通処理を定義
 */
@ControllerAdvice
public class WebMvcControllerAdvice {

	/*
	 * This method changes empty character to null
	 * こちらのメソッドを用意しておくと送信された空文字はnullに変換されます
	 */
    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }
    
    //エラー名(例外の名称)を引数に記載する必要がある
    //引数は（handleException内）、エラーのメッセージを受け取るために使用
    @ExceptionHandler(EmptyResultDataAccessException.class)
    public String handleException(EmptyResultDataAccessException e, Model model) {
    	//e.toString()が使用されている
    	model.addAttribute("message", e);
    	return "error/CustomPage";
    }
    
    //例外の処理をWebMvcControllerAdviceに記載すると、全てのコントローラーに例外処理が対応する
	@ExceptionHandler(InquiryNotFoundException.class)
	public String handleException(InquiryNotFoundException e, Model model) {
		model.addAttribute("message", e);
		return "error/CustomPage";
	}
}