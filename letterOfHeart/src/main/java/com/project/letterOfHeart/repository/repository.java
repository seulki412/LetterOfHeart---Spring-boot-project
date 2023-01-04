package com.project.letterOfHeart.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.letterOfHeart.domain.Users;

@Repository
public interface repository extends JpaRepository<Users, Long>{
	Optional<Users> findByAccountId(String accountId);
}
