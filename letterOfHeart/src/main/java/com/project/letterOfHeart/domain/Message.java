package com.project.letterOfHeart.domain;

import java.time.LocalDateTime;
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
		name = "MESSAGE_SEQ_GENERATOR"
	    , sequenceName = "MESSAGE_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class Message {

    @Id @GeneratedValue(
    		strategy = GenerationType.SEQUENCE,
    		generator = "MESSAGE_SEQ_GENERATOR")
    @Column(name = "MESSAGE_ID")
    private Long id;
    
//    @Column(name = "TREE_ID")
//    private Long treeId;            	// 트리 번호
    
//    private String u_Id;                // 수신인 아이디
    
    private String titleNickname;    	// 작성자 닉네임
    private String content;             // 내용
    private LocalDateTime sendDate;     // 작성 일자
    private LocalDateTime openDate;     // 확인 가능 일자
    private Long status;            	// 0 - 미확인, 1 - 확인
    
    
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Users users;
	@ManyToOne
	@JoinColumn(name = "TREE_ID")
	private Tree tree;
	
	@OneToMany(mappedBy = "message")
	private List<Deco> deco = new ArrayList<Deco>();
	
	public void addDeco(Deco deco) {
		deco.setMessage(this);
		this.deco.add(deco);
	}
	
	
	
}
