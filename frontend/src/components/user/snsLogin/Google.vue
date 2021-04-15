<template>
    <div id="google-signin-button" class="google-login" @click="onClick">
        <button >
            <svg xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" width="55" height="55" viewBox="0 0 55 55">
                <defs>
                    <clipPath id="clip-path">
                    <path id="패스_155" data-name="패스 155" d="M21.023,10.057H11.847v3.8h5.282c-.492,2.417-2.551,3.8-5.282,3.8a5.819,5.819,0,1,1,0-11.637,5.693,5.693,0,0,1,3.625,1.3l2.865-2.865a9.841,9.841,0,1,0-6.49,17.232c4.923,0,9.4-3.581,9.4-9.847A8.169,8.169,0,0,0,21.023,10.057Z" transform="translate(-2 -2)"/>
                    </clipPath>
                </defs>
                <g id="그룹_248" data-name="그룹 248" transform="translate(-300 -406)">
                    <g id="타원_6" data-name="타원 6" transform="translate(300 406)" fill="#fff" stroke="#eee" stroke-width="1">
                    <circle cx="27.5" cy="27.5" r="27.5" stroke="none"/>
                    <circle cx="27.5" cy="27.5" r="27" fill="none"/>
                    </g>
                    <g id="Google__G__Logo" transform="translate(318 424)">
                    <g id="그룹_114" data-name="그룹 114" clip-path="url(#clip-path)">
                        <path id="패스_154" data-name="패스 154" d="M0,22.637V11l7.609,5.819Z" transform="translate(-0.895 -6.972)" fill="#fbbc05"/>
                    </g>
                    <g id="그룹_115" data-name="그룹 115" clip-path="url(#clip-path)">
                        <path id="패스_156" data-name="패스 156" d="M0,4.923l7.609,5.819,3.133-2.73L21.484,6.266V0H0Z" transform="translate(-0.895 -0.895)" fill="#ea4335"/>
                    </g>
                    <g id="그룹_116" data-name="그룹 116" clip-path="url(#clip-path)">
                        <path id="패스_158" data-name="패스 158" d="M0,16.561,13.428,6.266l3.536.448L21.484,0V21.484H0Z" transform="translate(-0.895 -0.895)" fill="#34a853"/>
                    </g>
                    <g id="그룹_117" data-name="그룹 117" clip-path="url(#clip-path)">
                        <path id="패스_160" data-name="패스 160" d="M28.666,27.561,14.79,16.819,13,15.476,28.666,11Z" transform="translate(-8.077 -6.972)" fill="#4285f4"/>
                    </g>
                    </g>
                </g>
            </svg>

        </button>
    </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";


const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";
    export default {
        data(){
            return {
            }
        },
        created(){
          


        },
        methods:{

            onClick(){

                 var user ={
                        name : '',
                        email : '',
                        }
                var router= this.$router;
                var cookie = this.$cookie;
             window.gapi.load('auth2', () =>  {
                    var gauth = window.gapi.auth2.init({
                            client_id: '358141877415-v04s2e2im90i6cpacn828shokn0qe5bh.apps.googleusercontent.com',
                            scope: 'profile'
                        });
                        
                        gauth.then(function(response){
                            // console.log('init success');
                            var gauth = window.gapi.auth2.getAuthInstance();
                            gauth.signIn().then(function(){

                                var userData = gauth.currentUser.get().getBasicProfile();
                             
                                //닉네임이 겹치지않기 위해 시간을 넣는다.      
                               let today = new Date();   
                                let hours = today.getHours(); // 시
                                let minutes = today.getMinutes();  // 분
                                let seconds = today.getSeconds();  // 초
                                let milliseconds = today.getMilliseconds(); // 밀리초

                               const userInfo = {
                                   userName : userData.getName(),
                               userNickname : '구글'+hours+''+minutes+''+seconds+''+milliseconds,
                               userEmail : userData.getEmail(),
                               userPwd : 'google',
                               userComment : '구글회원입니다 수정해주세요',
                                userImg : 'profile_default.png'
                               }

                                //이메일 닉네임 유효성 검사
                                axios.get(`${SERVER_URL}/account/signup/valid?nickname=${userInfo.userNickname}&email=${userInfo.userEmail}`)
                                .then((response)=>{
                                    //이메일 닉네임 유효성 검사 성공
                                    // console.log(response);
                                    if(response.data.status){
                                        //구글 계정으로 등록
                                        axios.post(`${SERVER_URL}/account/apisignup`,userInfo)
                                        .then(res => {
                                        // console.log("성공");
                                        alert("가입되었습니다. 다시로그인해주세요");
                                        })
                                        .catch(err => {
                                            // console.log("실패");
                                        })
                                    }
                                    else{
                                            axios
                                        .get(
                                        `${SERVER_URL}/account/login?email=${userInfo.userEmail}&password=google`
                                        )

                                        .then((response) => {
                                        // console.log(response.data);

                                        cookie.set("accesstoken", response.data, 1);
                                        cookie.set("userId", response.data.object.userId, 1);
                                        cookie.set("useremail",userInfo.userEmail,1);
                                        router.push("/map");
                                        })

                                        .catch((error) => {
                                        // console.log(error.response);
                                        alert("로그인 실패");
                                        });
                                    }

                                }).catch((e)=>{
                                    //이메일 닉네임이 이미 있을경우
                                    //로그인 작업을 처리한 후 쿠키에 넣어준다
                                    // console.log(e);
                                });
                        
                            });
                        }, function(){
                            console.error('init fail');
                        })
                })

            },
            login(){
                // console.log("되라좀");
            }

           
            
        }
    }
</script>

<style scoped>
span {
    /* all: unset !important; */
    all: unset;
}
</style>