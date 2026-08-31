function verify() {    
    const curURL = $(window.location)[0].href;
    const index= curURL.indexOf("community") + 9;
    const refreshTokenURL = curURL.substring(0, index) + "/refresh";
    console.log("현재 url : " + curURL);
    console.log("refresh 페이지 " + refreshTokenURL);
    $.ajax({
        url: refreshTokenURL,
        type: "POST",
    })    
    .then(function() {
        location.reload();
    });    
}

verify();