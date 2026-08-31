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
    if(checkPw()){
        pwEye.classList.remove("gray_eye");
        pwEye.classList.add("green_eye");
    }else {
        pwEye.classList.remove("green_eye");
        pwEye.classList.add("gray_eye");
    }
}

function processErr(id) {
    showErrMessage(id);
    scrollErrMessage(id);
}

function showErrMessage(id) {
    $("#" + id).removeClass("hidden");
}

function hideErrMessage(id) {
    $("#" + id).addClass("hidden");
}

function scrollErrMessage(id) {
    const location = $("#" + id).closest(".info_row").position().top;
    $(window).scrollTop(location);
}

function init() {
    const pw = document.querySelector("#pw");
    const confirmationPw = document.querySelector("#confirmation_pw");
    
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
        location.replace("/community/members/mypage");
    });

    const submitButton = document.getElementById("submit_button");
    submitButton.addEventListener('click', (event) => {
        const currentPassword = $("#current_pw").val();
        const password = $("#pw").val();
        const confirmationPw = $("#confirmation_pw").val();

        if (currentPassword == "" || password == "" || confirmationPw == "") {
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

        const data = {
            'currentPassword' : currentPassword,
            'password' : password,
            'confirmationPassword': confirmationPw,
        };

        const url = $(window.location)[0].href;

         $.ajax({
             url: url,
             type: "POST",
             data : data,
             success: function(data) {
                 alert(data);
                 document.location.href = "/community/members/mypage";
             },
             error: function(response) {
                 const id = response.responseText;
                 alert("잘못된 입력이 존재합니다. 다시 확인해주세요");
                 processErr(id);
             }
         });
    });
}

init();