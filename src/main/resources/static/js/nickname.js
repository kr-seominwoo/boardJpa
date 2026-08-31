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

function replaceInputToEmpty(target) {
    target.setAttribute("value", "");
}

function init() {
    const nicknameInput = document.getElementById("nickname");
    const chkNickname = document.getElementById("chk_nickname");
    nicknameInput.addEventListener('focusout', (event) => {
        checkNickname();
        if (nicknameInput.value != chkNickname.value) {
            replaceInputToEmpty(chkNickname);
        } 
    });

    const duplicateNicknameButton = document.getElementById("duplicate_nickname_button");
    duplicateNicknameButton.addEventListener('click', (event) => {
        const nickname = document.getElementById("nickname").value;
        const chkNickname = document.getElementById('chk_nickname').value;

        const nicknameErrMsg = document.getElementById('invalid_nickname_err_msg');
        if (nickname.length < 5 || !nicknameErrMsg.classList.contains('hidden') || nickname == chkNickname) {
            return;
        }

        // 서버로 닉네임 중복검사 보내기 ajax사용
        const curURL = $(window.location)[0].href;
        const slashLastIndex = curURL.lastIndexOf("/");
        let slashIndex = 0;
        for (let i = slashLastIndex - 1; i >=0; --i) {
            let c = curURL[i];
            if (c == '/') {
                slashIndex = i;
                break;
            }
        }


        let url = curURL.substring(0, slashIndex + 1) + "nickname";
        const data = {"nickname" : nickname};
        console.log(url);

        $.ajax({
            url: url,
            type: "GET",
            data : data,
            success: function(data) {
                $("#chk_nickname").attr("value", "");
                $("#chk_nickname").attr("value", nickname);
                alert("사용가능한 닉네임입니다.")
            },
            error: function(xhr) {
                $("#chk_nickname").attr("value", "");
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
        location.replace("/community/members/mypage");
    });

    const submitButton = document.getElementById("submit_button");
    submitButton.addEventListener('click', (event) => {
        const nickname = $("#nickname").val();
        const chkNickname = $("#chk_nickname").val();
        const password = $("#pw").val();

        if (password == "") {
            return;
        }

        if (!checkNickname()) {
            processErr("invalid_nickname_err_msg");
            return;
        }

        if (chkNickname == "" || nickname != chkNickname) {
            alert("닉네임 중복을 체크해주세요.")
            return;
        }

        const data = {
            'nickname' : nickname,
            'password' : password,
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
                if (message == "중복된 닉네임입니다.") {
                    showErrMessage("duplicate_nickname_err_msg");
                    $("#chk_nickname").attr("value", "");
                }
            }
        });
    });
}

init();