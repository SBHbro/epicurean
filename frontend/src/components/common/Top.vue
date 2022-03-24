<template>
  <v-layout>
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">마이페이지</p>
      <v-spacer></v-spacer>
      <v-btn color="white" @click.stop="drawer = !drawer" depressed>
        <v-app-bar-nav-icon></v-app-bar-nav-icon>
      </v-btn>
    </v-toolbar>
    
    <!-- 햄버거? 눌렀을 때 -->
    <v-navigation-drawer v-model="drawer" app right>
      <v-system-bar></v-system-bar>
      <v-list>
        <v-list-item>
          <v-list-item-content>
            <v-list-item-title class="title">{{ user.userNickname }}</v-list-item-title><br>
            <v-list-item-subtitle>{{ user.userEmail }}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
          <v-btn color="warning find-btn" @click="updateUser" block>
            프로필 수정
          </v-btn>
      </v-list>

      <v-divider></v-divider>

      <v-list nav dense>
        <v-list-item-group>
          <v-list-item @click="SearchUser">
            <v-list-item-icon>
              <v-icon>mdi-account-search-outline</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>유저검색</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="followRequestList">
            <v-list-item-icon>
              <v-badge v-model="show" color="indigo">
                <span slot="badge">{{user.followingRequestCount}}</span>
                <v-icon>mdi-account-plus</v-icon>
              </v-badge>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>팔로우요청</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

           <v-list-item @click="meetupRequestList">
            <v-list-item-icon>
                <v-icon>mdi-account-plus</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>밋업요청</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="chatRoom">
            <v-list-item-icon>
              <v-icon>far fa-paper-plane</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>Message</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="updateUserInfo">
            <v-list-item-icon>
              <v-icon>mdi-account-box</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>추가정보 설정</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item @click="logout">
            <v-list-item-icon>
              <v-icon>mdi-logout</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title>로그아웃</v-list-item-title>
            </v-list-item-content>
          </v-list-item>
        </v-list-item-group>
      </v-list>

    </v-navigation-drawer>
  </v-layout>
</template>

<script>
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";
export default {
  data () {
    return {
      user:{},
      drawer: null,
      show: false,
      badgeData: { value: '!' },
      followCount :"",
    }
  },
  computed: {
  },
  methods:{
    chatRoom(){
      this.$router.push('/chatroom');
    },
    SearchUser(){
      this.$router.push('/SearchUser');  
    },
    followRequestList(){
      this.$router.push('/user/FollowRequestList');
    },
    meetupRequestList(){
      this.$router.push('/meetup/add');
    },
    updateUser(){
      this.$router.push('/user/updateUser');
    },
    updateUserInfo(){
      this.$router.push('/user/updateUserInfo');
    },
    logout (){
      this.$confirm("로그아웃 하시겠습니까?").then(() => {
      //do something...
      this.$cookie.delete('accesstoken');
      this.$cookie.delete('userId');
      this.$cookie.delete('loginSave');
      this.$router.push('/');
    });
    }
  },
  created() {
    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
      )
      .then((res) => {
        this.user = res.data;
        if(this.user.followingRequestCount != 0){
          this.show = true;
        }
      })
  }
};
</script>
<style>
  .fa-chevron-left {
    color: white; 
    margin-left: 7px;
  }
</style>