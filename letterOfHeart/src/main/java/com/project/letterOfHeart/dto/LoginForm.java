package com.project.letterOfHeart.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class LoginForm {
	@NotBlank(message = "* 아이디를 입력해주세요.")
	private String accountId;

	@NotBlank(message = "* 비밀번호를 입력해주세요.")
	private String password;
}
