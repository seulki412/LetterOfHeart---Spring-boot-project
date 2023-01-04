package com.project.letterOfHeart.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class UsersRolesForm {

	@NotEmpty(message = "회원 아이디는 필수입니다.")
	private String accountId;
	private String password;
}
