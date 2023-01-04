package com.project.letterOfHeart.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SequenceGenerator(
		name = "USERS_SEQ_GENERATOR"
	    , sequenceName = "USERS_SEQ"
	    , initialValue = 1
	    , allocationSize = 1
	)
public class Users implements UserDetails{

    @Id @GeneratedValue(
    		strategy = GenerationType.SEQUENCE,
    		generator = "USERS_SEQ_GENERATOR")
    @Column(name = "USERS_ID")
    private Long id;

    private String accountId;                // 유저 아이디
    private String password;            // 유저 비밀번호
    
    @Column(length = 8)
    private String nickname;            // 유저 닉네임
    private LocalDateTime createDate;   // 가입 일자
    private String urlrnd;			// 난수 URL
    
    @Column(unique = true)
    private String phone;
    
    @OneToMany(mappedBy = "users")
    private List<Tree> tree = new ArrayList<Tree>();
    
    public void addTree(Tree tree) {
    	tree.setUsers(this);
    	this.tree.add(tree);
    }
    
	// 유저 상태
	private String role;
	  
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
	    Collection<GrantedAuthority> collect = new ArrayList<GrantedAuthority>();
	    collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return role;
			}
		});
	    return collect;
	}
	  
	@Override
	public String getUsername() {
		return accountId;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
    
    
}
