<template>
  <section class="user join">
      <!-- 상단 툴바 -->
      <v-toolbar-title>
        <v-toolbar class="mb-1" dense elevation="1">
          <v-icon @click="$router.go(-1)">
            mdi-arrow-left
          </v-icon>
          <v-spacer></v-spacer>
          <p class="my-auto">댓글</p>
          <v-spacer></v-spacer>
        </v-toolbar>
      </v-toolbar-title>

      <!-- -->
      <div class="pb-10" style="position: relative;">

        <!-- 댓글 작성 창 -->
        <section class="comment">
          <div class="top">
            <!-- 프사 -->
            <span class="prf">
              <img
                style="height: 100%; width: 100%; -webkit-user-drag: none;"
                :src="`https://don.sbbro.shop/api/img/user?imgname=`+this.myimg"
              />
            </span>

            <!-- 댓글 작성창 -->
            <form style="height: 42px;" class="text-box">
              <textarea
                aria-label="here"
                autocomplete="off"
                autocorrect="off"
                data-focus-visible-added
                style="height: 18px;"
                class="text"
                placeholder="write here!"
                v-model="commentText"
                @keyup.enter="onCreate"
                rows="1"
                clearable
              ></textarea>
              <button @click="onCreate()" class="upload">게시</button>
            </form>
          </div>
        </section>

        <!-- 댓글 리스트 -->
        <div class="">
          <!-- <ul style="height: 494px;"> -->
            <!-- 작성자가 피드 컨텐츠 -->
            <v-list>
              <v-list-item>
                <v-list-item-avatar>
                  <img
                    @click="gotoProfile({userid:postuserid, userimg: postuserimg})"
                    style="height: 100%; width: 100%; -webkit-user-drag: none;"
                    :src="`https://don.sbbro.shop/api/img/user?imgname=`+postuserimg"
                  />
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>
                      <a class="black--text font-weight-bold mr-2" @click="gotoProfile({userid:postuserid, userimg : postuserimg})">{{postname}}</a>
                      {{ postcontent }}
                  </v-list-item-title>
                </v-list-item-content>
              </v-list-item>
            </v-list>
            <v-divider class="my-0"></v-divider>


            <!-- 댓글 리스트 -->
            <v-list v-for="(lst, i) in commentlst" :key="i">
              <v-list-item v-if="!recomment || i != index">
                <v-list-item-avatar>
                  <img
                    @click="gotoProfileByComment(lst)"
                    style="height: 100%; width: 100%; -webkit-user-drag: none;"
                    :src="`https://don.sbbro.shop/api/img/user?imgname=`+lst.userimg"
                  />
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>
                      <a class="black--text font-weight-bold mr-2" @click="gotoProfileByComment(lst)">{{ lst.usernickname }}</a>
                      {{lst.postcomment}}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                      <a style="margin-right: 12px; color: #8e8e8e;">
                        {{ lst.commentdate | moment("from", "now") }}
                      </a>
                  </v-list-item-subtitle>
                </v-list-item-content>
                <v-list-item-icon>
                  <v-btn icon v-if="lst.commentuserid  == userid" @click="rewrite(lst,i)">
                    <v-icon small>mdi-pencil</v-icon>
                  </v-btn>
                  <v-btn icon v-if="lst.commentuserid  == userid" @click="onDelete(lst)">
                    <v-icon small>mdi-trash-can-outline</v-icon>
                  </v-btn>
                </v-list-item-icon>
              </v-list-item>

              <v-list-item v-if="recomment && i == index">
                <v-list-item-avatar>
                  <img
                    @click="gotoProfileByComment(lst)"
                    style="height: 100%; width: 100%; -webkit-user-drag: none;"
                    :src="`https://don.sbbro.shop/api/img/user?imgname=`+lst.userimg"
                  />
                </v-list-item-avatar>
                <v-list-item-content>
                  <v-list-item-title>
                      <v-text-field 
                      @keyup.enter="onRewrite" 
                      v-model="commentcontent"
                      autofocus 
                      :value="lst.postcomment"
                      >
                      {{lst.postcomment}}
                      </v-text-field>
                  </v-list-item-title>
                </v-list-item-content>
                <v-list-item-icon>
                  <v-btn icon v-if="lst.commentuserid  == userid" @click="onRewrite()">
                    <v-icon small>mdi-pencil</v-icon>
                  </v-btn>
                </v-list-item-icon>
              </v-list-item>
            
            </v-list>

        </div>
      </div>
  </section>
</template>

<script>
import axios from "axios";
import $ from "jquery";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data() {
    return {
      commentText: "",
      commentlst: [],
      myimg: "",
      postid: "",
      postnickname: "",
      postuserimg: "",
      postcontent: "",
      postname: "",
      recomment: false,
      cid : "",
      postuserid:"",
      userid : "",
      mynickname : '',
      index : "",
    };
  },
  watch: {
    //   commentText: function (v) {
    //   this.Text();
    //   }
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


    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
      )
      .then((response) => {
        this.userid = this.$cookie.get("userId");
        this.username = response.data.userNickname;
        this.myimg = response.data.userImg;
      })
      .catch((error) => {
        // // console.log(error.response);
      });

    axios
      .get(`${SERVER_URL}/post/comment?commentid=${this.$route.query.postid}`)
      .then((response) => {
        this.commentlst = response.data.data;
        this.postname = this.$route.query.postnickname;
        this.postcontent = this.$route.query.postcontent;
        this.postuserimg = this.$route.query.postuserimg;
        this.postuserid = this.$route.query.postuserid;
        // alert(this.postuserid)
      })
      .catch((error) => {
        // // console.log(error.response);
      });
  },
  methods: {
    gotoProfile(user) {
      // 나를 누르면 마이페이지로 이동
      if(user.userid == this.$cookie.get("userId")){
        this.$router.push('/user/info');
      }
      // 타 유저 프로필 페이지로 이동
      else{
        let userImg = `https://don.sbbro.shop/api/img/user?imgname=`+user.userimg;
         this.$router.push('/user/profile?userId='+user.userid
        +'&followerFollowing='+true
        +'&userImg='+userImg);
      }
    },
    gotoProfileByComment(user) {
      // 나를 누르면 마이페이지로 이동
      if(user.commentuserid == this.$cookie.get("userId")){
        this.$router.push('/user/info');
      }
      // 타 유저 프로필 페이지로 이동
      else{
        let flag = "";
        // 댓글을 단 사용자들은 내가 팔로잉 중인 사용자가 무조건 적인게 아닐 수있다.그래서 검사
        axios
        .get(
          `${SERVER_URL}/userpage/getfollowerfollowing?userId=`+this.userId+`&followeruserId=`+user.commentuserid
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
          // // console.log(error.response);
        });
        let userImg = `https://don.sbbro.shop/api/img/user?imgname=`+user.userimg;
         this.$router.push('/user/profile?userId='+user.commentuserid
        +'&followerFollowing='+flag
        +'&userImg='+userImg);
      }
    },
    onDelete(lst) {
      axios
        .delete(`${SERVER_URL}/post/comment?commentid=${lst.commentid}`)
        .then((response) => {
          axios
            .get(
              `${SERVER_URL}/post/comment?commentid=${this.$route.query.postid}`
            )
            .then((response) => {
              this.commentlst = response.data.data;
              this.postname = this.$route.query.postnickname;
              this.postcontent = this.$route.query.postcontent;
              this.postuserimg = this.$route.query.postuserimg;
            })
            .catch((error) => {
              // // console.log(error.response);
            });
        })
        .catch((error) => {});
    },
    onCreate() {
      if(this.commentText.length == 0){
        alert("댓글을 입력해주세요!!")
      }
      else{
        var commentxt = {
        commentuserid: this.$cookie.get("userId"),
        postcomment: this.commentText,
        postid: this.$route.query.postid,
      };
      this.commentText = "";
      axios
        .post(`${SERVER_URL}/post/comment`, commentxt)
        .then((respose) => {
          axios
            .get(
              `${SERVER_URL}/post/comment?commentid=${this.$route.query.postid}`
            )
            .then((response) => {
              this.commentlst = response.data.data;
              this.postname = this.$route.query.postnickname;
              this.postcontent = this.$route.query.postcontent;
              this.postuserimg = this.$route.query.postuserimg;

              //게시한 유저가 자신이 아닐때만
            //좋아요 알림 보냄
            if(this.postuserid != this.$cookie.get('userId')){
              // console.log(this.mynickname);
               window.db.collection('alarm').doc('comment').collection('messages').add({
                        to : this.postuserid,
                        from : this.$cookie.get('userId'),
                        message: this.mynickname+"님이 회원님의 게시글에 댓글을 작성했습니다.",
                        time: Date.now(),
                        postid : this.$route.query.postid,
                        confirm : false
                    }).catch(err => {
                        // console.log(err);
                    });
            }
            })
            .catch((error) => {
              // // console.log(error.response);
            });
        })
        .catch((error) => {});
      }
    },
    rewrite(comment, i) {
      this.recomment = true
      this.cid = comment.commentid
      this.commentcontent = comment.postcomment;
      this.index = i;
    },
    onRewrite() {
      var commentxt = {
        postcomment: this.commentcontent,
        commentid: this.cid

      };
      
      axios
        .put(`${SERVER_URL}/post/comment`, commentxt)
        .then((response) => {
          this.recomment = false
          // alert("성공!");
          axios
            .get(
              `${SERVER_URL}/post/comment?commentid=${this.$route.query.postid}`
            )
            .then((response) => {
              this.commentlst = response.data.data;
              this.postname = this.$route.query.postnickname;
              this.postcontent = this.$route.query.postcontent;
              this.postuserimg = this.$route.query.postuserimg;
            })
            .catch((error) => {
              // // console.log(error.response);
            });
        })
        .catch((error) => {});
    },
    // Text() {
    //     $('.upload').css('color', 'blue')
    // }
  },
};
</script>

<style scoped>
.upload[disabled] {
  opacity: 0.3;
}
.prf-link {
  color: black !important;
  border: 0;
  display: inline;
  position: relative;
  text-decoration: none;
}
.lst-prf {
  margin: 0 18px 0 0;
  display: flex;
  flex-direction: column;
  position: relative;
  padding: 0;
}
.lst-cover {
  display: unset;
  flex-direction: column;
  position: relative;
}
.lst-box {
  flex-direction: row;
  display: flex;
  flex-direction: column;
  position: relative;
}
li {
  padding-bottom: 16px;
  padding-right: 28px;
  /* margin-left: -12px; */
  margin-right: 0;
  padding: 12px 16px;
  width: auto;
  overflow: hidden;
  position: relative;
  margin-top: -5px;
  /* border-bottom: 1px solid rgba(var(--ce3, 239, 239, 239), 1); */
  margin-bottom: 16px;
  display: list-item;
  /* list-style: none; */
}
.lst-div {
  touch-action: manipulation;
  display: flex;
  flex-direction: column;
  flex-shrink: 0;
  position: relative;
  margin: 0;
  padding: 0;
}
.lst {
  flex: 1 1 auto;
  margin-top: -2px;
  overflow-anchor: none;
  display: flex;
  flex-direction: column;
  position: relative;
  margin: 0;
  padding: 0;
  border: 0;
  vertical-align: baseline;
  /* -webkit-box-direction: normal; */
}
.text {
  text-rendering: auto;
  font-size: 14px;
  /* height: 18px; */
  line-height: 18px;
  background: 0 0;
  border: 0;
  color: rgba(var(--i1d, 38, 38, 38), 1);
  display: flex;
  /* -webkit-box-flex: 1; */
  flex-grow: 1;
  max-height: 80px;
  outline: 0;
  padding: 0;
  resize: none;
  width: 0;
  cursor: text;
  white-space: pre-wrap;
  flex-direction: column;
}

.text-box {
  /* -webkit-box-align: center; */
  /* -webkit-box-orient: horizontal; */
  /* -webkit-box-direction: normal; */
  flex-direction: row;
  display: flex;
  /* -webkit-box-flex: 1; */
  flex-grow: 1;
  flex-shrink: 1;
  position: relative;
  align-items: center;
  /* background-color: white; */
  background-color: rgba(var(--d87, 255, 255, 255), 1);
  /* border-bottom-color: rgb(219, 219, 219); */
  border: 1px solid rgba(var(--b6a, 219, 219, 219), 1);
  border-radius: 30px;
  margin-right: 16px;
  /* margin-top: 8px; */
  /* margin-bottom: 8px; */
  padding: 12px 16px;
  border: 0;
  outline: 0;
  /* padding: 0; */
  resize: none;
  width: 0;
  line-height: 18px;
}
.prf {
  width: 32px;
  height: 32px;
  background-color: rgba(var(--b3f, 250, 250, 250), 1);
  border-radius: 50%;
  box-sizing: border-box;
  /* display: block; */
  /* -webkit-box-flex: 0; */
  flex: 0 0 auto;
  overflow: hidden;
  position: relative;
  margin: 0 16px;
  padding: 0;
  border: 0;
}

.comment {
  color: rgba(var(--f52, 142, 142, 142), 1);
  border: 0;
  padding: 0;
  top: -2px;
  width: 100%;
  border-top: 1px solid rgba(var(--ce3, 239, 239, 239), 1);
  flex-shrink: 0;
  font-size: 14px;
  line-height: 18px;
  min-height: 56px;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-align: stretch;
  /* -webkit-box-direction: normal; */
  /* -webkit-box-pack: center; */
  justify-content: center;
  position: relative;
  flex-direction: column;
  align-items: stretch;
  box-sizing: border-box;
}
.top {
  /* background-repeat: no-repeat; */
  height: 60px;
  /* -webkit-box-align: center; */
  /* -webkit-box-orient: horizontal; */
  /* -webkit-box-direction: normal; */
  /* -webkit-box-pack: start; */
  flex-flow: row wrap;
  align-items: center;
  justify-content: flex-start;
  vertical-align: baseline;
  /* background-color: #efefef; */
  background-color: rgba(var(--bb2, 239, 239, 239), 1);
  /* border-bottom: 1px solid #dbdbdb; */
  border-bottom: 1px solid rgba(var(--b6a, 219, 219, 219), 1);
  border-top: 1px solid #dbdbdb;
  border: 0 solid black;
  box-sizing: border-box;
  display: flex;
  flex-shrink: 0;
  margin: 0;
  position: relative;
  padding: 8px 0;
}
</style>