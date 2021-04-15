<template>
  <section class="user join">
    <v-app>
      <v-toolbar-title dense>
        <v-toolbar dark>
          <!-- <a @click="$router.go(-1)"> -->
          <a @click="dialog=true">
            <i class="fas fa-chevron-left back"></i>
          </a>
          <v-dialog dark v-model="dialog" max-widht="250">
            <v-list>
              작업을 취소하시겠습니까?
              <v-list-item v-for="(item, index) in items" :key="index" @click="doit(item)">
                <v-list-item-title>{{ item.title }}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-dialog>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <p class="my-auto">정보 수정</p>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>

          <a @click="updatePost()">
            <i class="fas fa-check"></i>
          </a>
        </v-toolbar>
      </v-toolbar-title>
      <!-- <p>{{ lst.postid }}</p> -->
      <div style="padding: 18px; 25px;">
        <div role="button" tabindex="-1">
          <div class="hc1 hc2" style="postion: relative; padding-right: 17px;">
            <div class="hc-d1" tabindex="-1">
              <a class="a-img1 a-img2" href="#" tabindex="0" style="width: 32px; height: 32px;">
                <img
                  style="height: 100%; width: 100%;"
                  :src="`https://don.sbbro.shop/api/img/user?imgname=`+this.userimg"
                />
              </a>
            </div>
            <div style="float: right;">
              <div @click="onClickImageUpload">
                <!-- <input ref="imageInput" type="file" hidden @change="onChangeImages" /> -->
                <i class="fa fa-images" style="margin-top: 6px; width: 20px; height: 20px;"></i>
              </div>
            </div>
            <div class="pf">
              <div>
                <div class="pf-n">
                  <!-- <a
                    class="pf-n-a"
                    href="#"
                    tabindex="0"
                    style="color: black; font-weight: 600;"
                  >{{this.usernickname}}</a>-->
                  <span>{{this.usernickname}}</span>
                </div>
              </div>
              <div></div>
            </div>
          </div>
        </div>
        <div class="fc">
          <div class="fc-frame" tabindex="0">
            <div class="fc-fr">
              <img
                style="height: 350px;"
                :src="`https://don.sbbro.shop/api/img/post?imgname=`+this.postimgurl"
                class="fc-img"
              />
            </div>
          </div>
        </div>
        <div class="fb">
          <div style="margin-bottom: 4px;">
            <v-text-field autofocus v-model="postcontent"></v-text-field>
          </div>
        </div>
      </div>
    </v-app>
  </section>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data() {
    return {
      postid: "",
      postcontent: "",
      postimgurl: "",
      postuserid: this.$cookie.get("userId"),
      usernickname: "",
      userimg: "",
      items: [{ title: "예" }, { title: "아니오" }],
      dialog: false,
      uploadimg: false,
    };
  },
  methods: {
    onClickImageUpload() {
      this.uploadimg = true;
      this.$refs.imageInput.click();
    },
    doit(item) {
      if (item.title == "예") {
        router.go(-1);
        this.dialog = false;
      } else {
        this.dialog = false;
      }
    },
    updatePost() {
      var newpost = {
        postcontent: this.postcontent,
        postimgurl: this.postimgurl,
        postid: this.postid,
      };
      axios
        .put(`${SERVER_URL}/post`, newpost)

        .then((response) => {
          this.$router.push("/user/info");
        });
    },
  },
  created() {
    this.postid = this.$route.query.postlst.post_id;
    this.postcontent = this.$route.query.postlst.post_content;
    this.postimgurl = this.$route.query.postlst.post_img_url;
    this.userimg = this.$route.query.postlst.user_img;
    this.usernickname = this.$route.query.postlst.user_nickname;
  },
};
</script>

<style scoped>
.v-application a {
  color: white;
}
.heart-div {
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-pack: center;
  justify-content: center;
  position: relative;
}

.heart-btn {
  -webkit-box-align: center;
  align-items: center;
  background: 0 0;
  border: 0;
  cursor: pointer;
  display: flex;
  -webkit-box-pack: center;
  justify-content: center;
  padding: 8px;
}

.heart {
  /* display: inline-block; */
  margin: 0 0 0 -4px;
  padding: 0;
  border: 0;
  /* vertical-align: baseline; */
  -webkit-box-direction: normal;
}

.func {
  margin-top: 4px;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  flex-direction: row;
  -webkit-box-align: stretch;
  align-items: stretch;
  border: 0 solid black;
  display: flex;
  box-sizing: border-box;
  display: flex;
  flex-shrink: 0;
  margin: 0;
  padding: 0;
  position: relative;
}

.fb {
  padding: 0 16px;
  -webkit-box-align: stretch;
  align-items: stretch;
  border: 0 solid black;
  box-sizing: border-box;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  flex-shrink: 0;
  margin: 0;
  position: relative;
}

.fc {
  -webkit-box-align: stretch;
  align-items: stretch;
  display: flex;
  border: 0 solid black;
  box-sizing: border-box;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  flex-shrink: 0;
  margin: 0;
  padding: 0;
  position: relative;
}

.hc1 {
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  flex-direction: row;
  height: 60px;
  padding: 16px;
}

.hc2 {
  border-bottom: 1px solid rgba(var(--ce3, 239, 239, 239), 1);
}

.hc-d1 {
  -webkit-box-align: center;
  align-items: center;
  align-self: center;
  /* display: block; */
  float: left;
  -webkit-box-flex: 0;
  flex: none;
  -webkit-box-pack: center;
  justify-content: center;
}

.a-img1 {
  background-color: rgba(var(--b3f, 250, 250, 250), 1);
  border-radius: 50%;
  box-sizing: border-box;
  display: block;
  /* float: left; */
  /* padding: 12px; */
  -webkit-box-flex: 0;
  flex: 0 0 auto;
  overflow: hidden;
  position: relative;
}

.a-img2 {
  cursor: pointer;
}

.pf {
  -webkit-box-aligh: start;
  align-items: flex-start;
  display: flex;
  /* float: left; */
  -webkit-box-flex: 1;
  flex-grow: 1;
  flex-shrink: 1;
  margin-left: 12px;
  /* padding: 12px; */
  overflow: hidden;
}

.pf-n {
  -webkit-box-align: center;
  align-items: center;
  -webkit-box-orient: horizontal;
  -webkit-box-direction: normal;
  flex-direction: row;
  -webkit-box-flex: 1;
  flex-grow: 1;
  flex-shrink: 1;
  max-width: 100%;
  overflow: hidden;
  padding: 2px;
  top: 1px;
}

.pf-n-a {
  padding-left: 12px;
  padding-top: 4px;
  appearance: none;
  background: 0 0;
  text-align: center;
  text-transform: inherit;
  text-overflow: ellipsis;
  width: auto;
}

.fc-img {
  object-fit: cover;
  height: 100%;
  left: 0;
  position: absolute;
  top: 0;
  user-select: none;
  width: 100%;
  margin: 0;
  padding: 0;
  border: 0;
  font: inherit;
  vertical-align: baseline;
  position: relative;
}

.fc-frame {
  touch-action: manipulation;
  -webkit-box-align: stretch;
  align-content: stretch;
  border: 0 solid black;
  box-sizing: border-box;
  display: flex;
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  flex-shrink: 0;
  margin: 0;
  padding: 0;
  position: relative;
}

.fc-fr {
  /* padding-bottom: 125%; */
  display: block;
  /* overflow: hidden; */
  /* align-items: stretch; */
  border: 0 solid black;
  /* box-sizing: border-box; */
  -webkit-box-orient: vertical;
  -webkit-box-direction: normal;
  flex-direction: column;
  flex-shrink: 0;
  margin: 0;
  padding: 0;
  position: relative;
}
</style>