package com.project.letterOfHeart.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.letterOfHeart.domain.Message;
@Repository
public interface PageRepository extends JpaRepository<Message, Long>{
	// 페이징 처리 
	Page<Message> findAllByTreeId(Long id, Pageable pageable);

}
