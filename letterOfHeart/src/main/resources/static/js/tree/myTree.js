/**
 * myTree.html validation check
 */

var check = false;

function send() {
	var form = document.writeLetterForm;

	if (!form.titleNickname.value) {
		alert("작성자 닉네임을 작성해주세요.");
		form.titleNickname.focus();
		return;
	}

	if (form.titleNickname.value.length < 2 || form.titleNickname.value.length > 8) {
		alert("닉네임은 2자 이상, 8자 이하로 입력해주세요.");
		form.titleNickname.focus();
		return false;
	} else {
		check = true;
	}

	if (!form.titleNickname.value || !check) {
		alert("닉네임을 확인해주세요");
		form.titleNickname.focus();
		return;
	}

	if (!form.content.value) {
		alert("편지 내용을 작성해주세요.");
		form.content.focus();
		return;
	}
	
	if (form.content.value.length < 5 || form.content.value.length > 500) {
		alert("편지 내용은 5자 이상, 500자 이하로 작성해주세요.");
		form.content.focus();
		return false;
	} else {
		check = true;
	}

	form.submit();
}