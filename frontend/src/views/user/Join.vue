<template>
  <div>
    <v-toolbar dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">회원가입</p>
      <v-spacer></v-spacer>
    </v-toolbar>

    <div class="pa-sm-4 pa-md-6 pa-lg-12">
      <v-form ref="form">
        <v-col class="pb-0">
              <v-text-field
                :rules="[() => !!name || '이름을 입력해주세요']"
                outlined hide-details="auto" 
                v-model="name" 
                label="이름"
                clearable
              ></v-text-field>
        </v-col>
        <v-col class="pb-0">
              <v-text-field
                :rules="[() => !!nickName || '닉네임을 입력해주세요']"
                :error-messages="error.nickName"
                :hint="hint.nickName"
                outlined hide-details="auto" 
                v-model="nickName"
                label="닉네임"
                clearable
                class="pa-0"
                @input="hint.nickName=''"
              >
                <template v-slot:append>
                  <v-btn ref="button1" @click="checkNickname" class="ml-2 px-0" color="blue-grey lighten-1" style="top:-9px;" height="40" width="30" tile>
                    <label class="my-0 white--text text-caption">중복확인</label>
                  </v-btn>
                </template>
              
              
              </v-text-field>
        </v-col>
        <v-col class="pb-0">
              <v-text-field
                :rules="[() => !!email || '이메일을 입력해주세요', rules.email]"
                :error-messages="error.email"
                :hint="hint.email"
                outlined hide-details="auto" 
                v-model="email"
                label="이메일"
                clearable
                @input="hint.email=''"
              >
                <template v-slot:append>
                  <v-btn @click="checkEmail" class="ml-2 px-0" color="blue-grey lighten-1" style="top:-9px;" height="40" width="30" tile>
                    <label class="my-0 white--text text-caption">중복확인</label>
                  </v-btn>
                </template>
              </v-text-field>
        </v-col>
        <v-col class="pb-0">
              <v-text-field
                :rules="[() => !!password || '패스워드를 입력해주세요', rules.password]"
                outlined hide-details="auto" 
                type="password"
                v-model="password"
                label="비밀번호"
                clearable
              ></v-text-field>
        </v-col>
        <v-col class="pb-0">
              <v-text-field
                :rules="[() => password == passwordConfirm || '패스워드가 다릅니다.']"
                outlined hide-details="auto" 
                type="password"
                v-model="passwordConfirm"
                label="비밀번호 확인"
                clearable
              ></v-text-field>
        </v-col>
        <v-col class="pb-0">
               <v-text-field
               :rules="[() => !!phone || '연락처를 입력해주세요']"
                outlined hide-details="auto" 
                v-model="phone"
                label="연락처"
                clearable
              ></v-text-field>
        </v-col>
        <v-col class="pb-2">
            <v-checkbox
              v-model="isTerm"
              :rules="[() => !!isTerm || '약관에 동의해주세요.']"
            >
            <template v-slot:label >
              <label class="text-body-2">
                [필수] 
                <a href="#" @click.stop.prevent="dialog = true, joinrule=1">이용약관</a>,
                <a href="#" @click.stop.prevent="dialog = true, joinrule=2">개인정보처리방침</a>,
                <a href="#" @click.stop.prevent="dialog = true, joinrule=3">위치기반서비스약관</a>
                에 동의합니다.
              </label>
            </template>
            </v-checkbox>
        </v-col>
      </v-form>

    <v-dialog v-model="dialog" max-width="375"> 
      <v-card>
        <v-card-title class="h5 font-weight-bold mb-0">
          <span v-if="joinrule==1">이용약관</span>
          <span v-else-if="joinrule==2">개인정보처리방침</span>
          <span v-else-if="joinrule==3">위치기반서비스약관</span>
          <v-spacer></v-spacer>
          <v-icon @click="dialog=false">mdi-close</v-icon>
        </v-card-title>
        <v-divider class="mt-0"></v-divider>
        <v-card-text>
          <Join-Rule1 v-if="joinrule == 1"></Join-Rule1>
          <Join-Rule2 v-else-if="joinrule == 2"></Join-Rule2>
          <Join-Rule3 v-else-if="joinrule == 3"></Join-Rule3>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn
            text @click="dialog=false"
          >닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
    <v-col>
    <v-btn color="orange lighten-1" block class="text-white" @click="checkFormAndSignUp">다음</v-btn>
    </v-col>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";



import * as EmailValidator from "email-validator";
import PV from "password-validator";
import UserApi from "../../api/UserApi";
import JoinRule1 from "../user/JoinRule1"
import JoinRule2 from "../user/JoinRule2"
import JoinRule3 from "../user/JoinRule3"

const SERVER_URL = "https://don.sbbro.shop/api";

export default {
  name: "Join",
  components : {
    JoinRule1, JoinRule2, JoinRule3
  },
  created() {
    this.component = this;
  },
  data: () => {
    return {
      email: "",
      password: "",
      passwordConfirm: "",
      nickName: "",
      name: "",
      phone: "",
      isTerm: false,
      dialog : false,
      hint : {
        email : '',
        nickName: '',
      },
      error : {
        email : '',
        nickName: '',
      },
      joinrule : '',

      // 유효성 검사를 위한 rule
      rules: {
        nickName : () => {
          if(this.error.nickName)
            return true;
          else 
            return false;
        },
        password : value =>{
          const pattern = /^[A-Za-z0-9+]{8,}$/; 
          var checkNum = !value ? false : value.search(/[0-9]/g);
          var checkEng = !value ? false : value.search(/[a-z]/ig);
          if(checkNum < 0 || checkEng < 0 || !pattern.test(value))
            return "영문,숫자 포함 8 자리이상이어야 합니다.";
          else
            return true;
        },
        email: value => {
          const pattern = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
          return pattern.test(value) || '이메일 형식이 아닙니다.'
        },
      },
    };
  },
  methods: {
    checkNickname(){
       axios
        .get(`${SERVER_URL}/account/signup/valid?nickname=${this.nickName}&email=${this.email}`)
        .then(response => {
          // data : success / isExistNickname
          var data = response.data.data;
          // console.log(data);
          if (data == "isExistNickname") {
            this.error.nickName =
              "사용중인 닉네임입니다. 다른 닉네임을 입력해주세요.";
          }
          else{
            this.error.nickName = '';
            this.hint.nickName = "사용가능한 닉네임입니다."
          }
        })
        .catch(error => {
          // console.log(error.response);
        });
    },
    checkEmail(){
       axios
        .get(`${SERVER_URL}/account/signup/valid?nickname=${this.nickName}&email=${this.email}`)
        .then(response => {
          // data : success / isExistEmail
          var data = response.data.data;
          // console.log(data);

          if (data == "isExistEmail") {
            this.error.email =
              "사용중인 이메일입니다. 다른 이메일을 입력해주세요.";
          }
          else{
            this.error.email = '';
            this.hint.email = "사용가능한 이메일입니다."
          }
        })
        .catch(error => {
          // console.log(error.response);
        });
    },
    checkFormAndSignUp() {
      // 모든 폼들의 유효성 검사가 됐다면
      if(this.hint.nickName == ''){
        this.error.nickName = "닉네임 중복확인 해주세요"
        return;
      }
      else if(this.hint.email == ''){
        this.error.email = "이메일 중복확인 해주세요"
        return;
      }
      else if(this.$refs.form.validate()){
        let userInfo = {
          email: this.email,
          password: this.password,
          nickName: this.nickName,
          name: this.name,
          phone:this.phone
        };

        // 현재 페이지에서 입력받은 정보를 params에 넣어서 다음 페이지로 이동
        router.push({name : "JoinInfo", params : userInfo});
      }
    },
  }
};
</script>


<style scoped>
</style>