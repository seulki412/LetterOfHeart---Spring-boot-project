package com.project.letterOfHeart.service;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.dto.UsersForm;
import com.project.letterOfHeart.repository.UsersRepository;
import com.project.letterOfHeart.repository.repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {

	private final repository repository;
	private final UsersRepository usersRepository;
	
	// 회원가입
	@Transactional
	public Long join(Users users) {
		usersRepository.save(users);
		return users.getId();
	}
	
	// 로그인
	public Users login(String accountId, String password) {
		
		Users users = usersRepository.findByAccountsId(accountId);
		if(users != null && users.getUsername().equals(accountId)
				&& users.getPassword().equals(password)) {
			// 로그인 성공
			return users;
		}else {
			return null;	
		}
		
	}
	
	//  1건 조회
	public Users findOne(Long id) {
		return usersRepository.findOne(id);
	}
	// 로그인 id 조회
	public Users findById(String accountId) {
		return usersRepository.findByAccountId(accountId);
	}

	/* 회원가입 시, 유효성 체크 */
    @Transactional(readOnly = true)
    public Map<String, String> validateHandler(Errors errors) {
        Map<String, String> validateResult = new HashMap<>();

        for (FieldError error : errors.getFieldErrors()) {
            String validKeyName = "valid_" + error.getField();
            validateResult.put(validKeyName, error.getDefaultMessage());
        }


        return validateResult;
    }

    
    /* 회원가입 시, 아이디 중복 체크 */
	public Users join(String accountId) {
		
		Users users = usersRepository.findByAccountsId(accountId);
		if(users == null ) {
			System.out.println("검증성공===================ㄴ");
			// 아이디 체크 성공
			return null;
		}
		System.out.println("검증실패===================ㄴ");
		return users;
	}

	public Long findUserIdUserUrlrnd(String urlrnd) {
		
		return usersRepository.findUserIdUserUrlrnd( urlrnd );
	}
	
}
