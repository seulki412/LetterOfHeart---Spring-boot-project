/**
 * Naver SENS 번호 인증
 */
 

window.addEventListener("load", function(event){
	 
	var phone = document.getElementById("phone");
    var numberck = document.getElementById("numberck");
    var authnumber = document.getElementById("authnumber");
    var smsck = document.getElementById("smsck");
    var numbermsg = document.getElementById("numbermsg");
    var correctnumber = null;
    
	numberck.addEventListener("click", function(){
        if(phone.value.trim().length<1){
            numbermsg.innerHTML = "전화번호는 필수 입력입니다.";
            return;
        }else{
            numbermsg.innerText = "";
        }

        $.ajax({
            type: "POST",
            url: "/sign/numbercheck",
            data: {"phone":phone.value},
            dataType: "json",
            success: function (data, statusText) {
                var temp = JSON.stringify(data);
                var map = JSON.parse(temp);
                //alert(JSON.stringify(map.result));
                if(map.result == "-1") { //중복 & 전송 실패
                    smsauth = false;
                    numbermsg.innerHTML = map.message;
                }else{
                    smsauth = true;
                    numbermsg.innerText = map.message;
                    correctnumber = map.random;
                    $("#phone").prop("type", "hidden");
                    $("#numberck").prop("type", "hidden");
                    $("#authnumber").prop("type", "text");
                    $("#smsck").prop("type", "button")
                }
            },
            error: function (data, statusText) {
                alert("에러 발생");
                var temp = JSON.stringify(data);
                var map = JSON.parse(temp);
            }
        });
    });

    smsck.addEventListener("click", function (){
        if(authnumber.value==correctnumber){
            numbermsg.innerHTML = "인증되었습니다.</br>";
            smsauth = true;
        }else{
            numbermsg.innerHTML = "인증에 실패하였습니다.</br>";
            smsauth = false;
        }
    });

});


function regist() {
	var form = document.getElementById("signUpForm");
	
	if (confirm("핸드폰 번호를 검증하지 않으면 , 비밀번호 찾는데 이후에 문제가 생길 수 있습니다.")) {
        form.submit();
    } 
}
