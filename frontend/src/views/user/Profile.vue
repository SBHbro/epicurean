<template>
  <v-app>
    <!-- Profile -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">프로필</p>
      <v-spacer></v-spacer>
    </v-toolbar>

    <!-- 매너온도/팔로워/팔로잉 -->
    <v-layout>
    <v-toolbar flat> 
      <v-list-item-avatar>
        <v-img :src="this.userImg"></v-img>
      </v-list-item-avatar>
      {{ userNickname }}
      <v-spacer></v-spacer>
      <v-menu offset-y>
        <template v-slot:activator="{ on, attrs }">
        <v-btn
          depressed
          v-bind="attrs"
          v-on="on"
          text 
        >
          관심사
        </v-btn>
      </template>
      <v-list  v-if="realInterests.length > 0"> 
          <v-list-item
          v-for="(interest, index) in realInterests"
          :key="index"
          >
          <v-list-item-title># {{ interest }}</v-list-item-title>
          </v-list-item>
        </v-list>
        <v-list v-else>
          <h4 class="noInterest px-3">
            <!-- <i class="far fa-surprise"></i>
            <span style="margin: 0 1px"></span> -->
            등록된 관심사가 없습니다.
          </h4>
        </v-list>
      </v-menu>
      <v-spacer></v-spacer>
        <v-btn outlined color="orange"  @click="onFollow()" v-if="followerFollowing === 'false'">
            팔로우
        </v-btn>
        <v-btn color="white" depressed  @click="deleteFollowRequest()" v-else-if="followerFollowing === 'doing'">
            요청중
        </v-btn>
        <v-btn color="white" depressed @click="unFollow()" v-else>
            팔로잉
        </v-btn>
      <v-spacer></v-spacer>
      <CreateChat :postuserid="this.anotherId" />
    </v-toolbar>
    </v-layout>
    <v-container>
  <v-layout class="entireClass">
      <v-row>
      <v-col v-for="(lst,i) in postlst" :key="i" class="d-flex child-flex" cols="4">
          <v-card flat tile class="d-flex">
              {{lst.postimgurl}}
              <v-img @click="detailInfo(lst,commentlst[i])"
                :src="`https://don.sbbro.shop/api/img/post?imgname=`+lst.post_img_url"
                aspect-ratio="1" 
                class="grey lighten-2"
              >
              <template v-slot:placeholder>
              <v-row
                  class="fill-height ma-0"
                  align="center"
                  justify="center"
              >
                  <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
              </v-row>
              </template>
              </v-img>
          </v-card>
      </v-col>

      <!-- 피드 없을 때 -->
      <div v-if="postlst.length == 0" class="aligncss"> 
        <i class="far fa-file-image fa-5x"></i>
        <h3 class="mt-5">등록된 게시물이 없습니다.</h3>
      </div>
      </v-row>
  </v-layout>
  </v-container>
  </v-app>
</template>

<script>

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";
import CreateChat from "../../components/common/CreateChat";

import axios from "axios";
export default {
  components: {
    CreateChat,
  },
  data() {
    return {
      anotherId:"",
      userId:"",
      followerFollowing: "",
      followerCount: null,
      followingCount: null,
      userNickname: "",
      userImg:"",
      postlst: [],
      commentlst :[],
      realInterests: [],
      checkedInterests: [],
      dialog: false,
      mynickname:''
      
    }
  },
  methods :{
    detailInfo(post,comment) {
      let users = {
      userId: this.anotherId,
      };
       this.$router.push({name :'PostDetail', query: {post_content: post.post_content
        ,post_id : post.post_id, post_img_url : post.post_img_url,
        post_like : post.post_like, post_userid : post.post_userid, user_img:post.user_img,
        user_nickname : post.user_nickname, comment:comment , userId : this.anotherId}});
      // this.$router.push({name :'PostDetail',query: {post: post, comment : comment, users : users}});
    },
    deleteFollowRequest(){
      this.followerFollowing = 'false'
        //언팔로우 요청
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
    onFollow() {
      this.followerFollowing = 'doing'
      axios
      .post(
        `${SERVER_URL}/userpage/insertfollowingRequest?followerId=`+this.anotherId+`&userId=`+this.userId
      )
      .then((response) => {

        //팔로우 요청이 성공했을때
            //팔로우 알림 보냄
              // console.log(this.mynickname);
               window.db.collection('alarm').doc('follow').collection('messages').add({
                        to : this.anotherId,
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
    unFollow() {
        this.followerFollowing = 'false'
      axios
        .delete(
          `${SERVER_URL}/userpage/deletefollowing?anotherId=`+this.anotherId+`&userId=`+this.userId
        )
        .then((response) => {
        })
        .catch((error) => {
          // console.log(error.response);
        });
    }
  },

  name: "Profile",
  
  created() {

    //유저의 닉네임 가져오기
     axios
      .post(`${SERVER_URL}/chat/nickname`,[this.$cookie.get('userId')])
      .then((response) => {
       this.mynickname = response.data[0];
      })
      .catch((error) => {
        // console.log(error.response);
      });


    this.userId = this.$cookie.get("userId");
    this.anotherId = this.$route.query.userId
    this.userImg = this.$route.query.userImg
    this.followerFollowing = this.$route.query.followerFollowing
    if (this.followerFollowing === 'true') {
      this.followerFollowing = 'true'
    }else if(this.followerFollowing === 'doing'){
      this.followerFollowing = 'doing'  
    }else{
      this.followerFollowing = 'false'
    }
    
    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=`+this.anotherId
      )
      .then((response) => {
        this.followerCount =  response.data.followerCount
        this.followingCount = response.data.followingCount
        this.userNickname = response.data.userNickname
      })
      .catch((error) => {
        // console.log(error.response);
      });

      axios
        .get(`${SERVER_URL}/userpage/getuserpost?userId=`+this.anotherId)
        .then((response) => {
            this.postlst = response.data.data
            this.commentlst = response.data.comment;
            // // console.log(this.postlst)
        })

      // 유저 추가 정보  
      axios 
      .get(`${SERVER_URL}/userpage/getuserInfo?userId=`+this.anotherId)
         .then((response) => {
            var tmp = response.data.userInterest.replace('[','').replace(']',''); 
            var s = "";
            for (let i = 0; i < tmp.length; i++) {
              if(tmp[i] == ','){
                this.checkedInterests.push(s);
                s = "";
              }
              else if(tmp[i] == ' '){
                continue;
              }
              else{
                s += tmp[i];
              }
            }
            this.checkedInterests.push(s);

            for (var i=0; i<this.checkedInterests.length; i++ ) {
              if (this.checkedInterests[i] === "1") {
              this.realInterests.push("아이돌")
              }
              if (this.checkedInterests[i] === "2") {
              this.realInterests.push("콘서트")
              }
              if (this.checkedInterests[i] === "3") {
              this.realInterests.push("취업")
              }
              if (this.checkedInterests[i] === "4") {
              this.realInterests.push("게임")
              }
              if (this.checkedInterests[i] === "5") {
              this.realInterests.push("패션")
              }
              if (this.checkedInterests[i] === "6") {
              this.realInterests.push("다이어트")
              }
              if (this.checkedInterests[i] === "7") {
              this.realInterests.push("재테크")
              }
              if (this.checkedInterests[i] === "8") {
              this.realInterests.push("사업")
              }
              if (this.checkedInterests[i] === "9") {
              this.realInterests.push("경제")
              }
              if (this.checkedInterests[i] === "10") {
              this.realInterests.push("건강")
              }
              if (this.checkedInterests[i] === "11") {
              this.realInterests.push("스포츠")
              }
              if (this.checkedInterests[i] === "12") {
              this.realInterests.push("요리")
              }
              if (this.checkedInterests[i] === "13") {
              this.realInterests.push("군대")
              }
              if (this.checkedInterests[i] === "14") {
              this.realInterests.push("인테리어")
              }
              if (this.checkedInterests[i] === "15") {
              this.realInterests.push("여행")
              }
              if (this.checkedInterests[i] === "16") {
              this.realInterests.push("일러스트")
              }
              if (this.checkedInterests[i] === "17") {
              this.realInterests.push("동물")
              }
              if (this.checkedInterests[i] === "18") {
              this.realInterests.push("IT")
              }
            }
         })
      
  },
  
}

</script>

<style>
  .entire {
  padding: 18px
  }
  .my-auto {
  font-size: 20px;
  }
  .noInterest {
    text-align: center;
  }

</style>