package com.project.letterOfHeart.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.letterOfHeart.domain.Message;
import com.project.letterOfHeart.repository.MessageRepository;
import com.project.letterOfHeart.repository.PageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PageService {
	private final PageRepository pageRepository;
	
	@Transactional(readOnly = true)
	public Page<Message> pageList(Pageable pageable,Long id){
		return pageRepository.findAllByTreeId(id, pageable);
	}
}
