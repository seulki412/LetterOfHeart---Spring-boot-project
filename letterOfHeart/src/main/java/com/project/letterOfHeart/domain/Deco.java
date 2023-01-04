package com.project.letterOfHeart.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@SequenceGenerator(
		name = "DECO_SEQ_GENERATOR"
	    , sequenceName = "DECO_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class Deco {

	@Id @GeneratedValue(
    		strategy = GenerationType.SEQUENCE,
    		generator = "DECO_SEQ_GENERATOR")
	@Column(name = "DECO_ID")
	private Long id;	
	
//	@Column(name = "MESSAGE_ID")
//	private Long massageid ; 	// 메세지 번호
	private Long ornamentType ; // 트리 장식 종류
	private Long decoOption1 ; 	// 꾸미기1
	private Long decoOption2 ; 	// 꾸미기2
	
	@ManyToOne
	@JoinColumn(name = "MESSAGE_ID")
	private Message message;
	
}