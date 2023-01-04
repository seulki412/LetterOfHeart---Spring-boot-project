package com.project.letterOfHeart.service;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.project.letterOfHeart.domain.Tree;
import com.project.letterOfHeart.repository.TreeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TreeService {

	private final TreeRepository treeRepository;
	
	@Transactional
	public void save(Tree tree) {
		treeRepository.save(tree);
	}
	

	//  1건 조회
	@Transactional(readOnly = true)
	public Tree findOne(Long id) {
		return treeRepository.findOne(id);
	}
	
	// 메세지 작성 후, 트리테이블에 메세지 카운트 증가
	@Transactional
	public void updateTree(Long id, int count) {
		Tree tree = treeRepository.findOne(id);
		tree.setMessageCnt(count);
		
	}
	
	@Transactional
	public void editDesign(Long id, int imgNum) {
		Tree tree = treeRepository.findOne(id) ;
		tree.setTreeDesign(imgNum) ;
	}
}
