<template>
  <v-app>
     <v-toolbar-title >
      <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">밋업 참가요청</p>
      <v-spacer></v-spacer>
    </v-toolbar>
    </v-toolbar-title>
    
    
    <div v-if="guestList.length > 0">
    <v-list subheader>
      <v-list-item
        v-for="(guest,i) in guestList"
        :key="i"
      >
        <v-list-item-avatar @click="showUser(guest)">
          <v-img
            :src="'https://don.sbbro.shop/api/img/user?imgname='+guest.userImg">
          </v-img>
        </v-list-item-avatar>

        <v-list-item-content @click="showUser(guest)">
          <a
          class="pf-n-a"
          tabindex="0"
          style="color: black; font-weight: 600;" 
          >
            {{guest.userNickname}}
          </a>
        </v-list-item-content>

        <v-list-item-icon >
          <v-btn outlined color="orange" @click="showRequest(i)">
            요청보기
          </v-btn>
        </v-list-item-icon>
       
      </v-list-item>
    </v-list>
    </div>

    <div v-else>
      <h3>밋업 참가 요청이 없습니다.</h3>
    </div>
    <v-dialog
      v-model="dialog"
      >
      <v-card>
        <div class="d-flex">
          <v-avatar
          class="ma-3"
          size="85"
          tile
          >
            <v-img :src="meetupInfo.img"></v-img>
          </v-avatar>
          <div>
            <v-card-title
            class="headline"
            v-text="meetupInfo.title"
            ></v-card-title>
            <v-card-subtitle>{{this.meetupInfo.location}}<br>{{this.meetupDate}}</v-card-subtitle>
          </div>
        </div>
        <v-list-item>
          <v-list-item-avatar>
            <v-img
                :src="'https://don.sbbro.shop/api/img/user?imgname='+this.userImg">
              </v-img>
          </v-list-item-avatar>
          <v-list-item-content>
            <v-list-item-title class="headline">{{this.userNickname}}</v-list-item-title>
            <v-list-item-subtitle style="text-color='orange'">{{this.requestMessage}}</v-list-item-subtitle>
          </v-list-item-content>
        </v-list-item>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn outlined color="info" @click="acceptRequest()">
            수락
          </v-btn>
          <v-btn outlined color="error" @click="deleteRequest()">
            거절
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-app>
</template>

<script>

import axios from "axios";
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";
export default {
  name: "components",
 
  data: () => {
    return {
      userId :"",
      requestList: [],
      guestList:[],
      dialog : false,
      meetupInfo:[],
      index : "",
      userImg : "",
      userNickname : "",
      requestMessage : "",
      meetupDate : "",
    };
  },
  methods :{
    acceptRequest(){
      var userid = this.guestList[this.index].userId;
      axios
      .get(
        `${SERVER_URL}/meetup/acceptMeetupRequest?meetupId=${this.meetupInfo.id}&guestId=${this.guestList[this.index].userId}&requestId=${this.requestList[this.index].id}`
      )
      .then((response) => {
        if(response.data.data == "success"){

            // 유저 밋업에 추가할 때 사용할 코드 삭제하지 말 것
    
          const newRoomRef = window.db.collection('test').where('name','==', this.meetupInfo.title).get()
          .then(snapshot =>{
            if(snapshot.empty){
              // alert("없다");
            }
            snapshot.forEach(doc=>{
              var id = doc.data().id;
              if(!id.includes(userid)){
                id.push(userid);
                id.sort()
              }
              window.db.collection('test').doc(doc.id).set({
                id : id
              },{merge:true});
            })
          })

          alert("수락완료");
          this.dialog = false
          this.created();
        }
        else
          alert("수락실패");
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
    deleteRequest(){
      axios
      .delete(
        `${SERVER_URL}/meetup/request/${this.requestList[this.index].id}`
      )
      .then((response) => {
        if(response.data.data == "success"){
          alert("거절완료");
          this.dialog = false
          this.created();
        }
        else
          alert("거절실패");
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
    showRequest(idx){
      this.index = idx;
      this.dialog = true;
      this.userImg = this.guestList[idx].userImg
      this.userNickname = this.guestList[idx].userNickname
      this.requestMessage = this.requestList[idx].requestMessage
      axios
      .get(`${SERVER_URL}/meetup/search/${this.requestList[idx].meetupId}`)
      .then((response) => {
        this.meetupInfo = response.data;
        this.meetupDate = this.meetupInfo.date.slice(0, 16)
      })
    },
    showUser(user){
      let flag = "";
      axios
        .get(
          `${SERVER_URL}/userpage/getfollowerfollowing?userId=`+this.userId+`&followeruserId=`+user.userId
        )
        .then((response) => {
          if(response.data == "true"){
            flag = "true"
          }else if(response.data == "false"){
            flag = "false"
          }else{
            flag = "doing" 
          }
        })
        .catch((error) => {
          // console.log(error.response);
        });
        let userImg = `https://don.sbbro.shop/api/img/user?imgname=`+user.userImg;
         this.$router.push('/user/profile?userId='+user.userId
        +'&followerFollowing='+flag
        +'&userImg='+userImg);
    },
    created(){
      this.userId = this.$cookie.get("userId");
      this.guestList = []
    axios
      .get(
        `${SERVER_URL}/meetup/request?userId=${this.userId}`
      )
      .then((response) => {
        if(response.data.data == "success"){
          this.requestList = response.data.object
          for (let i = 0; i < this.requestList.length; i++) {
            axios
            .get(
              `${SERVER_URL}/userpage/getuser?userId=`+ this.requestList[i].guestId
            )
            .then((response) => {
              this.guestList.push(response.data)
            })
            .catch((error) => {
              // console.log(error.response);
            });
          }
        }
      })
      .catch((error) => {
        // console.log(error.response);
      });
  },
},
   created(){
     this.created();
   }
};
</script>

<style scoped>
  .follow-lsit {
    width: 100px
  }

  h3 {
    margin: 60px;
    text-align: center;
  }
</style>