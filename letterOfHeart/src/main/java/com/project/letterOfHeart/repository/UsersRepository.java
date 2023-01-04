package com.project.letterOfHeart.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.project.letterOfHeart.domain.Users;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UsersRepository {

	@Autowired
	private final EntityManager em;
	
	// 저장
	public void save(Users users) {
		em.persist(users);
	}
	
	// 1건 조회
	public Users findOne(Long id) {		
		return em.find(Users.class, id);
	}
	
	
	// 여러건 조회
	public List<Users> findAll(){
		return em.createQuery("select u from Users u", Users.class).getResultList();
	}
	
	// 아이디로 조회 
	public List<Users> findById(String accountId){
		return em.createQuery("select u from Users u where u.accountId = :accountId", Users.class)
				 .setParameter("accountId", accountId).getResultList();
	}
	
	public Users findByAccountId(String accountId) {
		return em.find(Users.class, accountId);
	}
	
	// 로그인 아이디 체크
	public Users findByAccountsId(String accountId){
		List<Users> all = findAll();
		
		for(Users u : all) {
			if(u.getAccountId().equals(accountId)) {
				return u;
			}
		}
		
		return null;
	}

	public Long findUserIdUserUrlrnd(String urlrnd) {
		return em.createQuery("select u from Users u where u.urlrnd = :urlrnd",Users.class).setParameter("urlrnd", urlrnd).getSingleResult().getId() ;
	}
	
}
