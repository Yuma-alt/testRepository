package com.example.demo.app.inquiry;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Inquiry;
import com.example.demo.service.InquiryNotFoundException;
import com.example.demo.service.InquiryService;

@Controller
@RequestMapping("/inquiry")
public class InquiryController {
	
	//型の名前はインターフェース名をつける
	//初期化する時はインターフェース名をつけるようだ。
	private final InquiryService inquiryService;
	
	//デフォルトコンストラクタを使用して、上記のInquiryServiceを読み込んでいく
	@Autowired
	public InquiryController(InquiryService inquiryService) {
		this.inquiryService = inquiryService;
	}
	
	@GetMapping
	public String index(Model model) {
		List<Inquiry> list = inquiryService.getAll();
		
		//Formから値が来たと想定する
//		Inquiry inquiry = new Inquiry();
//		inquiry.setId(4);
//		inquiry.setName("Jamie");
//		inquiry.setEmail("sample4@gmail.com");
//		inquiry.setContents("contents");
//		
//		inquiryService.update(inquiry);
//		
//		try {
//			inquiryService.update(inquiry);
//		} catch (InquiryNotFoundException e) {
//			model.addAttribute("message", e);
//			return "error/CustomPage";
//		}
		
		model.addAttribute("inquiryList", list);
		model.addAttribute("title", "Inquiry Index");
		return "inquiry/index_boot";
	}
	
	@GetMapping("/form")
	public String form(InquiryForm inquiryForm,
			Model model,
			//フラッシュスコープを受け取るための処理を記述している。
			//これでHTMLに値をレンダリングできる
			@ModelAttribute("Complete") String complete) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form_boot";
	}
	
	@PostMapping("/form")
	public String formGoBack(InquiryForm inquiryForm, Model model) {
		model.addAttribute("title", "Inquiry Form");
		return "inquiry/form_boot";
	}
	
	//遷移後のURLで、バリデーションをかけている（@Validated）
	@PostMapping("/confirm")
	public String confirm(@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model) {
		//BindingResultはバリデーションをかけた後の結果が帰ってくる
		//バリデーションに引っかかったらtrueになる
		if(result.hasErrors()) {
			model.addAttribute("title", "Inquiry Form");
			return "inquiry/form_boot";
		}
		model.addAttribute("title", "Confirm Page");
		return "inquiry/confirm_boot";
	}
	
	//ページ間を超えてデータを保持しておく仕組みのことをセッションという
	//このセッションをしようする為にフラッシュスコープを使用する必要がある・・・RedirectAttributes
	
	@PostMapping("/complete")
	public String complete(@Validated InquiryForm inquiryForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "Confirm Page");
			return "inquiry/form_boot";
		}
		
		//InquiryFormからentityクラスにデータを詰め替える必要がある。
		Inquiry inquiry = new Inquiry();
		inquiry.setName(inquiryForm.getName());
		inquiry.setEmail(inquiryForm.getEmail());
		inquiry.setContents(inquiryForm.getContents());
		inquiry.setCreated(LocalDateTime.now());
		
		//Serviceへ情報を流す。
		inquiryService.save(inquiry);
		
		//これでDBに登録された後にリダイレクトされ、また元のページに戻る流れになる。
		
		//下記「Registerd!」が表示されると、その時にセッションのデータが破棄させる。
		//これをフラッシュスコープといい、addFrashAttributeを使用することで実現できる。
		redirectAttributes.addFlashAttribute("complete", "Registerd!");
		return "redirect:/inquiry/form";
		
//		フラッシュスコープとは、リダイレクト時に一度だけ値を保持して表示することができる。
	}

	//コントローラー内で例外を補足するやり方
//	@ExceptionHandler(InquiryNotFoundException.class)
//	public String handleException(InquiryNotFoundException e, Model model) {
//		model.addAttribute("message", e);
//		return "error/CustomPage";
//	}
	
	//例外の処理をWebMvcControllerAdviceに記載すると、全てのコントローラーに例外処理が対応する

}
