<template>
  <v-app>
    <v-toolbar-title >
      <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">프로필 수정</p>
      <v-spacer></v-spacer>
      <a @click="checkForm">
          <i class="fas fa-check"></i>
        </a>
    </v-toolbar>
    </v-toolbar-title>
    
    <div class="entireClass">
    <v-layout row>
        <v-spacer></v-spacer>
        <v-avatar class="p-img" size="100" >
          <v-img 
            v-if="viewImg" :src="viewImg">
          </v-img>
        </v-avatar>
        <v-spacer></v-spacer>
    </v-layout>
    <v-layout>
        <v-spacer></v-spacer>
        <v-btn small type="button" class="mr-2" @click="onClickImageChange" >수정</v-btn>
        <input ref="imageInput" type="file" hidden @change="onChangeImage">
        <v-btn small @click="onClickSubmit">확인</v-btn>
        <v-spacer></v-spacer>
    </v-layout>
    <v-layout row>
   <v-main>
    <v-col class="pb-0">
          <v-text-field
            outlined hide-details="auto" 
            v-model="user.userName" 
            disabled
            label="이름"
            clearable
          ></v-text-field>
      </v-col>
      <v-col class="pb-0">
        <v-text-field
        outlined hide-details="auto" 
        v-model="userNickname"
        label="닉네임"
        clearable
        ref="userNickname"
        >          
        </v-text-field>
      </v-col>

      <v-col class="pb-0">
        <v-text-field
          outlined hide-details="auto" 
          type="password"
          :append-icon="show1 ? 'mdi-eye' : 'mdi-eye-off'" 
          v-model="userPwd"
          label="비밀번호"
          :disabled="show"
          ref="userPwd"
        ></v-text-field>
      </v-col>

        <v-col class="pb-0">
               <v-text-field
                outlined hide-details="auto" 
                v-model="user.userEmail"
                label="이메일"
                disabled
                clearable
              ></v-text-field>
        </v-col>

        <v-col class="pb-0">
               <v-text-field
                outlined hide-details="auto" 
                v-model="user.userPhone"
                label="연락처"
                disabled
                clearable
              ></v-text-field>
        </v-col>

        <v-col class="pb-0">
               <v-text-field
                outlined hide-details="auto" 
                v-model="userComment"
                label="코멘트"
                clearable
              ></v-text-field>
        </v-col>
    </v-main>
    </v-layout>
    </div>
  </v-app>
</template>

<script>
import PV from "password-validator";
import axios from "axios";
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";


export default {
  data: () => {
    return {
      passwordSchema: new PV(),
      nickName: "",
      error: {
        nickName: true,
      },
      show : false,
      show1: false,
      user : {},
      userNickname : "",
      userPwd : "",
      userComment : "",
      userImg : "",
      userId : "",
      viewImg:""
    };
  },
  methods: {
    //이미지 변경버튼이 클릭됐을때
    //파일 입력 창을 띄운다
    onClickImageChange(){
      this.$refs.imageInput.click();
    },

    //이미지 보이는부분 변경
    onChangeImage(e){
      this.file = e.target.files[0];
      this.viewImg = URL.createObjectURL(this.file);
    },

    //이미지 입력 버튼을 클릭했을때
    //서버와 통신하여 이미지를 저장하고 url을 반환.
    onClickSubmit(){
      const file = new FormData();
      file.append('file',this.file);
      axios
      .post(`${SERVER_URL}/userpage/img`,file)

      .then((response) => {
        this.userImg = response.data;
        this.addImg();
      })

      .catch((error) => {
        // console.log(error.response);
        alert("이미지 전송 실패");
      });
    },

    addImg(){
      this.userImgCpy = this.userImg;
      axios
      .get(`${SERVER_URL}/userpage/updateimg?user_img=${this.userImgCpy}&user_id=${this.userId}`
        )

      .then((response) => {
        alert("성공");
      })

      .catch((error) => {
        // console.log(error.response);
        alert("데이터 전송 실패");
      });
    },

    checkpwd() {
      
    },
    checkForm() {
      this.error.nickName = false;
      this.updateUser();
    },
    updateUser (){
      if (this.userNickname.length < 1){
        this.$refs.userNickname.focus()
        this.$alert("닉네임을 입력해주세요.");
      }
      else if (this.userPwd.length > 0 && !this.passwordSchema.validate(this.userPwd) && !this.show){
        this.$refs.userPwd.focus()
        this.$alert("영문,숫자 포함 8 자리이상이어야 합니다.");
      }
      else{
        axios
        .put(
          `${SERVER_URL}/userpage/updateuser?userId=${this.user.userId}&userNickname=${this.userNickname}&userPwd=${this.userPwd}&userComment=${this.userComment}`
        )
        .then((response) => {
          if(response.data.data == "isExistNickname"){
            this.$refs.userNickname.focus()
            this.$alert("사용중인 닉네임입니다. 다른 닉네임을 입력해주세요.");
          }
          else{
            // 정상 동작
            this.$alert("수정완료");
            this.$router.go(-1);
          }
        })
        .catch((error) => {
          // console.log(error.response);
        });  
      }
    }
  },
  created(){
    this.passwordSchema
      .is()
      .min(8)
      .is()
      .max(100)
      .has()
      .digits()
      .has()
      .letters();
    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
      )
      .then((response) => {
        this.user = response.data;
        this.userNickname = this.user.userNickname;
        this.userPwd = this.user.userPWd;
        if(this.userPwd == "google" || this.userPwd == "kakao"){
          this.show = true;
        }
        this.userComment = this.user.userComment;
        this.userImg = this.user.userImg;
        this.userId = this.$cookie.get("userId");
        this.viewImg = SERVER_URL+"/img/user?imgname="+this.userImg;
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
  };
</script>

<style scoped>
  .fa-chevron-left {
    color: white; 
    margin-left: 7px;
   }
  .update-buttons {
    text-align: center;
    margin-bottom: 12px;
  } 
  .p-img {
    margin: 1px 0 10px 12px;
  }
  .text-field-css{
    margin: 3px;
  } 
</style>