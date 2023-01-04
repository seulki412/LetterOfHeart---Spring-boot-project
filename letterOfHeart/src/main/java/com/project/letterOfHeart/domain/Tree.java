package com.project.letterOfHeart.domain;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter @Setter
@SequenceGenerator(
		name = "TREE_SEQ_GENERATOR"
	    , sequenceName = "TREE_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class Tree {

    @Id @GeneratedValue(
    		strategy = GenerationType.SEQUENCE,
    		generator = "TREE_SEQ_GENERATOR")
	@Column(name = "TREE_ID")
    private Long id;

//    @Column(name = "USER_ID")
//    private String userid;        // 유저 아이디
    
    private int messageCnt;    	// 메세지 개수
    
    
    private int treeDesign;	// 트리 디자인
    
    
	@ManyToOne
	@JoinColumn(name = "USERS_ID")
	private Users users;
	
	
	@OneToMany(mappedBy = "tree")
	private List<Message> messages = new ArrayList<Message>();
	
	public void addMessage(Message message) {
		message.setTree(this);
		this.messages.add(message);
	}

	
	
}
