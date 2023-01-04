package com.project.letterOfHeart.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import com.project.letterOfHeart.domain.Message;
import com.project.letterOfHeart.domain.Tree;
import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.dto.MessageForm;
import com.project.letterOfHeart.dto.UsersForm;
import com.project.letterOfHeart.service.MessageService;
import com.project.letterOfHeart.service.TreeService;
import com.project.letterOfHeart.service.UsersService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor  
public class MessageController {

	private final MessageService messageService;
	private final UsersService userService;
	private final TreeService treeService;

	@GetMapping("/message/write")
	public String messageForm( Model model, MessageForm messageForm) {
		model.addAttribute("messageForm", messageForm);
		return "redirect:/myTree/"+ messageForm.getId();
	}
	
	
	@PostMapping("/message/write")
	public String boardWritePro(@Valid MessageForm messageForm ,Model model, Errors errors) {
		Message message = new Message();
		// 유저 아이디 1건 조회
		Users user = userService.findOne(messageForm.getId());
		// 트리 아이디 1건 조회
		Tree tree = treeService.findOne(messageForm.getId());
		
		
		
		if (errors.hasErrors()) {
			/*  실패시 입력 데이터 유지 */
			model.addAttribute("messageForm", messageForm);
			/* 실패시 message 값들을 모델에 매핑해서 View로 전달 */
			Map<String, String> validateResult = messageService.validateHandler(errors);
			// map.keySet() -> 모든 key값을 갖고온다.
			// 그 갖고온 키로 반복문을 통해 키와 에러 메세지로 매핑
			for (String key : validateResult.keySet()) {
				// ex) model.addAtrribute("valid_id", "아이디는 필수 입력사항 입니다.")
				model.addAttribute(key, validateResult.get(key));
			}
			
			return "/redirect:/myTree/"+messageForm.getId();
		}

		LocalDateTime opendate = LocalDateTime.parse("2022-12-25T00:00:00.000");
		message.setTitleNickname(messageForm.getTitleNickname());
		message.setContent(messageForm.getContent());
		// 날짜 비교 조건은 프론트단에서?? 둘다?
		message.setOpenDate(opendate);
		message.setSendDate(LocalDateTime.now());
		message.setStatus(0L);
		//user id insert
		message.setUsers(user);
		// tree id insert
		message.setTree(tree);
		messageService.wirte(message);
		
		
		int messageCnt = messageService.findAllById(messageForm.getId());
		treeService.updateTree(messageForm.getId(), messageCnt);
		
		//return "redirect:/myTree/"+messageForm.getId();
		return "redirect:/myTree/"+user.getUrlrnd();
	}

}

