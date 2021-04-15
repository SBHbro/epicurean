<template>
            <v-card flat tile>
              <!-- 피드 작성 상단 툴바 -->
              <v-toolbar dense elevation="1">
                <v-icon @click="close">
                  mdi-arrow-left
                </v-icon>
                <v-spacer></v-spacer>
                <p class="my-auto">피드 작성</p>
                <v-spacer></v-spacer>
              </v-toolbar>
              

              <!-- 취소 확인 DIALOG -->
              <v-dialog v-model="dialog" max-width="290" persistent>
                <v-card>
                  <v-card-title class="title">작성을 취소하시겠어요?</v-card-title>
                  <v-card-actions class="px-3">
                    <v-spacer></v-spacer>
                    <v-btn style="width:40%;" color="error" outlined @click="dialog = false">
                      <v-icon>mdi-close</v-icon>
                    </v-btn>
                    <v-btn style="width:40%;" color="primary" outlined @click="close">
                      <v-icon>mdi-check</v-icon>
                    </v-btn>
                    <v-spacer></v-spacer>
                  </v-card-actions>
                </v-card>
              </v-dialog>

              <!-- 사용자 정보 -->
              <v-card-title class="pa-1">
              <v-list>
                <v-list-item>
                  <v-list-item-avatar>
                    <img :src="`https://don.sbbro.shop/api/img/user?imgname=`+ userimg"/>
                  </v-list-item-avatar>
                    <v-list-item-content>
                      <v-list-item-title><strong>{{username}}</strong></v-list-item-title>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-card-title>

              <!-- 피드 작성부분 -->
              <v-card-text style="height:450;" class="pa-1">
                <v-list>
                  <input ref="imageInput" type="file" hidden @change="onChangeImages" />
                  
                  <!-- 피드 글 -->
                  <v-list-item>
                    <v-textarea
                      v-model = "content"
                      prepend-inner-icon="mdi-paperclip"
                      @click:prepend-inner="onClickImageUpload"
                      rows="2" class="mx-2" 
                      placeholder="피드 내용을 작성해보세요 :)"
                      clearable
                    ></v-textarea>

                    <!-- <v-textarea
                      v-if="!revise&&!userpage1"
                      v-model="postcontent"
                      placeholder="피드 내용을 작성해보세요 :)"
                      clearable
                    ></v-textarea> -->
                  </v-list-item>

                  <!-- 피드 이미지 -->
                  <v-list-item>
                    <v-list-item-content>

                      <!-- 이미지 삭제 버튼 -->
                      <v-btn x-small dark fab absolute top right color="black" style="top:20px;right:20px;opacity:.7;"
                        @click="test"
                      >
                        <v-icon dark>mdi-close</v-icon>
                      </v-btn>

                      <!-- 이미지 -->
                      <img
                        v-show="showImg"
                        ref="img"
                        style="width: 100%;border: 1px solid; border-radius: 10px;"
                        :src="imgPath+img"
                        alt="테스트"
                      />
                      <!-- <img
                        v-show="showImg"
                        ref="img"
                        id="imageview"
                        style="width: 100%;border: 1px solid; border-radius: 10px;"
                        v-if="userpage1&&userpage2"
                        :src="`https://don.sbbro.shop/api/img/post?imgname=`+postimg"
                      />
                      <img
                        v-show="showImg"
                        ref="img"
                        id="imageview"
                        style="width: 100%;border: 1px solid; border-radius: 10px;"
                        v-else-if="revise && revise3"
                        :src="`https://don.sbbro.shop/api/img/post?imgname=`+postimg"
                      />
                      <img 
                        v-show="showImg"
                      ref="img" id="imageview" style="width: 100%" v-if="revise1" :src="postimg" />
                      <v-img 
                        v-show="showImg"
                      v-else ref="img" :src="postimgurl"></v-img> -->
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
              </v-card-text>

                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn text @click="dialog=true">취소</v-btn>
                  <v-btn text color="green" @click="uploadFeed">확인</v-btn>
                </v-card-actions>
          </v-card>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  props:{
    data : Object,
  },
  data() {
    return {
      postuserid: this.$cookie.get("userId"),

      username: "",
      userimg: "",

      uploadimg: false,
      dialog: false,
      
      showImg : true,
      content : '',
      imgPath : 'https://don.sbbro.shop/api/img/post?imgname=',
      img : 'no_image.jpg',
    };
  },
  created () {
    // console.log(this.data);
    if(this.data){
      this.content = this.data.postcontent,
      this.img = this.data.postimgurl
    }
    
    // 현재 로그인한 유저정보 가져오기
    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
      )
      .then((response) => {
        this.username = response.data.userNickname;
        this.userimg = response.data.userImg;
      })
      .catch((error) => {
        // console.log(error.response);
      });

  },

  methods: {
    test(){
      this.showImg = false;
      this.postimgurl = null;
      this.postimg = 'no_image.jpg';
    },

    // 피드 작성창 닫을 지 확인하는 메소드
    close() {
      this.$emit('closeFeedDialog');
      this.showImg = false;
    },

    modifyFeed(newPost){
      // console.log("수정")
      // console.log(newPost);
      axios
        .put(`${SERVER_URL}/post`, newPost)
        .then((response) => {
          // console.log(response);
          this.close();
          this.$emit('init');
        })
        .catch((error) => {
          // console.log(error.response);
          alert("이미지 전송 실패");
        });
    },
    insertFeed(newPost){
      // console.log("등록")
      // console.log(newPost);
      axios
        .post(`${SERVER_URL}/post`, newPost)
        .then((response) => {
          // console.log(response);
          this.close();
          this.$emit('init');
        })
        .catch((error) => {
          // console.log(error.response);
          alert("이미지 전송 실패");
        });
    },
    uploadFeed() {

      var newPost = {
        postuserid : this.$cookie.get("userId"),
        postcontent: this.content,
        postid: this.data? this.data.postid : '',
        postimgurl: this.imgPath + this.img,
      };

      // console.log(newPost)

      // 이미지를 안바꿨을 때
      if (!this.uploadimg) {
        if(this.data)
          this.modifyFeed(newPost);
        else
          this.insertFeed(newPost);
      }
      
      // 이미지 바꿨을 때
      else {
        const file = new FormData();
        file.append("file", this.file);
        axios
          .post(`${SERVER_URL}/post/img`, file)
          .then((response) => {
            newPost.postimgurl = response.data;
            // console.log(newPost.postimgurl);
            if(this.data)
              this.modifyFeed(newPost);
            else
              this.insertFeed(newPost);
          })
          .catch((error) => {
            // console.log(error.response);
            alert("이미지 전송 실패");
          });
      }
    },
    onClickImageUpload() {
      this.$refs.imageInput.click();
    },
    onChangeImages(e) {
        this.showImg = true;
        this.uploadimg = true;
        this.file = e.target.files[0];
        this.imgPath = URL.createObjectURL(this.file);
        this.img = '';
    },
  },
};
</script>

