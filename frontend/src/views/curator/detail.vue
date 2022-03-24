<template>
  <v-card elevation="24" max-width="444" class="mx-auto">
    <v-toolbar-title>
        <v-toolbar class="mb-1" dense elevation="1">
          <v-icon @click="$router.go(-1)">
            mdi-arrow-left
          </v-icon>
          <v-spacer></v-spacer>
          <p class="my-auto">음식점 상세보기</p>
          <v-spacer></v-spacer>
        </v-toolbar>
      </v-toolbar-title>
    <v-carousel
      :continuous="false"
      show-arrows
      hide-delimiter-background
      height="300"
    >
      <v-carousel-item v-for="(img, index) in restaurant.img" :key="index">
        <v-img
          :src="img"
          class="white--text align-end"
          gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          height="100%"
        ></v-img>
      </v-carousel-item>
    </v-carousel>

    <v-list>
      <v-list-item>
        <v-list-item-content>
          <v-list-item-title v-text="restaurant.name"></v-list-item-title>
          <v-list-item-subtitle v-text="restaurant.category"></v-list-item-subtitle>
        </v-list-item-content>
        <v-list-item-action>
          <v-btn icon @click="show = !show">
            <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
          </v-btn>
        </v-list-item-action>
      </v-list-item>

      <v-expand-transition>
        <div v-show="show">
          <v-divider></v-divider>

          <v-list-item>
            <v-list-item-icon>
              <v-icon color="indigo">mdi-map-marker</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title v-if="restaurant.location" v-text="restaurant.location"></v-list-item-title>
              <v-list-item-subtitle  v-if="restaurant.location" v-text="restaurant.location"></v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon color="indigo">mdi-phone</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title  v-if="restaurant.telphone" v-text="restaurant.telphone"></v-list-item-title>
              <v-list-item-title  v-else>등록된 연락처가 없습니다.</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

          <v-list-item>
            <v-list-item-icon>
              <v-icon color="indigo">mdi-link-variant</v-icon>
            </v-list-item-icon>
            <v-list-item-content>
              <v-list-item-title v-if="restaurant.url"><a :href="restaurant.url">{{restaurant.url}}</a></v-list-item-title>
              <v-list-item-title v-else>등록된 URL이 없습니다.</v-list-item-title>
            </v-list-item-content>
          </v-list-item>

        </div>
      </v-expand-transition>

      <v-list two-line>
        <template v-for="(review, index) in reviews">
          <v-divider :key="index"></v-divider>
          <v-subheader v-if="review.header" :key="review.header" v-text="review.header"></v-subheader>
          <v-divider v-else-if="review.divider" :key="index" :inset="review.inset"></v-divider>
          <v-list-item v-else :key="review.title" @click="moveReviewDetail(review)">

            <v-list-item-avatar>
              <v-img :src="`https://don.sbbro.shop/api/img/user?imgname=`+reviewWriterImg[index]"></v-img>
            </v-list-item-avatar>

            <v-list-item-content>
              <v-list-item-title>
                {{review.title}}
              </v-list-item-title>
              <v-list-item-subtitle>
                {{review.content}}
                <!-- <span class="text--primary" v-for="(member, index) in members[index]" :key="index">
                  <span class="blue--text" v-if="index == 0">@{{member}} </span>
                  <span v-else>@{{member}} </span>
                </span> -->
                <span class="text-caption grey--text text--lighten-1" style="float:right;" v-text="review.date"></span>
                <!-- &mdash; {{review.content}} -->
              </v-list-item-subtitle>
            </v-list-item-content>
          </v-list-item>
        </template>
      </v-list>
    </v-list>
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
      cycle: false,
      show: false,
      restaurant: this.$route.query,
      reviews: [],
      members : [],
      reviewWriterId : [],
      reviewWriterImg :[]
    };
  },
  created() {
    axios
        .get(`${SERVER_URL}/rest/review/${this.restaurant.id}`)
        .then((response) => {
          this.reviews = response.data.object;
          // this.reviews = response.data.review;
          // this.members = response.data.member;

          //이미지 가져오기
          for(var i = 0 ; i<this.reviews.length;i++){
            this.reviewWriterId.push(this.reviews[i].writer);
          }
    
           axios.post(
                    `${SERVER_URL}/userpage/getuserpost`,this.reviewWriterId
                )
                .then((response) => {
                    // // console.log('응답',response);
                   this.reviewWriterImg = response.data;
              })
                .catch((error) => {
                    // console.log(error.response);
                });
          
        })
        .catch((error) => {
          // console.log(error.response);
        });
      

  },
  methods : {
    moveReviewDetail(review) {
      router.push({name : "ReviewDetail", params : { review : review }});
    }
  }
};
</script>