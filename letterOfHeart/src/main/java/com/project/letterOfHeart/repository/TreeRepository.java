package com.project.letterOfHeart.repository;



import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.project.letterOfHeart.domain.Tree;

import lombok.RequiredArgsConstructor;


@Repository
@RequiredArgsConstructor
public class TreeRepository {

	private final EntityManager em;

	public void save(Tree tree) {
		em.persist(tree);
	}

	// 트리 1건 조회
	public Tree findOne(Long id) {
		return em.find(Tree.class,id);
	}



	
}
