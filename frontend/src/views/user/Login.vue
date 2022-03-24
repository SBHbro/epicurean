<template>
  <div>
    <v-toolbar class="" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">로그인</p>
      <v-spacer></v-spacer>
    </v-toolbar>

      <div class="pa-sm-4 pa-md-6 pa-lg-12">
        <v-col class="text-center">
          <img class="my-8" src="../../assets/images/logo.png" alt style="width:30%;" />
        </v-col>
        <v-form ref="form">
          <v-col class="pt-0">
            <v-text-field
              v-model="email"
              :rules="[() => !!email || '이메일을 입력해주세요', rules.email]"
              outlined hide-details="auto"
              @keyup.enter="onLogin"
              placeholder="이메일을 입력하세요."
              type="text"
              clearable
            ></v-text-field>
          </v-col>
          <v-col class="pt-0">
            <v-text-field
              v-model="password"
              :rules="[() => !!password || '패스워드를 입력해주세요', rules.password]"
              outlined hide-details="auto"
              @keyup.enter="onLogin"
              placeholder="비밀번호를 입력하세요."
              type="password"
              clearable
            ></v-text-field>
          </v-col>
          <v-col>
          <v-btn block color="orange accent-2 white--text" @click="onLogin">
            로그인
          </v-btn>
          </v-col>
            <v-row class="px-3">
              <v-col class="py-0" cols="5">
                  <v-checkbox v-model="checked" class="ma-0 pa-0">
                    <template v-slot:label>
                      <span class="text-subtitle-2">로그인 유지</span>
                    </template>
                  </v-checkbox>
              </v-col>
              <v-col class="py-0 text-right text-subtitle-2" cols="7">
                  <router-link to="/user/findpw">
                    비밀번호를 잊으셨습니까?
                  </router-link>
              </v-col>
            </v-row>
        </v-form>
        <v-col class="py-0">
          <div class="text-body-2 grey--text text-weight-medium">SNS 간편 로그인</div>
          <v-divider class="mt-3 mb-5"></v-divider>
          <v-row justify="end">
            <kakaoLogin class="mr-2" :component="component" />
            <GoogleLogin class="mr-4" :component="component" />
          </v-row>
        </v-col>
      </div>
  </div>
</template>

<script>
// import "../../components/css/user.scss";
import PV from "password-validator";
import * as EmailValidator from "email-validator";
import kakaoLogin from "../../components/user/snsLogin/Kakao.vue";
import GoogleLogin from "../../components/user/snsLogin/Google.vue";
import UserApi from "../../api/UserApi";
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "http://localhost:8080";

export default {
  components: {
    kakaoLogin,
    GoogleLogin,
  },
  data: () => {
    return {
      email: "",
      password: "",
      rules: {
        password : value => {
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
      isSubmit: false,
      component: this,
      checked:true
    };
  },
  created() {
    this.component = this;
  },
  methods: {
    // 쿠키를 사용한 로그인 부분
    // 로그인이 성공했을 때 쿠키에 토큰와 userId를 저장한다.
    onLogin() {
      // console.log('로그인의온로그인');
      if(!this.$refs.form.validate()){
        return;
      }
      axios
        .get(
          `${SERVER_URL}/account/login?email=${this.email}&password=${this.password}`
        )
        .then((response) => {
          this.$cookie.set("accesstoken", response.data, 1);
          this.$cookie.set("userId", response.data.object.userId, 1);
          this.$cookie.set("useremail",this.email,1);
          if(this.checked){
            this.$cookie.set("loginSave",true,1);
          }
          this.$router.push("/map");
        })
        .catch((error) => {
          // console.log(error.response);
          alert("이메일과 비밀번호를 확인해주세요.");
        });
    },
  },

};
</script>


<style scoped>


</style>