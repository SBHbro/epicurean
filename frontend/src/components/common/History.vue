<template>
  <v-app>

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

    <!-- <div v-show="!listLen" style="margin-top:40%;text-align:center;"> 
      <i class="fab fa-meetup fa-6x"></i>
      <h3 class="mt-5 font-weight-bold">등록된 Meetup이 없습니다.</h3>
    </div> -->

      </v-row>

    
  </v-layout>
    <div v-if="postlst.length == 0" class="text-center" style="margin-top:40%;"> 
      <v-icon size="80" color="grey darken-2">mdi-file-image-outline</v-icon>
      <div class="mt-5 h6 font-weight-bold">등록된 게시물이 없습니다.</div>
    </div>
  </v-container>
  </v-app>
</template>


<script>
import axios from "axios";
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

  export default {
    props:['userId'],
    data () {
      return {
        items: [],
        postlst: [],
        commentlst :[]
      }
    },
    methods:{
      detailInfo(post,comment) {
        // // console.log(post)
        this.$router.push({name :'PostDetail', query: {post_content: post.post_content
        ,post_id : post.post_id, post_img_url : post.post_img_url,
        post_like : post.post_like, post_userid : post.post_userid, user_img:post.user_img,
        user_nickname : post.user_nickname, comment:comment , userId : this.userId, post_date: post.post_date}});
      },
    },
    created(){
      axios
        .get(`${SERVER_URL}/userpage/getuserpost?userId=`+this.userId)
        .then((response) => {
            this.postlst = response.data.data
            this.commentlst = response.data.comment;
        })  
    }

  }
</script>

<style scoped>
  /* .entireClass{
    padding: 20px;
  } */
  .aligncss {
    margin: 100px 0 0 0;
    color: rgba(0,0,0,.6);
    width: 100%;
    text-align: center;
  }
</style>