package com.project.letterOfHeart.service;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserService {

	//전화번호 중복 체크
    public boolean phoneNumberCheck(String number);

    //전화번호 인증(문자) -> Naver Sens
    //5분 동안 인증 가능하도록 하고, 5분 지나면 다시 문자 인증 버튼 출력하도록 구현
    public boolean sendSms(String userNumber, int authNumber);

    //Signature(서명) 생성
    public String getSignature(String time) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException;

}
