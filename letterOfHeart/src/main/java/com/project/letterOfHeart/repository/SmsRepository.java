package com.project.letterOfHeart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.letterOfHeart.domain.Users;

@Repository
public interface SmsRepository extends JpaRepository<Users, Long> {

	Users findByPhone(String phone);
}
