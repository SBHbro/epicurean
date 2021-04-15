<template>
  <div>
    <!-- 상단 툴바 -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">뉴스피드</p>
      <v-spacer></v-spacer>
    </v-toolbar>

  <div id="scroll-area-1">
    <div class="position:relative;" style="max-width:960px;" scroll-target="#scroll-area-1">

      <!-- 피드 작성/수정 DIALOG -->
      <v-dialog v-model="feedDialog" scrollable persistent max-width="640">
        <AddFeed :key="reloadKey" @init="init" @closeFeedDialog="closeFeedDialog" :data="postlst[index]" :isfromuserpage="false"></AddFeed>
      </v-dialog>
      
      <!-- DIALOG ACTIVATOR-->
      <v-btn
        fixed fab dark small color="green"
        bottom right
        style="bottom:80px;right:20px;"
        @click="openFeedDialog(-1)"
      >
        <v-icon>mdi-pencil-plus</v-icon>
      </v-btn>

      <!-- 피드 -->
      <v-card
       class="mx-auto" tile flat v-for="(lst, i) in postlst" :key="i">
        <v-list-item>
          <!-- 유저 프로필 사진-->
          <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
            <img
              :src="`https://don.sbbro.shop/api/img/user?imgname=`+lst.user_img"
            />
          </v-list-item-avatar>

          <!-- 유저 닉네임 -->
          <v-list-item-content>
              <v-list-item-title class="font-weight-bold">
                <span style="cursor:pointer;" @click="gotoProfile(lst)">{{lst.usernickname}}</span>
              </v-list-item-title>
          </v-list-item-content>


          <!-- 수정/삭제 -->
          <v-list-item-icon class="float-right" v-if="lst.postuserid==userid">

                <!-- 수정 버튼 -->
                <v-btn icon small @click="openFeedDialog(i)">
                  <v-icon>mdi-square-edit-outline</v-icon>
                </v-btn>

                <!-- 삭제 버튼 -->
                <v-btn icon small @click.stop="del(lst.postid)">
                  <v-icon>mdi-trash-can-outline</v-icon>
                </v-btn>

                <!-- 삭제 확인 DIALOG -->
                <v-dialog v-model="dialogDelete" max-width="290" persistent>
                  <v-card>
                    <v-card-title class="title">정말 삭제하시겠습니까?</v-card-title>
                    <v-card-actions class="px-3">
                      <v-spacer></v-spacer>

                        <v-btn style="width:40%;" color="error" outlined @click="dialogDelete=false">
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
            

             </v-list-item-icon>
            </v-list-item>



        <!-- 피드 이미지 -->
        <v-img
          :src="`https://don.sbbro.shop/api/img/post?imgname=`+lst.postimgurl"
          min-height="300" max-height="450" 
        ></v-img>

        <!-- 좋아요/댓글/DM 버튼 -->
        <v-card-text class="pt-2 pb-0">
          
          <!-- 좋아요 -->
          <v-btn v-if="likelist.includes(lst.postid*1)"
          icon @click="unLike(lst, i)"
          >
            <v-icon color="red">mdi-heart</v-icon>
          </v-btn>
          <v-btn v-else icon @click="onLike(lst, i)">
            <v-icon color="">mdi-heart-outline</v-icon>
          </v-btn>


          <!-- 댓글 -->
          <v-btn icon
            @click="onComment(lst.postid, lst.usernickname, lst.postcontent,lst.user_img,lst.postuserid)"
          >
            <v-icon>mdi-chat-processing-outline</v-icon>
          </v-btn>

          <!-- DM -->
          <CreateChat :postuserid="lst.postuserid" />
        </v-card-text>

        <!-- 좋아요 갯수/컨텐츠/댓글보기/시간 -->
        <v-card-text class="pt-0 pb-8">
          <!-- 좋아요 갯수-->
          <div class="font-weight-bold">좋아요 {{lst.postlike}}개</div>

          <!-- 컨텐츠 -->
          <a class="black--text">
            <span class="mr-2 font-weight-bold">{{lst.usernickname}}</span>{{ lst.postcontent }}
          </a>

          <!-- 댓글보기 -->
          <div v-if="commentlst[i][0] > 0">
            <span class="grey--text font-weight-medium" @click="onComment(lst.postid, lst.usernickname, lst.postcontent, lst.user_img, lst.postuserid)">  
              댓글 {{commentlst[i][0]}}개 모두 보기
            </span>
          </div>

          <!-- 시간 -->
          <div class="grey--text text-caption">{{ lst.postdate | moment("from", "now") }}</div>
        </v-card-text>
      </v-card>

          <!-- <div v-if="chat.length+post.length+accept.length == 0" class="text-center" style="margin-top:50%;"> 
      <v-icon size="100" color="grey darken-2">mdi-file-image-outline</v-icon>
      <h3 class="mt-5 font-weight-bold">새로운 알림이 없습니다.</h3>
    </div> -->

      <div v-if="postlst.length==0" class="text-center" style="margin-top:50%;">
        <v-icon size="100" color="grey darken-2">mdi-file-image-outline</v-icon>
        <div class="my-5 h6 font-weight-bold">다른 사람을 팔로우하면 <br>상대방의 피드를 확인할 수 있습니다.</div>
        <router-link to="/SearchUser"><v-btn color="warning" style="width: 60%">유저 보기</v-btn></router-link>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";
import Vue from "vue";
import vueMoment from "vue-moment";
import CreateChat from "../../components/common/CreateChat";
import AddFeed from "./addFeed";


Vue.use(vueMoment);

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  components: {
    CreateChat, AddFeed
  },
  data() {
    return {
      dialogDelete:false,
      show: false,
      postlst: [],
      commentlst: [],
      like: false,
      timestamp: "",
      likelist: [],
      likeornot: "",
      likestate: true,
      items: [{ title: "삭제" }, { title: "취소" }],
      delid : "",
      mynickname : '',
      feedDialog : '',
      index : '',

      reloadKey : 0,
      repost : {},
    };
  },

  mounted() {},
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

    this.init();
    this.timestamp = new Date();
    this.userid = this.$cookie.get("userId");

  },
  methods: {
    // 뉴스피드 리스트 초기화
    init(){
      // 로그인한 유저가 볼 수 있는 피드들 불러오기
      axios
      .get(`${SERVER_URL}/post?userid=${this.$cookie.get("userId")}`)
      .then((response) => {
        var posts = response.data.data;
        var comments = response.data.comment;
        posts.sort((a, b) => {
          return -1 * (a.postid - b.postid);
        });
        comments.sort((a, b) => {
          return -1 * (a[1] - b[1]);
        });
        this.postlst = posts;
        this.commentlst = response.data.comment;
        // // console.log("mentlst : " + response.data.comment);
      })
      .catch((error) => {
        // console.log(error.response);
      });
      
      // 로그인한 유저가 좋아요한 피드들 불러오기
      axios
        .get(`${SERVER_URL}/post/like/${this.$cookie.get("userId")}`)
        .then((response) => {
          this.likelist = response.data;
        });
    },

      // 삭제 확인 모달 띄우기
      del(deleteid){
        this.dialogDelete =  true;
        this.delid = deleteid;
      },
    
      // 게시글 삭제 
      deletePost() {
        axios
          .delete(`${SERVER_URL}/post?postid=${this.delid}`)
          .then((response) => {
            this.dialog = false;
            this.init();
          })
          .catch((error) => {});
          this.dialogDelete=false
        },


    gotoProfile(user) {
      // 나를 누르면 마이페이지로 이동
      if(user.postuserid == this.$cookie.get("userId")){
        this.$router.push('/user/info');
      }
      // 타 유저 프로필 페이지로 이동
      else{
        let userImg = `https://don.sbbro.shop/api/img/user?imgname=`+user.user_img;
         this.$router.push('/user/profile?userId='+user.postuserid
        +'&followerFollowing='+true
        +'&userImg='+userImg);
      }
    },
    onDelete(lst) {
      axios
        .delete(`${SERVER_URL}/post?postid=${lst.postid}`)
        .then((response) => {
          this.init();
        })
        .catch((error) => {});
    },
    onComment(pid, pname, pcontent, puserimg,puserid) {
      let postinfo = {
        postid: pid,
        postnickname: pname,
        postcontent: pcontent,
        postuserimg: puserimg,
        postuserid : puserid
      };
      // query로 넘기고 받는 라우터에서 query로 받아야 뒤로가기했을때 데이터 존재
      router.push({ name: "Comment", query: postinfo });
    },
    onLike(postlike, idx) {
      this.likestate = !this.likestate;
      if (this.postlst[idx].postid in this.likelist) {
        // // console.log("이거나오면안됨");
      } else {
        axios
          .put(`${SERVER_URL}/post/like?postid=${this.postlst[idx].postid}&userid=${this.$cookie.get("userId")}`)
          .then((response) => {
            //게시한 유저가 자신이 아닐때만
            //좋아요 알림 보냄
            if(this.postlst[idx].postuserid != this.$cookie.get('userId')){
              // console.log(this.mynickname);
               window.db.collection('alarm').doc('like').collection('messages').add({
                        to : this.postlst[idx].postuserid,
                        from : this.$cookie.get('userId'),
                        message: this.mynickname+"님이 회원님의 게시글에 좋아요를 눌렀습니다..",
                        time: Date.now(),
                        postid : this.postlst[idx].postid,
                        confirm : false
                    }).catch(err => {
                        // console.log(err);
                    });
            }
            this.like = !this.like;
            this.timestamp = new Date();
            this.init();
          })
          .catch((error) => {
            // console.log(error);
          });
      }
    },
    unLike(list, idx) {
      this.likestate = !this.likestate;
      axios
        .delete(`${SERVER_URL}/post/like?postid=${this.postlst[idx].postid}&userid=${this.$cookie.get("userId")}`)
        .then((response) => {
          this.timestamp = new Date();
          this.init();
        })
        .catch((error) => {
          // console.log(error);
        });
    },
    // 해당 index의 피드에 대한 DIALOG OPEN
    openFeedDialog(index){
        this.reloadKey++;
        this.index = index;
        this.feedDialog = true;
    },
    // 열린 피드의 DIALOG 닫기
    closeFeedDialog(){
      this.feedDialog = false;
    }
  },
};
</script>

<style scoped>
</style>