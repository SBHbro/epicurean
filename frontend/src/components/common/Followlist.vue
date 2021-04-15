<template>
  <v-app>
    <!-- 상단 -->
     <v-toolbar-title>
      <Top></Top>
    </v-toolbar-title>
    <!-- 가운데 부분 -->
    <v-sheet  id="scrolling-techniques"
      class="overflow-y-auto mb-12"
      >
      <!-- tab view -->
      <v-tabs fixed-tabs v-model="currentItem" slider-color="orange">
      <v-tab class="follow-list" v-for="item in items" :key="item" :href="'#tab-' + item">
        <v-icon class="followListBtn" v-if="item=='follower'">팔로워</v-icon>
        <v-icon class="followListBtn" v-if="item=='following'">팔로잉</v-icon>
      </v-tab>
    </v-tabs>

    <v-tabs-items v-model="currentItem">
      <v-tab-item v-for="item in items" :key="item" :value="'tab-' + item">
        <v-card class="text-center" flat min-height="600">
          
        <!-- 팔로워 -->
        <v-list nav dense v-if="item=='follower'">
        <!-- 팔로워 존재하면  -->
          <!-- 팔로워 검색바 -->
          <v-row>
            <v-col class="search-bar">
            <v-text-field 
              @keyup="onSearchFollower"
              v-model="searchFollower"
              label="검색"
              :append-icon="'mdi-account-search-outline'"
              hide-details
              solo
            ></v-text-field>             
            </v-col>
          </v-row>

            <v-list-item class="followlist" v-for="(user, i) in followerList" :key="i">
                <!-- 사진 표기 -->
                <v-list-item-avatar @click="gotoProfile(user)">
                  <v-img
                    :src="user.followerImg">
                  </v-img>
                </v-list-item-avatar>
                
                <v-list-item-content>
                <a
                class="pf-n-a"
                tabindex="0"
                style="color: black; font-weight: 600;" 
                @click="gotoProfile(user)"
                >
                  {{user.followerNickname}}
                </a>
                <!-- 이름 표기 -->
                <!-- <v-list-item-title v-text="user.followingName"></v-list-item-title> -->
                </v-list-item-content>

                <v-btn outlined color="orange"  @click="onFollow(user,i)" v-if="user.followerFollowing === 'false'">
                    팔로우
                </v-btn>
                
                <v-btn color="white" depressed  @click="deleteFollowRequest(user,i)" v-else-if="user.followerFollowing === 'doing'">
                    요청중
                </v-btn>

                <v-btn outlined color="grey" depressed @click="unFollow(user,i,'followerlist')" v-else>
                    팔로잉
                </v-btn>
            </v-list-item>
        <!-- 팔로워 존재하지 않으면 -->
        <div style="margin-top:30%;" v-if="followerList.length == 0">
          <v-icon class="ml-n6" size="100" color="grey darken-2">mdi-account-plus</v-icon>
          <div class="">
            <h2 class="font-weight-bold">팔로워</h2>
            <p>회원님을 팔로우하는 모든 사람이 표시됩니다.</p>
          </div>
        </div>
        </v-list>

        <!-- 팔로잉 -->
        <v-list nav dense v-else min-height="600">
        <!-- 팔로잉 존재하면  -->
          <!-- 팔로잉 검색바 -->
          <v-row>
            <v-col class="search-bar">
              <v-text-field 
                @keyup="onSearchFollowing"
                v-model="searchFollowing"
                label="검색"
                :append-icon="'mdi-account-search-outline'"
                hide-details
                solo
              ></v-text-field>             
            </v-col>
          </v-row>

        <v-list-item class="followlist" v-for="(user, i) in followingList" :key="i">
            <!-- 사진 표기 -->
            <v-list-item-avatar @click="gotoProfile(user)">
              <v-img
                :src="user.followingImg">
              </v-img>
            </v-list-item-avatar>

            <v-list-item-content>
            <a
            class="pf-n-a"
            tabindex="0"
            style="color: black; font-weight: 600;" 
            @click="gotoProfile(user)"
            >
              {{user.followingNickname}}
            </a>
            </v-list-item-content>

            <v-btn outlined color="grey" depressed @click="unFollow(user,i,'followinglist')">
                팔로잉
            </v-btn>
          </v-list-item>

          <div v-if="followingList.length == 0" style="margin-top:30%;">
            <v-icon class="ml-n6" size="100" color="grey darken-2">mdi-account-plus</v-icon>
            <div class="">
              <h2 class="font-weight-bold">팔로잉</h2>
              <p>회원님이 팔로우하는 모든 사람이 표시됩니다.</p>
            </div>
          </div>
          </v-list>
        </v-card>
      </v-tab-item>
    </v-tabs-items>

    </v-sheet>
  </v-app>
</template>

<script>
import Top from "../common/Top";

import axios from "axios";
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";
export default {
  name: "components",
  components: {
     Top,
  },
 
  data: () => {
    return {
        anotherId:"",
        nicknameForProfile: "",
        searchFollower: "",
        searchFollowing:"",
        userId: "",
        currentItem: 'tab-Web',
        items: [
            'follower', 'following'
        ],
        text: '',
        followingList:[],
        followerList:[],
        mynickname :'',
        methods: {
          checkfollow(flag){
              if(flag=="true")
                  return true;
              else
                  return false;
          }
        },
    };
  },
  created(){
    //유저의 닉네임 가져오기
     axios
      .post(`${SERVER_URL}/chat/nickname`,[this.$cookie.get('userId')])
      .then((response) => {
       this.mynickname = response.data[0];
      })
      .catch((error) => {
        // console.log(error.response);
      });

    this.whenCreated(); 
  },
  methods: {
    whenCreated() {
      this.followingList = []
      this.followerList = []
      if (this.$route.params.info == 'follow') {
      this.currentItem = 'tab-following'
      }

     this.userId = this.$cookie.get("userId");
     axios
      .get(
        `${SERVER_URL}/userpage/getfollowinglist?userId=`+this.userId+`&searchName=`+this.searchFollowing
      )
      .then((response) => {
        for (let i = 0; i <  response.data.length; i++) {
          let userImg = response.data[i].followingImg;
          let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
          this.followingList.push({
              followingId : response.data[i].followingId,
              followingNickname : response.data[i].followingNickname,
              followingImg: viewImg,
              followerFollowing :  "true"
          })
        }
      })
      .catch((error) => {
        // console.log(error.response);
      });
      axios
      .get(
        `${SERVER_URL}/userpage/getfollowerlist?userId=`+this.userId+`&searchName=`+this.searchFollower
      )
      .then((response) => {
        for (let i = 0; i <  response.data.length; i++) {
          let userImg = response.data[i].followerImg;
          let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
          this.followerList.push({
              followerId : response.data[i].followerId,
              followerNickname : response.data[i].followerNickname,
              followerImg: viewImg,
              followerFollowing :  response.data[i].followerFollowing
          })
        }
      })
      .catch((error) => {
        // console.log(error.response);
      });    
    },
    gotoProfile(user) {
      if (user.followerId) {
        let profileInfo = {
          userId: user.followerId,
          userImg : user.followerImg,
          followerFollowing: user.followerFollowing
        };
        this.$router.push('/user/profile?userId='+user.followerId
        +'&followerFollowing='+user.followerFollowing
        +'&userImg='+user.followerImg);
      } else { 
        let profileInfo = {
        userId: user.followingId,
        userImg : user.followingImg,
        followerFollowing: user.followerFollowing
        };
        this.$router.push('/user/profile?userId='+user.followingId
        +'&followerFollowing='+user.followerFollowing
        +'&userImg='+user.followingImg);
      }
    },
    onSearchFollower () {
      axios
      .get(
        `${SERVER_URL}/userpage/getfollowerlist?userId=`+this.userId+`&searchName=`+this.searchFollower
      )
      .then((response) => {
        this.followerList = []
        for (let i = 0; i <  response.data.length; i++) {
          let userImg = response.data[i].followerImg;
          let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
          this.followerList.push({
              followerId : response.data[i].followerId,
              followerNickname : response.data[i].followerNickname,
              followerImg: viewImg,
              followerFollowing :  response.data[i].followerFollowing
          })
        }
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },

    onSearchFollowing () {
      axios
      .get(
        `${SERVER_URL}/userpage/getfollowinglist?userId=`+this.userId+`&searchName=`+this.searchFollowing
      )
      .then((response) => {
        this.followingList = []
        for (let i = 0; i <  response.data.length; i++) {
            let userImg = response.data[i].followingImg;
            let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
            this.followingList.push({
                followingId : response.data[i].followingId,
                followingNickname : response.data[i].followingNickname,
                followingImg: viewImg,
                followerFollowing :  "true"
            })
          }
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },

    deleteFollowRequest(user,idx){
      this.$set(this.followerList[idx], 'followerFollowing', 'false');
        //언팔로우 요청
        if (user.followerId) {
          this.anotherId = user.followerId
        } else {
          this.anotherId = user.followingId
        }
        axios
          .delete(
            `${SERVER_URL}/userpage/deletefollowingRequest?anotherId=`+this.anotherId+`&userId=`+this.userId
          )
          .then((response) => {
          })
          .catch((error) => {
            // console.log(error.response);
          });
    },
    onFollow(user,idx) {
      this.$set(this.followerList[idx], 'followerFollowing', 'doing');
      axios
      .post(
        `${SERVER_URL}/userpage/insertfollowingRequest?followerId=`+user.followerId+`&userId=`+this.userId
      )
      .then((response) => {
           //팔로우 요청이 성공했을때
            //팔로우 알림 보냄
              // console.log(this.mynickname);
               window.db.collection('alarm').doc('follow').collection('messages').add({
                        to : user.followerId,
                        from : this.$cookie.get('userId'),
                        message: this.mynickname+"님이 회원님에게 팔로우 요청을 하였습니다.",
                        time: Date.now(),
                        confirm : false
                    }).catch(err => {
                    });
      })
      .catch((error) => {
          // console.log(error.response);
      });
    },
    unFollow(user,idx,str) {
      if(str == 'followerlist'){
        this.$set(this.followerList[idx], 'followerFollowing', 'false');
      }
      else{
        this.$delete(this.followingList, idx);
      }
      //언팔로우 요청
      if (user.followerId) {
        this.anotherId = user.followerId
      } else {
        this.anotherId = user.followingId
      }
      axios
        .delete(
          `${SERVER_URL}/userpage/deletefollowing?anotherId=`+this.anotherId+`&userId=`+this.userId
        )
        .then((response) => {
          if(str == 'followerlist'){
            this.followingList = []
            axios
              .get(
                `${SERVER_URL}/userpage/getfollowinglist?userId=`+this.userId+`&searchName=`+this.searchFollowing
              )
              .then((response) => {
                for (let i = 0; i <  response.data.length; i++) {
                  let userImg = response.data[i].followingImg;
                  let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
                  this.followingList.push({
                      followingId : response.data[i].followingId,
                      followingNickname : response.data[i].followingNickname,
                      followingImg: viewImg,
                      followerFollowing :  "true"
                  })
                }
              })
              .catch((error) => {
                // console.log(error.response);
              });
          }
          else{
            this.followerList = []
            axios
            .get(
              `${SERVER_URL}/userpage/getfollowerlist?userId=`+this.userId+`&searchName=`+this.searchFollower
            )
            .then((response) => {
              for (let i = 0; i <  response.data.length; i++) {
                let userImg = response.data[i].followerImg;
                let viewImg = SERVER_URL+"/img/user?imgname=" + userImg;
                this.followerList.push({
                    followerId : response.data[i].followerId,
                    followerNickname : response.data[i].followerNickname,
                    followerImg: viewImg,
                    followerFollowing :  response.data[i].followerFollowing
                })
              }
            })
            .catch((error) => {
              // console.log(error.response);
            });  
        }
        })
        .catch((error) => {
          // console.log(error.response);
        });
    }
    
  }
};
</script>

<style scoped>
  .follow-lsit {
    width: 100px
  }

  .search-bar {
    padding: 0 12px 0 12px;
    margin: 10px 18px 10px 18px;
  }


  .followListBtn {
    font-style: normal;
    font-size: 15px !important;
    font-family: 'Jua', sans-serif;  
  }

   .followListBtn::after {
    font-style: normal;
    font-size: 15px !important;
    color: orange;
    font-family: 'Jua', sans-serif;  
  }


  .followlist{
    margin: 18px;
  }
  .v-list-item__content {
    width: 12px;
  }
  .text-css {
    color: rgba(0,0,0,.6);
    text-align: center;
  }
  /* .fa-user-plus {
    color: rgba(0,0,0,.6);
    margin: 70px 0 20px 140px;
    align-items: center;
  }
   */
  .v-application a{
    color: orange ;
  }
  .pf-n-a{
    color: rgb(97, 94, 94) !important;
  }
</style>