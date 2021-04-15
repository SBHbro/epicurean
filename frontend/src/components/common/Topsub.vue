<template>
  <v-layout>
    <v-toolbar dense flat>
      <v-list-item-avatar>
        <v-img :src="'https://don.sbbro.shop/api/img/user?imgname='+proptoTopsub.userImg"></v-img>
      </v-list-item-avatar>

        {{proptoTopsub.userNickname}}

      <!-- <v-btn><br>매너온도</v-btn> -->
      <v-spacer></v-spacer>
        <v-btn color="white" @click="getFollowers" depressed>{{proptoTopsub.followerCount}}<br>팔로워</v-btn>
      <v-spacer></v-spacer>
        <v-btn color="white" @click="getFollowings" depressed>{{proptoTopsub.followingCount}} <br>팔로잉</v-btn>
      <v-spacer></v-spacer>
    </v-toolbar>
  </v-layout>
</template>
<script>
// import Following from "../../views/Following";
import router from "@/routes";
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";

export default {
  props:{
    proptoTopsub: {
      type : Object
    },
  },
  // data() {
  //   return {
  //     UserImg : "",
  //   }
  // },
  methods: {
    getFollowers () {
      this.$router.push('/followlist')
    },
    getFollowings () {
      let userInfo = {
              info: "follow",
            };
      this.$router.push({name :'FollowList', params: userInfo});
    },
  },
}
</script>

<style>

</style>s