package com.project.letterOfHeart.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MessageForm {
	private Long id; 	// 유저 PK
	
	@NotBlank(message 	= "닉네임은 필수입니다.")
	@Size(min = 2, max = 8, message = "닉네임은 2~8자 사이로 입력해주세요.")
    private String titleNickname;
	
	@NotBlank(message 	= "편지 내용은 필수입니다.")
	@Size(min = 2, max = 850, message = "내용을 5~500자 사이로 입력해주세요.")
    private String content;

}