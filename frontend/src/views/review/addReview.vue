<template>
    <div>
      <v-toolbar dense elevation="1">
        <v-icon @click="$router.go(-1)">
          mdi-arrow-left
        </v-icon>
        <v-spacer></v-spacer>
        <p class="my-auto text-center">리뷰작성</p>
        <v-spacer></v-spacer>
        <a @click="addReview">
            <i class="fas fa-check"></i>
        </a>
      </v-toolbar>
      <div>
        <v-form
          ref="form" 
          v-model="form"
          class="pa-8 pt-8"
        >
         <!-- 밋업 타이틀 -->
          <v-col class="pb-0">
            <v-text-field
              outlined hide-details="auto" 
              v-model="review.title"
              label="제목"
              clearable
            >          
            </v-text-field>
          </v-col>

           <!-- 밋업 내용 -->
          <v-col class="pb-0">
            <v-textarea
              outlined hide-details="auto"
              v-model="review.content"
              label="Contents"
              rows="10"
              clearable
            >          
            </v-textarea>
          </v-col>
        </v-form>
        

      </div>
    </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data() {
    return {
      review : {
        writer : this.$cookie.get("userId"),
        title : '',
        content : '',

        category : '',
        restId : '',
        meetupId : '',
      },



      revise: false,
      revise1: false,
      revise3: true,

      form: false,
      isLoading: false,
      rules: {
        email: v => !!(v || '').match(/@/) || 'Please enter a valid email',
        length: len => v => (v || '').length >= len || `Invalid character length, required ${len}`,
        password: v => !!(v || '').match(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*(_|[^\w])).+$/) ||
          'Password must contain an upper case letter, a numeric character, and a special character',
        required: v => !!v || 'This field is required',
      },
    };
  },
  created() {
    // console.log(this.$cookie.get("userId"));
    // // console.log(this.$route.params);

    

    // if (this.$route.params.postid >= 0) {
    //   // console.log("여기");
    //   // console.log(this.$route.params);
    //   this.revise = true;
    //   // console.log(this.revise);
    //   this.postimg = this.$route.params.postimage;
    //   this.content = this.$route.params.postcontent;
    //   // console.log(this.content);
    //   // console.log(this.postimg);
    // }
    axios
      .get(
        `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
      )
      .then((response) => {
        // console.log(response);
        this.username = response.data.userNickname;
        this.userimg = response.data.userImg;
        // console.log(this.username);
      })
      .catch((error) => {
        // console.log(error.response);
      });
  },
  methods: {
    addReview(){
      var meetup = this.$route.params;
      this.review.category = meetup.category;
      this.review.meetupId = meetup.id;
      this.review.restId = meetup.restId;
      axios
        .put(`${SERVER_URL}/review`, this.review)
        .then((response) => {
          alert("리뷰가 등록되었습니다.");
          this.$router.go(-1);
        })
        .catch((error) => {
          // console.log(error.response);
        });
    },
    // addImg() {
    //   //이미지 서버에 전송해서 저장하는부분
    //   const file = new FormData();
    //   file.append("file", this.file);
    //   // console.log(file);
    //   axios
    //     .post(`${SERVER_URL}/post/img`, file)

    //     .then((response) => {
    //       this.postimgurl = response.data;
    //       // console.log("여기");
    //       // console.log(this.postimgurl);
    //       this.addPost();
    //     })

    //     .catch((error) => {
    //       // console.log(error.response);
    //       alert("이미지 전송 실패");
    //     });
    // },

    // reviseImg() {
    //   const file = new FormData();
    //   file.append("file", this.file);
    //   // console.log(file);
    //   axios
    //     .post(`${SERVER_URL}/post/img`, file)

    //     .then((response) => {
    //       this.postimgurl = response.data;
    //       // console.log("여기");
    //       // console.log(this.postimgurl);
    //       var repost = {
    //         postcontent: this.content,
    //         postid: this.$route.params.postid,
    //         postimgurl: this.postimgurl
    //       };
    //       // console.log(this.postimgurl);
    //       // console.log("여기여기");
    //       // console.log(repost);
    //       axios
    //         .put(`${SERVER_URL}/post`, repost)

    //         .then((response) => {
    //           // this.postimgurl = response.data;
    //           // // console.log(this.postimgurl);
    //           // console.log("여기여기여기역이겨이겨")
    //           router.push({ name: "NewsFeed" });
    //         })

    //         .catch((error) => {
    //           // console.log(error.response);
    //           alert("이미지 전송 실패");
    //         });
    //     })

    //     .catch((error) => {
    //       // console.log(error.response);
    //       alert("이미지 전송 실패");
    //     });
    // },
    //게시물을 DB에 저장하는 부분
    // addPost() {
      // if (this.revise1) {
      //   var repost = {
      //     postcontent: this.postcontent,
      //     postimgurl: this.postimg,
      //     postuserid: this.postuserid,
      //   };
      //   // console.log(repost);
      //   axios
      //     .post(`${SERVER_URL}/post`, repost)

      //     .then((response) => {
      //       alert("성공");
      //       this.$router.push("/newsfeed");
      //     })

      //     .catch((error) => {
      //       // console.log(error.response);
      //       alert("데이터 전송 실패");
      //     });
      // } else {
      //   var newpost = {
      //     postcontent: this.postcontent,
      //     postimgurl: this.postimgurl,
      //     postuserid: this.postuserid,
      //   };
      //   axios
      //     .post(`${SERVER_URL}/post`, newpost)

      //     .then((response) => {
      //       alert("성공");
      //       this.$router.push("/newsfeed");
      //     })

      //     .catch((error) => {
      //       // console.log(error.response);
      //       alert("데이터 전송 실패");
      //     });
      // }
    // },

    // onClickImageUpload() {
    //   this.$refs.imageInput.click();
    // },

    // onChangeImages(e) {
    //   if (this.$route.params.postid >= 0) {
    //     this.file = e.target.files[0];
    //     this.postimg = URL.createObjectURL(this.file);
    //     this.revise1 = true;
    //     this.revise3 = false;
    //   } else {
    //     this.file = e.target.files[0];
    //     this.postimgurl = URL.createObjectURL(this.file);
    //   }
    //   // console.log(e.target.files);
    //   // console.log(this.postimg);
    // },

    // insert() {},
  },
};
</script>

<style scoped>
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
</style>
