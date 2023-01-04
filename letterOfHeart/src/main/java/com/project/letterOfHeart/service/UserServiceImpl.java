package com.project.letterOfHeart.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.letterOfHeart.domain.Users;
import com.project.letterOfHeart.repository.SmsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private SmsRepository smsRepository;
	
	@Value("${serviceId}")
	private String serviceId;

	@Value("${accessKey}")
	private String accessKey;
	
	@Value("${secretKey}")
	private String secretKey;
	
	@Value("${from}")
	private String from;

	@Override
	public boolean phoneNumberCheck(String phone) {
		String parsedNumber = phone.trim();
        boolean result = false; //중복된 전화번호 없음
        Users user = smsRepository.findByPhone(parsedNumber);
        if(user!=null) result = true; //중복된 전화번호 있음

        return result;
	}


	@Override
	public boolean sendSms(String userNumber, int authNumber) {
		boolean result = false; //default = 실패
        String time = String.valueOf(System.currentTimeMillis());
        String accessKey = this.accessKey;

        String serviceId = this.serviceId;
        String from = this.from; //등록한 번호만 사용 가능
        String to = userNumber;
        String subject = "[내 트리를 꾸며줘]"; //기본 메시지 제목
        String apiUrl = "https://sens.apigw.ntruss.com/sms/v2/services/"+serviceId+"/messages";

        JSONObject bodyJson = new JSONObject();
        JSONObject toJson = new JSONObject();
        JSONArray toArr = new JSONArray();

        toJson.put("to", to);
        toJson.put("content", "인증번호["+authNumber+"]를 입력해 주세요.");
        toArr.add(toJson);

        bodyJson.put("type", "SMS");
        bodyJson.put("contentType", "COMM");
        bodyJson.put("countryCode", "82");
        bodyJson.put("from", from);
        bodyJson.put("subject", subject);
        bodyJson.put("content", "인증번호 전송"); //to가 우선순위가 높음
        bodyJson.put("messages", toArr);

        String body = bodyJson.toJSONString();

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn =  (HttpURLConnection)url.openConnection();
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("content-type", "application/json; charset=utf-8");
            conn.setRequestProperty("x-ncp-apigw-timestamp", time);
            conn.setRequestProperty("x-ncp-iam-access-key", accessKey);
            conn.setRequestProperty("x-ncp-apigw-signature-v2", getSignature(time));

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.write(body.getBytes());
            dos.flush();
            dos.close();

            int responseCode = conn.getResponseCode();
            
            System.out.println("responseCode : " + responseCode);
            
            BufferedReader br;
            if(responseCode==202){
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                result = true; //성공
            } else{
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                result = false; //실패
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while((inputLine = br.readLine()) != null){
                response.append(inputLine);
            }
            br.close();

            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e){
        	throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        return result;
	}

	@Override
	public String getSignature(String time) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
		String serviceId = this.serviceId;
        String accessKey = this.accessKey;
        String secretKey = this.secretKey;
        String space = " ";
        String newLine = "\n";
        String method = "POST";
        String url = "/sms/v2/services/" + serviceId + "/messages";

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(time)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
        String encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);

        return encodeBase64String;
	}

}
