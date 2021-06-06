alert("Hello");

function codeverify(){
    var code=document.getElementById('verificationCode').value;
    coderesult.confirm(code).then(function(result){

        firebase.auth().currentUser.getIdToken(/* forceRefresh */ true).then(function(idToken) {
            let xhttp = new XMLHttpRequest();
            xhttp.onload = function() {
                console.log("done");
            }
            xhttp.open("POST","/private/sessionLogin");  //here is where the cookie is sset
            console.log(idToken);
            xhttp.setRequestHeader("Content-type", "application/json");
            xhttp.send(idToken);
            location.replace("http://localhost:8082/success");
            alert("Registered");
            //
        }).catch(function(error) {
            console.log("error");
        })
        /*let idToken = result.user.getIdToken();
        let xhttp = new XMLHttpRequest();
        xhttp.onload = function() {
            console.log("done");
        }
        xhttp.open("POST","/private/sessionLogin");
        //xhttp.setRequestHeader("Content-type", "application/json");
        console.log("sending request to sessionLogin, id token = " + idToken['i']);
        console.log(idToken);
        console.log(idToken[i]);
        xhttp.send("idToken=" + idToken['i']);
        //location.replace("http://localhost:8082/success")
        alert("Successfully registered");*/
    }).catch(function(error){
        alert(error.message);
    });
}


function phoneAuth() {
    console.log("function: phone auth");
    let contactNumber = document.getElementById("phoneNumber").value; //getting phone number
    let xhttp = new XMLHttpRequest();
    xhttp.open("POST","http://localhost:8082/check")
    xhttp.setRequestHeader("Content-type", "application/json");xhttp.onload = function() {
        let status = JSON.parse(xhttp.responseText);
        if (status[0].phoneNumber === "success") {
            console.log("phone number okay");
            //sessionStorage.setItem("contactNumber", contactNumber);
            phoneAuthSecond()
        } else {
            alert("Sorry, your number not there in db man!");
        }
    }
    xhttp.send(contactNumber);

    /*let xhttp = new XMLHttpRequest(); //creating ajax req
    xhttp.open("GET","http://localhost:8082/check/" + contactNumber,true); //sending request
    xhttp.onload = function() {
        let status = JSON.parse(xhttp.responseText);
        if (status[0].phoneNumber === "success") {
            console.log("phone number okay");
            //sessionStorage.setItem("contactNumber", contactNumber);
            phoneAuthSecond()
        } else {
            alert("Sorry, your number not there in db man!");
        }
    }
    xhttp.send();*/

}

/*function phoneAuthTester() {
    firebase.auth().setPersistence(firebase.auth.Auth.Persistence.NONE);

    firebase.auth().signInWithPhoneNumber(document.getElementById('phoneNumbe-r').value).then(user => {
        return user.getIdToken().then(idToken => {

            //const csrfToken = getCookie('csrfToken');
            let xtthp
            return postIdTokenToSessionLogin('/private/sessionLogin', idToken);
        });
    }).then(() => {
        return firebase.auth().signOut();
    }).then(() => {
        window.location.assign('/profile');
    });
}*/

function phoneAuthSecond() {
    const number = document.getElementById('phoneNumber').value;
    firebase.auth().signInWithPhoneNumber(number, window.recaptchaVerifier).then(function (confirmationResult) {
        window.confirmationResult = confirmationResult;
        coderesult = confirmationResult;
        console.log("code result:" + coderesult);
        alert("Message sent");


    }).catch(function (error) {
        alert(error.message)
    });
}


var firebaseConfig = {
    apiKey: "AIzaSyDM52NzMtY6h9ZL3P_XpqHOMlPTufdB0SI",
    authDomain: "avtc-hfn.firebaseapp.com",
    databaseURL: "https://avtc-hfn.firebaseio.com",
    projectId: "avtc-hfn",
    storageBucket: "avtc-hfn.appspot.com",
    messagingSenderId: "792429164181",
    appId: "1:792429164181:web:df326d796542b56eee0d9c",
    measurementId: "G-76XMRKRE3N"
};
// Initialize Firebase
firebase.initializeApp(firebaseConfig);
firebase.analytics();