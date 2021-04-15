<template>
  <section class="user join">
    <v-app>
      <v-toolbar-title>
         <!-- 상단 툴바 -->
        <v-toolbar class="mb-1" dense elevation="1">
          <v-icon @click="$router.go(-1)">
            mdi-arrow-left
          </v-icon>
          <v-spacer></v-spacer>
          <p class="my-auto">피드</p>
          <v-spacer></v-spacer>
        </v-toolbar>
      </v-toolbar-title>
      <!-- <p>{{ lst.postid }}</p> -->
      
      <div scroll-target="#scroll-area-1">


      <!-- 피드 -->
     <v-card class="pb-10" tile flat>
        <v-list-item>
          <!-- 유저 프로필 사진-->
          <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
            <img
                :src="`https://don.sbbro.shop/api/img/user?imgname=`+this.postlst.user_img"
              />

          </v-list-item-avatar>
      
        <!-- 유저 닉네임 -->
          <v-list-item-content>
              <v-list-item-title class="font-weight-bold">
                <span style="cursor:pointer;">{{this.postlst.user_nickname}}</span>
              </v-list-item-title>
          </v-list-item-content>

        <!-- 수정/삭제 -->
          <v-list-item-icon class="float-right">
             <v-btn icon small @click="dialog = true" v-show="show">
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </v-list-item-icon>
          </v-list-item>
      
          <v-dialog v-model="dialog" max-width="190">
            <v-list>
              <v-list-item v-for="(item, index) in items" :key="index" @click="doit(item)">
                <v-list-item-title>{{ item.title }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-dialog>


        <!-- 삭제 확인 DIALOG -->
        <v-dialog v-model="dialogDelete" max-width="290" persistent>
          <v-card>
            <v-card-title class="title">정말 삭제하시겠습니까?</v-card-title>
            <v-card-actions class="px-3">
              <v-spacer></v-spacer>
              <v-btn style="width:40%;" color="error" outlined @click="dialogDelete = false, dialog = false">
                <!-- <v-icon>mdi-close</v-icon> -->
                취소
              </v-btn>
              <v-btn style="width:40%;" color="primary" outlined @click="deletePost">
                삭제
                <!-- <v-icon>mdi-check</v-icon> -->
              </v-btn>
              <v-spacer></v-spacer>
            </v-card-actions>
          </v-card>
        </v-dialog>


      <!-- 피드 작성/수정 DIALOG -->
      <v-dialog v-model="feedDialog" scrollable persistent max-width="640">
        <AddFeed :key="reloadKey" @init="init" @closeFeedDialog="closeFeedDialog" :data="repost"></AddFeed>
      </v-dialog> 



        <!-- 피드 이미지 -->
        <v-img
          :src="`https://don.sbbro.shop/api/img/post?imgname=`+postlst.post_img_url"
        ></v-img>

        <!-- 좋아요/댓글 버튼 -->
        <v-card-text class="pt-2 pb-0">
          
          <!-- 좋아요 -->
          <v-btn v-if="likelist.includes(postlst.post_id*1)"
          icon @click="unLike"
          >
            <v-icon color="red">mdi-heart</v-icon>
          </v-btn>
          <v-btn v-else icon @click="onLike">
            <v-icon color="">mdi-heart-outline</v-icon>
          </v-btn>

          <!-- 댓글 -->
          <v-btn icon
            @click="onComment()"
          >
            <v-icon>mdi-chat-processing-outline</v-icon>
          </v-btn>

        </v-card-text>

         <!-- 좋아요 갯수/컨텐츠/댓글보기/시간 -->
          <v-card-text class="pt-0 pb-8">
            <!-- 좋아요 갯수-->
            <div class="font-weight-bold">좋아요 {{ postlst.post_like }}개</div>
          
            <!-- 컨텐츠 -->
            <a class="black--text">
              <span class="mr-2 font-weight-bold">{{postlst.user_nickname}}</span>{{ postlst.post_content }}
            </a>

            <!-- 댓글보기 -->
            <div v-if="commentlst > 0">
              <span class="grey--text font-weight-medium" @click="onComment">  
                댓글 {{commentlst}}개 모두 보기
              </span>
            </div>
          
            <!-- 시간 -->
            <div class="grey--text text-caption">{{ postlst.post_date | moment("from", "now") }}</div>
          </v-card-text>
      </v-card>
      </div>
      
    </v-app>
  </section>
</template>

<script>

import axios from "axios";
import router from "@/routes";
import Vue from "vue";
import vueMoment from "vue-moment";
import AddFeed from "../feed/addFeed";

Vue.use(vueMoment);

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  components: {
    AddFeed
  },
  data() {
    return {
      like: false,
      timestamp: "",
      likelist: [],
      likeornot: "",
      likestate: true,
      postlst: "",
      commentlst: [],
      items: [{ title: "수정" }, { title: "삭제" }],
      dialog: false,
      userId: "",
      userNickname : "",
      userImg : "",
      dialogDelete: false,


      reloadKey : 0,
      show: false,
      feedDialog : false,
      repost : '',
    };
  },
  created() {
        this.userNickname = this.$route.query.user_nickname;
        this.userImg = this.$route.query.user_img;
    // 선택한 피드 객체 생성
      this.postlst = {
        post_date : this.$route.query.post_date,
        post_content: this.$route.query.post_content,
        post_id: this.$route.query.post_id,
        post_img_url: this.$route.query.post_img_url,
        post_like: this.$route.query.post_like,
        post_userid: this.$route.query.post_userid,
        user_img: this.$route.query.user_img,
        user_nickname: this.$route.query.user_nickname,
      };
      // this.init();
      this.timestamp = new Date();
      // // console.log(this.postlst.post_date)

    // 유저가 좋아요한 피드 리스트 가져오기
    axios
      .get(`${SERVER_URL}/post/like/${this.$cookie.get("userId")}`)
      .then((response) => {
        // // console.log(response);
        this.likelist = response.data;
    });

    this.userId = this.$route.query.userId;
    this.commentlst = this.$route.query.comment;
    if (this.userId == this.$cookie.get("userId")) {
      this.show = true;
    }
  },
  methods: {
    init(){
      // console.log("init")
      // 현재 로그인한 유저정보 가져오기
      axios
        .get(
          `${SERVER_URL}/post/${this.postlst.post_id}`
        )
        .then((response) => {
          // console.log(response.data);
            let updatePost = response.data;
            this.postlst = {
              // post_date : updatePost.postdate,
              post_content: updatePost.postcontent,
              post_id: updatePost.postid,
              post_img_url: updatePost.postimgurl,
              post_like: updatePost.postlike,
              post_userid: updatePost.postuserid,
              user_img: this.userImg,
              user_nickname: this.userNickname,
            };
            // console.log(this.postlst);
        })
        .catch((error) => {
          // console.log(error.response);
        });
    },
    // 피드 삭제
    deletePost() {
      axios
          .delete(`${SERVER_URL}/post?postid=` + this.postlst.post_id)
          .then((response) => {
            this.dialog = false;
            router.go(-1);
          });
    },

    // 열린 피드의 DIALOG 닫기
    closeFeedDialog(){
      this.reloadKey++;
      this.dialog = false;
      this.feedDialog = false;
    },
    del(deleteid){
      this.dialog =  true;
      this.delid = deleteid;
    },
    updateLikes(){
      axios
        .get(`${SERVER_URL}/post/like/${this.$cookie.get("userId")}`)
        .then((response) => {
          // console.log(response);
          this.likelist = response.data;
      });
    },
    doit(item) {
      if (item.title == "수정") {
        this.repost = {
          postid: this.postlst.post_id,
          postnickname: this.postlst.user_nickname,
          postcontent: this.postlst.post_content,
          postimgurl: this.postlst.post_img_url,
          userpage: true,
        };
        this.feedDialog = true;
        // router.push({ name: "AddFeed", params: repost });
      } else {
        this.dialogDelete = true
      }
    },
    onComment() {
      let postinfo = {
        postid: this.postlst.post_id,
        postnickname: this.postlst.user_nickname,
        postcontent: this.postlst.post_content,
        postuserimg: this.postlst.user_img,
        postuserid : this.userId
      };
      router.push({ name: "Comment", query: postinfo });
    },
    onLike() {
      this.likestate = !this.likestate;
      // console.log('onlike제발')
      if (this.likelist.includes(this.postlst.post_id)) {
        // console.log("이거나오면안됨");
      } else {
        axios
          .put(
            `${SERVER_URL}/post/like?postid=${
              this.postlst.post_id
            }&userid=${this.$cookie.get("userId")}`
          )
          .then((response) => {
            this.postlst.post_like = response.data;
            this.updateLikes();
            this.like = !this.like;
            this.timestamp = new Date();
            // this.init();
          })
          .catch((error) => {
            // console.log(error);
          });
      }
    },
    unLike() {
      // console.log('unlike제발')
      this.likestate = !this.likestate;
      axios
        .delete(
          `${SERVER_URL}/post/like?postid=${
            this.postlst.post_id
          }&userid=${this.$cookie.get("userId")}`
        )
        .then((response) => {
          this.postlst.post_like = response.data;
          this.timestamp = new Date();
          this.updateLikes();

        })
        .catch((error) => {
          // console.log(error);
        });
    }

  },
};
</script>

<style scoped>

</style>