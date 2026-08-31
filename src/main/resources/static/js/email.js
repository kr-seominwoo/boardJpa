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
    const email = document.querySelector("#email");
    const address = document.querySelector("#address");

    email.addEventListener('change', (event) => {
        // 이메일 적합성
        checkEmail(); 
    });

    address.addEventListener('change', (event) => {
        // 이메일 적합성
        checkEmail(); 
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
        const password = $("#pw").val();
        const email = $("#email").val() + $("#address option:selected").val();

        if (password == "") {
            return;
        }

        if (email == "") {
            return;
        }

        if (!checkEmail()) {
            processErr("email_err_msg");
            return;
        }

        const data = {
            'password' : password,
            'email' : email,
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
                const message = response.responseText;
                alert(message);
            }
        });
    });
}

init();