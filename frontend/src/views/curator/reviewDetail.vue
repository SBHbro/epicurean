<template>
  <v-card flat class="pb-6">
    <v-toolbar-title>
        <v-toolbar dense>
          <v-icon @click="$router.go(-1)">
            mdi-arrow-left
          </v-icon>
          <v-spacer></v-spacer>
          <p class="my-auto">리뷰</p>
          <v-spacer></v-spacer>
        </v-toolbar>
      </v-toolbar-title>

    <v-carousel :continuous="false" show-arrows hide-delimiter-background height="300">
      <!-- <v-carousel-item v-for="index of 5" :key="index"> -->
        <v-img
          :src="meetup.img"
          class="white--text align-end"
          gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          height="100%"
        ></v-img>
      <!-- </v-carousel-item> -->
    </v-carousel>
    <v-expansion-panels accordion multiple hover>
      <v-expansion-panel>
        <v-expansion-panel-header @click="show =! show">
          <span v-if="!show">밋업상세조회</span>
          <span v-else>{{meetup.title}}</span>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <v-list>
            <v-list-item>
              <v-list-item-icon>
                <v-icon color="indigo">mdi-map-marker-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{meetup.location}}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="indigo">mdi-calendar-month-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{meetup.date}}</v-list-item-title>
                <!-- <v-list-item-subtitle>Mobile</v-list-item-subtitle> -->
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="indigo">mdi-crown-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title>{{master.userNickname}}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="indigo">mdi-account-group-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                <v-list-item-title v-for="(member,i) in members" :key="i">
                  @{{member.userNickname}}
                  </v-list-item-title>
              </v-list-item-content>
            </v-list-item>

            <v-list-item>
              <v-list-item-icon>
                <v-icon color="indigo">mdi-information-outline</v-icon>
              </v-list-item-icon>
              <v-list-item-content>
                {{meetup.content}}
              </v-list-item-content>
            </v-list-item>
          </v-list>
        </v-expansion-panel-content>
      </v-expansion-panel>

      <v-expansion-panel>
        <v-expansion-panel-header>리뷰상세조회</v-expansion-panel-header>

        <v-expansion-panel-content>
          <v-spacer></v-spacer>
          <v-card class="mx-auto" max-width="400">
            <v-card-title>
              <span class="title font-weight-bold">{{review.title}}</span>
            </v-card-title>

            <v-card-text class="headline font-weight-bold">{{review.content}}</v-card-text>

            <v-card-actions>
              <v-list-item class="grow">
                <v-list-item-avatar color="grey darken-3">
                  <v-img
                    class="elevation-6"
                    :src="master.userImg"
                  ></v-img>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title>{{master.userNickname}}</v-list-item-title>
                  <v-list-item-subtitle>master</v-list-item-subtitle>
                </v-list-item-content>
              </v-list-item>
            </v-card-actions>
          </v-card>

          <v-list v-for="(comment,i) in comments" :key="i">
            <template >
              <v-divider></v-divider>
              <v-list-item>
                <v-list-item-avatar>
                  <v-img :src="'https://don.sbbro.shop/api/img/user?imgname='+reviewCommentmember[i].userImg"/>
                </v-list-item-avatar>

                <v-list-item-content>
                  <v-list-item-title>
                      <a class="black--text font-weight-bold mr-2">{{reviewCommentmember[i].userNickname}}</a>
                      
                      {{comment.content}}
                  </v-list-item-title>
                  <v-list-item-subtitle>
                      <a style="margin-right: 12px; color: #8e8e8e;">
                        {{comment.date}}
                      </a>
                  </v-list-item-subtitle>

                  <!-- <v-list-item-title>
                    {{reviewCommentmember[i].userNickname}}
                  </v-list-item-title> -->
 
                  <!-- <v-list-item-subtitle  class='text--primary'>
                    {{comment.content}} &mdash; <span  class="grey--text text--lighten-1">{{comment.date}}</span>
                  </v-list-item-subtitle> -->
                </v-list-item-content>
                
              </v-list-item>
            </template>
          </v-list>

          <v-divider></v-divider>
          <v-text-field
            v-model="comment"
            placeholder="댓글을 입력하세요." solo
            rounded dense clearable append-outer-icon="mdi-arrow-up-thick"
            @keyup.enter="writeComment"
            @click:append-outer="writeComment"
          ></v-text-field>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
    <br><br>
  </v-card>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data() {
    return {
      show: false,
      review: "",
      meetup: "",
      members: [],
      reviewCommentmember:[],
      master : [],
      comments: [],
      comment : '',
      user:[],
      imgs: [
        "https://cdn.vuetifyjs.com/images/lists/1.jpg",
        "https://cdn.vuetifyjs.com/images/lists/2.jpg",
        "https://cdn.vuetifyjs.com/images/lists/3.jpg",
        "https://cdn.vuetifyjs.com/images/lists/4.jpg",
        "https://cdn.vuetifyjs.com/images/lists/5.jpg",
      ],
    };
  },
  created() {
    axios
    .get(
      `${SERVER_URL}/userpage/getuser?userId=${this.$cookie.get("userId")}`
    )
    .then((response) => {
      this.user = response.data;
      this.user.userImg = "https://don.sbbro.shop/api/img/user?imgname="+ this.user.userImg
    })
    .catch((error) => {
      // console.log(error.response);
    });
    this.review = this.$route.params.review;
    this.getMeetup();
    this.getComments();
  },
  methods : {
    getMeetup(){
      axios
      .get(`${SERVER_URL}/meetup/search/${this.review.meetupId}`)
      .then((response) => {
        this.meetup = response.data;
        //밋업 멤버 가져오기
        axios
        .get(`${SERVER_URL}/meetup/members/${this.review.meetupId}`)
        .then((response) => {
          this.members = response.data.object;
          for (let i = 0; i < this.members.length; i++) {
            if(this.members[i].userId == this.meetup.master){
              this.master = this.members[i];
              this.master.userImg = "https://don.sbbro.shop/api/img/user?imgname="+ this.master.userImg
              break;
            }
          }
        })
        .catch((error) => {
          // console.log(error.response);
        });
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
    getComments(){
    axios
      .get(`${SERVER_URL}/review/comment/${this.review.id}`)
      .then((response) => {
        // console.log("hi")
        // console.log(response)
        this.comments = response.data.object;
        this.reviewCommentmember = response.data.user;
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
    writeComment() {
      let flag = false;
      for (let i = 0; i < this.members.length; i++) {
        // 밋업 멤버로 사용자가 있는 경우에만 댓글 작성 가능
        if(this.members[i].userId == `${this.user.userId}`){
          flag = true;
          break;
        }
      }
      if(flag){
        var newComment = {
            writer : `${this.user.userId}`,
            content : this.comment,
            reviewId : this.review.id,
          };
          axios
            .put(`${SERVER_URL}/review/comment`, newComment)
            .then((response) => {
              this.getComments();
              this.comment = "";
            })
            .catch((error) => {
              // console.log(error.response);
            });  
      }
      else{
        this.$alert("해당 밋업의 멤버만 댓글을 작성 가능합니다.")
      }
    },

  },
};
</script>