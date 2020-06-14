package com.example.demo.app.survey;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.entity.Survey;
import com.example.demo.service.SurveyService;

@Controller
@RequestMapping("/survey")
public class SurveyController {
	
	//型の名前はインターフェース名をつける
	//初期化する時はインターフェース名をつけるようだ。
	private final SurveyService surveyService;
	
	//デフォルトコンストラクタを使用してServiceを読み込む(上記のSurveyServiceを読み込む)
	@Autowired
	public SurveyController(SurveyService surveyService) {
		this.surveyService = surveyService;
	}
	
	@GetMapping
	public String index(Model model) {
		List<Survey> list = surveyService.getAll();
		
		//大文字・小文字注意
		model.addAttribute("surveyList", list);
		model.addAttribute("title", "Survey Index");
		return "survey/index";
	}
	
	@GetMapping("/form")
	public String form(SurveyForm surveyForm, 
			Model model,
			@ModelAttribute("Complete") String complete) {
		model.addAttribute("title", "Survey Form");
		
		return "survey/form";
	}
	
	@PostMapping("/form")
	public String formGoBack(SurveyForm surveyForm, Model model) {
		model.addAttribute("title", "Survey Form");
		return "survey/form";
	}
	
	@PostMapping("/confirm")
	public String confirm(@Validated SurveyForm surveyForm,
			BindingResult result,
			Model model) {
		model.addAttribute("title", "Survey Form");
		
		if(result.hasErrors()) {
			model.addAttribute("title", "Survey Form");
			return "survey/form";
		}
		model.addAttribute("title", "Confirm Page");
		return "survey/confirm";
	}
	
	@PostMapping("/complete")
	public String complete(@Validated SurveyForm surveyForm,
			BindingResult result,
			Model model,
			RedirectAttributes redirectAttributes) {
		if(result.hasErrors()) {
			model.addAttribute("title", "Confirm Page");
			return "survey/form";
		}
		
		//SurveyFormからentityクラスにデータを詰め替える
		Survey survey = new Survey();
		survey.setAge(surveyForm.getAge());
		survey.setSatisfaction(surveyForm.getSatisfaction());
		survey.setComment(surveyForm.getComment());
		survey.setCreated(LocalDateTime.now());
		
		//entityへ保存した情報をServiceへ流す
		surveyService.save(survey);
		
		//下記「Registerd!」が表示されると、その時にセッションのデータが破棄させる。
		//これをフラッシュスコープといい、addFrashAttributeを使用することで実現できる。
		redirectAttributes.addFlashAttribute("complete", "Registerd!");
		return "redirect:/survey/form";
		
//		フラッシュスコープとは、リダイレクト時に一度だけ値を保持して表示することができる。
	}

}
