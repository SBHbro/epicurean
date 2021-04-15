<template>
  <div>
    <v-toolbar  class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">비밀번호 찾기</p>
      <v-spacer></v-spacer>
    </v-toolbar>

    <div class="entire">
      <v-form ref="form">
        <v-col class="pb-0">
          <p>※ 회원가입시 입력한 정보를 입력해주세요</p>
          <v-text-field
            :rules="[() => !!name || '이름을 입력해주세요']"
            :error-messages="error.name"
            outlined hide-details="auto" 
            v-model="name" 
            label="이름"
            clearable
          ></v-text-field>
        </v-col>
        
        <v-col class="pb-0">
          <v-text-field
            :rules="[() => !!email || '이메일을 입력해주세요', rules.email]"
            :error-messages="error.email"
            outlined hide-details="auto" 
            v-model="email"
            label="이메일"
            clearable
          >
          </v-text-field>
        </v-col>
     </v-form>
     </div>
    <!-- 이름 -->
    <div class="entire"><br>
    <v-btn @click="checkFormAndFindpw" color="warning find-btn">확인</v-btn>
  </div>
  </div>
</template>

<script>
import axios from "axios";
import * as EmailValidator from "email-validator";

const SERVER_URL = "https://don.sbbro.shop/api";

export default {
  name: "FindPw",
  methods: {
    checkFormAndFindpw() {
      // 전체 폼 체크(형식)
      this.checkForm();
      // 유효한 data이면
      if (this.isValidForm()){
        this.onFindPw();}      
    },
    checkForm() {
      if (this.name.length < 1){
        this.error.name = "이름 입력해주세요.";
        return;
      }
      else this.error.name = '';
    
      if(this.email.length < 1){
        this.error.email = "이메일을 입력해주세요.";
        return;
      }
      else if (this.email.length > 0 && !EmailValidator.validate(this.email)){
        this.error.email = "이메일 형식이 아닙니다.";
        return;
      }
      else this.error.email = '';
    },
    isValidForm() {
      for (let key in this.error) {
        if (this.error[key]) return false;
      }
      return true;
    },
    
    onFindPw() {
      let userData = {
        userEmail : this.email,
        userName : this.name
      };
      axios.post(`${SERVER_URL}/account/checkUser`, userData)
        .then(res => {
          // isNotExistName / isNotExistEmail / success  
          var data = res.data.data;
          
          if(data == 'isNotExistName'){
            this.error.name = '가입되지 않는 이름입니다.'
          }
          if (data == 'isNotExistEmail'){
            this.error.email = '가입되지 않는 이메일입니다.'
          }
          else if (data == 'success'){
            this.$router.push("/user/foundpw")
            axios.post(`${SERVER_URL}/account/sendUserPwd`, userData)
          }
        })
        .catch(error => {
          // console.log(error.res)
        })
    },
  },
  data() {
    return {
      name: "",
      email: "",
      phone: "",
      error: {
        name: '',
        email: '',
      },
       // 유효성 검사를 위한 rule
      rules: {
        nickName : () => {
          if(this.email.length > 0)
            return true;
          else 
            return false;
        },
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          return pattern.test(value) || '이메일 형식이 아닙니다.'
        },
      },
    }
  },
};
</script>

<style>
h2 {
  text-align: center;
}
.find-btn {
  margin: 20px 0;
  height: 200px;
  width: 100%;
}
p {
  margin: 0 0 0 5px;
}
.cell-label {
  position: relative;
}
.cell-auth {
  position: absolute;
  left: 277px;
  bottom: 168px;
}
</style>