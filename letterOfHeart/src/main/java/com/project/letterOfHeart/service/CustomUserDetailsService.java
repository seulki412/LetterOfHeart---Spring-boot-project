package com.project.letterOfHeart.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.repository.UsersRepository;
import com.project.letterOfHeart.repository.repository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
 
	private final repository repository;
	private final UsersService usersService;
    private final PasswordEncoder passwordEncoder;
 
    @Override
    public UserDetails loadUserByUsername(String accountId) throws UsernameNotFoundException {
    	return repository.findByAccountId(accountId)
                .map(this::createUserDetails)
                .orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));
    	
    }
 
    // 해당하는 User 의 데이터가 존재한다면 UserDetails 객체로 만들어서 리턴
    private UserDetails createUserDetails(Users user) {
        return User.builder()
                .username(user.getAccountId())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRole())
                .build();
    }
}
