package com.project.letterOfHeart.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.letterOfHeart.service.UserService;

@RestController
@RequestMapping("/sign")
public class SmsController {
	
	@Autowired
	UserService userService;
	
	@Autowired
    HttpSession session;

	//전화번호 중복 체크 및 인증번호 전송
    @PostMapping("/numbercheck")
    public Map<String, Object> numberCheck(@RequestParam("phone") String phone){
        boolean result = userService.phoneNumberCheck(phone.trim());
        Map<String, Object> map = new HashMap<>();
        if(result==true) {
            map.put("result", -1);
            map.put("message", "이미 사용 중인 전화번호입니다.");
            return map;
        }
        //전송할 인증번호
        int random = 0;
        while(random==0){
            random = (int)(Math.random()*10000); //4자리 인증키
        }
        result = userService.sendSms(phone.trim(), random);
        
        if(result==true) {
            map.put("result", 1);
            map.put("message", "인증번호가 발송되었습니다.");
            map.put("random", random);
            session.setAttribute("random", random);
            return map;
        }else{
            map.put("result", -1);
            map.put("message", "잠시후 다시 시도해주세요.");
            return map;
        }
    }
}
