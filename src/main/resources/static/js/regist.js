// 비밀번호 적합성 검사
function checkPw() {
    const numberPattern = /[0-9]/; //숫자
    const alphabetPattern = /[a-zA-Z]/; //영어
    const specialPattern = /[!-/:-@\[-`{-~]/; //특수문자
    const excludePattern = /[^!-~]/;

    const pw = document.querySelector("#pw");
    const pwValue = pw.value;
    const pwLen = pwValue.length;
    const pwErrMsg = document.querySelector("#pw_err_msg");

    let resultNumber = false;
    let resultAlphabet = false;
    let resultSpecial = false;
    let resultLen = false;
    let resultExclude = excludePattern.test(pwValue);

    //길이 체크
    if(pwLen >= 8 && pwLen <= 20) {
        pwErrMsg.classList.add("hidden");
        resultLen = true;
    } else {
        pwErrMsg.classList.remove("hidden");
    }

    //숫자 체크
    if (numberPattern.test(pwValue)) {
        document.querySelector("#number").classList.add("green_font");
        resultNumber = true;
    } else {
        document.querySelector("#number").classList.remove("green_font");
    }

    //영어 체크
    if(alphabetPattern.test(pwValue)){
        document.querySelector("#alphabet").classList.add("green_font");
        resultAlphabet = true;
    } else {
        document.querySelector("#alphabet").classList.remove("green_font");
    }

    if(specialPattern.test(pwValue)) {
        document.querySelector("#special").classList.add("green_font");
        resultSpecial = true;
    } else {
        document.querySelector("#special").classList.remove("green_font");
    }

    return resultLen && resultNumber && resultAlphabet && resultSpecial && !resultExclude;
}

// 비밀번호 재확인
function confirmPw() {
    const pw = document.querySelector("#pw");
    const confirmationPw = document.querySelector("#confirmation_pw");
    const confirmationPwErrMsg = document.querySelector("#confirmation_pw_err_msg");
    const confirmationEye = document.querySelector("#confirmation_eye");

    if (pw.value === confirmationPw.value) {
        confirmationPwErrMsg.classList.add("hidden");
        if(pw.value != null){
            confirmationEye.classList.remove("gray_eye");
            confirmationEye.classList.add("green_eye");
            return true;
        }
    } else {
        confirmationPwErrMsg.classList.remove("hidden");
        confirmationEye.classList.remove("green_eye");
        confirmationEye.classList.add("gray_eye");
    }

    return false;
}

function finalCheckPw() {
    const pwEye = document.querySelector("#pw_eye");
    const errMsg = document.querySelector("#invalid_pw_err_msg");
    if(checkPw()){
        pwEye.classList.remove("gray_eye");
        pwEye.classList.add("green_eye");
        errMsg.classList.add("hidden");
    } else {
        pwEye.classList.remove("green_eye");
        pwEye.classList.add("gray_eye");
        errMsg.classList.remove("hidden");
    }
}

// 이메일 적합성
function checkEmail() {
    const email = document.querySelector("#email");
    const address = document.querySelector("#address");
    const emailaddress = email.value + address.value;
    const emailErrMsg =  document.querySelector("#email_err_msg");
    const emailPattern = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i; //이메일

    if(emailPattern.test(emailaddress)){
        emailErrMsg.classList.add("hidden");
        return true;
    } else {
        emailErrMsg.classList.remove("hidden");
        return false;
    }
}

function checkId() {
    const pattern = /^[a-zA-Z0-9-_]{5,20}$/;
    const id = document.getElementById('id').value;
    const idErrMsg = document.getElementById('invalid_id_err_msg');
    if (pattern.test(id)) {
        idErrMsg.classList.add("hidden");
        return true;
    } else {
        idErrMsg.classList.remove("hidden");
        return false;
    }
}

function checkNickname() {
    const pattern = /^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣-_]{5,20}$/;
    const nickname = document.getElementById('nickname').value;
    const nicknameErrMsg = document.getElementById('invalid_nickname_err_msg');
    if (pattern.test(nickname)) {
        nicknameErrMsg.classList.add("hidden");
        return true;
    } else {
        nicknameErrMsg.classList.remove("hidden");
        return false;
    }
}

function processErr(id) {
    showMessage(id);
    scrollMessage(id);
}

function showMessage(id) {
    $("#" + id).removeClass("hidden");
}

function hideMessage(id) {
    $("#" + id).addClass("hidden");
}

function scrollMessage(id) {
    const location = $("#" + id).closest(".info_row").position().top;
    $(window).scrollTop(location);
}

function replaceInputToEmpty(target) {
    target.setAttribute("value", "");
}

function init() {
    const pw = document.querySelector("#pw");
    const confirmationPw = document.querySelector("#confirmation_pw");
    const email = document.querySelector("#email");
    const address = document.querySelector("#address");
    
    pw.addEventListener('change', (event) => {
        // 비밀번호 적합성
        checkPw();
        // 비밀번호 재확인
        confirmPw();
        //비밀번호 최종 확인        
        finalCheckPw();
    });

    confirmationPw.addEventListener('change', (event) => {
        // 비밀번호 재확인
        confirmPw();
    });

    email.addEventListener('change', (event) => {
        // 이메일 적합성
        checkEmail(); 
    });

    address.addEventListener('change', (event) => {
        // 이메일 적합성
        checkEmail(); 
    });

    const idInput = document.getElementById("id");
    const chkId = document.getElementById("chk_id");
    const idPass = "id_pass";
    const diemId = "duplicate_id_err_msg";
    idInput.addEventListener('change', (event) => {
        replaceInputToEmpty(chkId);
        hideMessage(diemId);
        hideMessage(idPass);
        checkId();
    });

    const duplicateIdButton = document.getElementById("duplicate_id_button");
    const iiemId = "invalid_id_err_msg";
    duplicateIdButton.addEventListener('click', (event) => {
        const id = document.getElementById("id").value;
        const idErrMsg = document.getElementById('invalid_id_err_msg');
        if (id.length < 5 || !idErrMsg.classList.contains('hidden')) {
            hideMessage(diemId);
            alert("사용할 수 없는 아이디입니다.")
            return;
        }

        // 서버로 아이디 중복검사 보내기 ajax사용     
        const curURL = $(window.location)[0].href;
        const slashLastIndex = curURL.lastIndexOf("/"); 
        let url = curURL.substring(0, slashLastIndex + 1) + "id";
        const data = {"id" : id};
        
        $.ajax({
            url: url,
            type: "GET",
            data : data,
            success: function(data) {
                hideMessage(diemId);
                hideMessage(iiemId);
                $("#chk_id").attr("value", "");
                $("#chk_id").attr("value", id);
                showMessage(idPass);
                alert("사용할 수 있는 아이디입니다.");                
            },
            error: function(response) {
                const id = response.responseText;
                $("#chk_id").attr("value", "");
                showMessage(id);
                hideMessage(idPass);
                alert("이미 사용 중인 아이디입니다.");                
            }
        });
    });

    const nicknameInput = document.getElementById("nickname");
    const chkNickname = document.getElementById("chk_nickname");
    const nicknamePass = "nickname_pass";
    const dnemId = "duplicate_nickname_err_msg";
    nicknameInput.addEventListener('change', (event) => {
        replaceInputToEmpty(chkNickname);
        hideMessage(dnemId);
        hideMessage(nicknamePass);
        checkNickname();
    });

    const duplicateNicknameButton = document.getElementById("duplicate_nickname_button");    
    const inemId = "invalid_nickname_err_msg";
    duplicateNicknameButton.addEventListener('click', (event) => {
        const nickname = document.getElementById("nickname").value;
        const nicknameErrMsg = document.getElementById(inemId);

        if (nickname.length < 5 || !nicknameErrMsg.classList.contains('hidden')) {
            hideMessage(nicknamePass);
            hideMessage(dnemId);
            alert("사용할 수 없는 닉네임입니다.")
            return;
        }

        // 서버로 닉네임 중복검사 보내기 ajax사용     
        const curURL = $(window.location)[0].href;
        const slashLastIndex = curURL.lastIndexOf("/"); 
        let url = curURL.substring(0, slashLastIndex + 1) + "nickname";
        console.log(url);
        const data = {"nickname" : nickname};
        

        $.ajax({
            url: url,
            type: "GET",
            data : data,
            success: function(data) {
                hideMessage(dnemId);
                hideMessage(inemId);
                $("#chk_nickname").attr("value", "");
                $("#chk_nickname").attr("value", nickname);                
                showMessage(nicknamePass);
                alert("사용할 수 있는 닉네임입니다.");
            },
            error: function(response) {
                const id = response.responseText;
                $("#chk_nickname").attr("value", "");
                showMessage(id);
                hideMessage(nicknamePass);
                alert("이미 사용 중인 닉네임입니다.");
            }
        });
    });

    const backButton = document.getElementById("back_button");
    backButton.addEventListener('click', (event) => {
        /*
        // 뒤로갈 히스토리가 있는 경우
        if (document.referrer) {
            history.back();
        } else { // 히스토리가 없는 경우
            location.replace("login.html");
        }
        */
        location.replace("/community/members/login");
    });

    const submitButton = document.getElementById("submit_button");
    submitButton.addEventListener('click', (event) => {
        const name = $("#name").val();
        const nickname = $("#nickname").val();
        const chkNickname = $("#chk_nickname").val();
        const id = $("#id").val();
        const chkId = $("#chk_id").val();
        const password = $("#pw").val();
        const confirmationPw = $("#confirmation_pw").val();
        const email = $("#email").val() + $("#address option:selected").val();
        const birthday = $("#birthday").val();
        

        if (name == "") {
            return;
        }

        if (!checkNickname()) {
            processErr("invalid_nickname_err_msg");
            return;
        }

        if (chkNickname == "" || nickname != chkNickname) {
            scrollMessage("duplicate_nickname_err_msg");
            alert("닉네임 중복을 체크해주세요.")
            return;
        }

        if(!checkId()) {
            processErr("invalid_id_err_msg");
            return;
        }

        if (chkId == "" || id != chkId) {
            scrollMessage("duplicate_id_err_msg");
            alert("id 중복을 체크해주세요.")
            return;
        }

        if (!checkPw()) {
            processErr("pw_err_msg")
            return;
        }

        if (!confirmPw()) {
            processErr("confirmation_pw_err_msg");
            return;
        }

        if (!checkEmail()) {
            processErr("email_err_msg");
            return;
        }

        if (birthday == "") {
            return;
        }

        const data = {
            'name' : name,
            'nickname' : nickname,
            'id' : id,
            'password' : password,
            'confirmationPassword': confirmationPw,
            'email' : email,
            'birthday' : new Date(birthday)
        };

        const curURL = $(window.location)[0].href;
        const slashLastIndex = curURL.lastIndexOf("/"); 
        const url = curURL.substring(0, slashLastIndex);

        $.ajax({
            url: url,
            type: "POST",
            data : data,
            success: function(data) {
                alert(data);
                document.location.href = "/community";
            },
            error: function(response) {
                const id = response.responseText;
                alert("잘못된 입력이 존재합니다. 다시 확인해주세요");
                if (id == "duplicate_id_err_msg") {
                    $("#chk_id").attr("value", "");
                    hideMessage("duplicate_id_pass");
                } else if (id == "duplicate_nickname_err_msg") {
                    $("#chk_nickname").attr("value", "");
                    hideMessage("duplicate_nickname_pass");
                }

                processErr(id);
            }
        });
    });
}

$(document).ready(function() {
    init();
});