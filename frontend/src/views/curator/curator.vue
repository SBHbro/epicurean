<template>
  <div>
    <!-- 상단 툴바 -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">음식점 검색</p>
      <v-spacer></v-spacer>
    </v-toolbar> 
  
  <div class="entire">
  <div style="padding : 0">

    <v-tooltip top
      nudge-bottom="20"
      color="rgba(1, 1, 1, 0.7)"
      :position-y="-10">
    <template v-slot:activator="{ on, attrs }">
      <v-card-title class="pb-0 pt-0">
        <v-text-field
          hide-details=""
          v-model="keyword"
          @keyup.enter="doSearch"
          placeholder='키워드 검색'
          outlined clearable
          prepend-inner-icon="mdi-magnify"
          v-bind="attrs" v-on="on"
        />
      </v-card-title>
    </template>
      <span class="text--caption">ex) 대전 맛집, 삼겹살 ...</span>
    </v-tooltip>
      <br>
    <div v-if="filter==true && restaurants.length != 0" style="text-align: center;" class="mb-3" >
      <v-btn-toggle>
        <v-icon>mdi-filter-outline</v-icon>
        <v-btn small width="23%" outlined @click="sortByDist">거리순</v-btn>
        <v-btn small width="23%" outlined @click="sortByLike">좋아요순</v-btn>
        <v-btn small width="23%" outlined @click="sortByReview">리뷰순</v-btn>
        <v-btn small width="23%" outlined @click="sortByProperties">선호음식</v-btn>
      </v-btn-toggle>
    </div>

    <v-card>
        <v-row dense>
          <v-col v-for="restaurant in restaurants" :key="restaurant.id" cols="12">
            <v-hover v-slot:default="{ hover }">
              <v-card  :elevation="hover ? 12 : 2"
              :class="{ 'on-hover': hover }"
              >
                <v-img
                  :src="restaurant.img[0]"
                  class="white--text align-end"
                  gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                  height="200px"
                  @click="moveDetail(restaurant)"
                  >
                </v-img>

                <v-card-actions>
                  <div style="width:55%;">
                    <v-card-title @click="moveDetail(restaurant)">{{restaurant.name}}</v-card-title>
                    <v-card-subtitle v-text="restaurant.location" @click="moveDetail(restaurant)"></v-card-subtitle>
                    <v-card-subtitle class="caption grey--text" v-html="restaurant.distUnit" @click="moveDetail(restaurant)"></v-card-subtitle>
                  </div>
                  <v-spacer></v-spacer>
                  <v-spacer></v-spacer>
                  <v-spacer></v-spacer>
                  <v-btn icon small :color="likes.indexOf(restaurant.id*1) != -1 ? 'pink' : ''" @click="doLike(restaurant)">
                    <v-icon v-text="likes.indexOf(restaurant.id*1) != -1 ? 'mdi-heart' : 'mdi-heart-outline' "></v-icon>
                  </v-btn>
                  <span style="margin:0 5px 0 -1px;">{{restaurant.like}}</span>

                  <v-btn icon small :color="scraps.indexOf(restaurant.id*1) != -1 ? 'blue' : ''" @click="doScrap(restaurant)">
                    <v-icon v-text="scraps.indexOf(restaurant.id*1) != -1 ? 'mdi-bookmark-check' : 'mdi-bookmark-check-outline' "></v-icon>
                  </v-btn>
                  <span style="margin:0 3px 0 -2px;">{{restaurant.scrap}}</span>

                  <v-btn icon>
                    <v-icon size="23">mdi-comment-edit-outline</v-icon>
                  </v-btn>
                  <span style="margin:0 2px 0 -4px;">{{restaurant.review}}</span>
                  <v-spacer></v-spacer>
                </v-card-actions>
              </v-card>
            </v-hover>
          </v-col>
        </v-row>
    </v-card>

    <div v-if="filter==true && restaurants.length == 0" style="text-align: center">
      <p>'{{keyword}}'에 대한 검색결과가 없습니다.</p>
    </div>
  </div>
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
      restaurantsLength : "",
      likes : [],
      scraps : [],
      reviews : [],
      fav: true,
      menu: false,
      message: false,
      hints: true,
      filter: false,
      targetLocation :{
        lat : '',
        lng : '',
      },

      geocoder : '',
      keyword: '',
      restaurants : []
    };
  },
  mounted() {
    
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(pos => {
        this.targetLocation.lat = pos.coords.latitude;
        this.targetLocation.lng = pos.coords.longitude;
      });
    }

    if (window.Kakao && window.Kakao.maps) {
      // this.initMap();
    }else{
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=6192aa4fccde619ea3ac09491e57abd0&libraries=services";
      document.head.appendChild(script);
    }

    // 페이지 로딩 시 로그인한 유저의 좋아요/스크랩 정보를 가져옴
    axios
      .get(`${SERVER_URL}/rest/like/${this.$cookie.get('userId')}`)
      .then((response) => {
        this.likes = response.data;
    });

    axios
      .get(`${SERVER_URL}/rest/scrap/${this.$cookie.get('userId')}`)
      .then((response) => {
        this.scraps = response.data;
    });

  },
  methods: {
    sortByDist(){
      // console.log("거리순");
      this.restaurants.sort((a, b) => {
        return a.dist - b.dist;
      })
    },
    sortByLike(){
      // console.log("좋아요순");
      this.restaurants.sort((a, b) => {
        return -1 * (a.like - b.like);
      })
    },
    sortByReview(){
      // console.log("리뷰순");
      this.restaurants.sort((a, b) => {
        return -1 * (a.review - b.review);
      })
    },
    sortByProperties(){
      // console.log("선호음식");
      axios
        .get(`${SERVER_URL}/userpage/getuserInfo?userId=${this.$cookie.get('userId')}`)
        .then((response) => {
          var userInfo = response.data;
          var userFavoriteID = userInfo.userFavorite;
          var userFavorite = [];
          userFavoriteID = userFavoriteID.substr(1, userInfo.userFavorite.length - 2);
          userFavoriteID = userFavoriteID.split(", ");
          (userFavoriteID).forEach(category => {
            if(category == 1){
              userFavorite.push("한식");
            } 
            else if(category == 2){
              userFavorite.push("양식");
            }
            else if(category == 3){
              userFavorite.push("중식");
            }
            else if(category == 4){
              userFavorite.push("일식");
            }
            else if(category == 5){
              userFavorite.push("분식");
            }
            else if(category == 6){
              userFavorite.push("뷔페");
            }
          });
          this.restaurants.sort((a, b) => {
            return (userFavorite.indexOf(a.category) - userFavorite.indexOf(b.category)) * -1;
          })
        })
    },
    distUnitConversion(distance){
      var result = '';
      var tempDist = Math.round(distance);
      var km = Math.floor(tempDist / 1000);
      var m = tempDist % 1000;
      if(km > 0)
        result = result.concat(km, "km ");
      result = result.concat(m, "m");
      return result;
    },
    doSearch() {
      // 주소-좌표 변환 객체를 생성합니다
      this.geocoder = new kakao.maps.services.Geocoder();
      axios
        .get(`${SERVER_URL}/curation?location=${this.keyword}`)
        .then((response) => {
          // 음식점리스트 받기
          var restaurants = response.data.list;       
          // 음식점리스트 돌면서 좌표(position), 거리(dist) 구하기
          restaurants.forEach(restaurant => {

            // String 형태의 img src 5개를 파싱해서 배열로 만듬
            let imgSrcs = restaurant.img;
            imgSrcs = imgSrcs.replace('[', '');
            imgSrcs = imgSrcs.replace(']', '');
            imgSrcs = imgSrcs.replace(/(\s*)/g, ''); // 모든공백제거
            imgSrcs = imgSrcs.split(",");
            restaurant.img = imgSrcs;

            // 주소 -> 좌표
            this.geocoder.addressSearch(restaurant.location, (result, status) => {
              if (status === kakao.maps.services.Status.OK) {
                var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
              }
              restaurant.position = coords;
              
              // 현위치에서 음식점까지의 거리(dist) 구하기
              if(restaurant.position){
                var polyline = new kakao.maps.Polyline({
                  path: [
                    new kakao.maps.LatLng(this.targetLocation.lat, this.targetLocation.lng),
                    restaurant.position
                  ],
                });
                restaurant.dist = polyline.getLength();
                restaurant.distUnit = this.distUnitConversion(polyline.getLength());
              }
            });
          })
          this.restaurants = restaurants;
          this.filter = true;
        })
        .catch((error) => {
          // console.log(error.response);
        });
    },
    moveDetail(restaurant) {
      router.push({name : "Detail", query : restaurant});
    },
    doLike(restaurant){
      if(this.$cookie.get('userId') == null){
        alert("로그인 해주세요");
        return;
      }

      // DB에 존재하지 않는 음식점이라면
      if(restaurant.id == null){

        // img src 배열을 String으로 변환 "[src1,src2,...,src5]"
        var imgSrcs = restaurant.img.toString();
        imgSrcs = `[${imgSrcs}]`;

        // 새로운 음식점 객체 생성
        var newRest = {
          category : restaurant.category,
          img : imgSrcs,
          like : restaurant.like,
          location : restaurant.location,
          name : restaurant.name,
          review : restaurant.review,
          scrap : restaurant.scrap,
          telphone : restaurant.telphone,
          url : restaurant.url,
          dist  : restaurant.dist
        };

        // DB에 음식점 정보 등록 후 restId 반환
        axios
        .post(`${SERVER_URL}/rest`, newRest)
        .then((response) => {
            var newRestId = response.data;
            restaurant.id = newRestId;
            // console.log(restaurant.id);
            this.toggleLike(restaurant);
        });
      }
      // DB에 존재하는 음식점이라면
      else{
        this.toggleLike(restaurant);
      }
    },
    toggleLike(restaurant){
        // 내가 좋아요한 음식점이 아니라면
        if(this.likes.indexOf(restaurant.id*1) == -1){
          this.likes.push(restaurant.id*1);
          axios
          .put(`${SERVER_URL}/rest/like?userid=${this.$cookie.get('userId')}&restid=${restaurant.id}`)
          .then((response) => {
            // reponse : 해당 음식점의 좋아요 갯수
            restaurant.like = response.data;
            
          })       
        }
        // 내가 좋아요한 음식점이라면
        else{
          this.likes.splice(this.likes.indexOf(restaurant.id), 1);
          axios
          .delete(`${SERVER_URL}/rest/like?userid=${this.$cookie.get('userId')}&restid=${restaurant.id}`)
          .then((response) => {
            // reponse : 해당 음식점의 좋아요 갯수
            restaurant.like = response.data;
          })  
        }
    },
    doScrap(restaurant){
      if(this.$cookie.get('userId') == null){
        alert("로그인 해주세요");
        return;
      }

      // DB에 존재하지 않는 음식점이라면
      if(restaurant.id == null){

        // img src 배열을 String으로 변환 "[src1,src2,...,src5]"
        var imgSrcs = restaurant.img.toString();
        imgSrcs = `[${imgSrcs}]`;

        // 새로운 음식점 객체 생성
        var newRest = {
          category : restaurant.category,
          img : imgSrcs,
          like : restaurant.like,
          location : restaurant.location,
          name : restaurant.name,
          review : restaurant.review,
          scrap : restaurant.scrap,
          telphone : restaurant.telphone,
          url : restaurant.url,
          dist  : restaurant.dist
        };

        // DB에 음식점 정보 등록 후 restId 반환
        axios
        .post(`${SERVER_URL}/rest`, newRest)
        .then((response) => {
            var newRestId = response.data;
            restaurant.id = newRestId;
            // console.log(restaurant.id);
            this.toggleScrap(restaurant);
        });
      }
      // DB에 존재하는 음식점이라면
      else{
        this.toggleScrap(restaurant);
      }
    },
    toggleScrap(restaurant){
        // 내가 좋아요한 음식점이 아니라면
        if(this.scraps.indexOf(restaurant.id*1) == -1){
          this.scraps.push(restaurant.id*1);
          axios
          .put(`${SERVER_URL}/rest/scrap?userid=${this.$cookie.get('userId')}&restid=${restaurant.id}`)
          .then((response) => {
            // reponse : 해당 음식점의 좋아요 갯수
            restaurant.scrap = response.data;
          })       
        }
        // 내가 좋아요한 음식점이라면
        else{
          this.scraps.splice(this.scraps.indexOf(restaurant.id), 1);
          axios
          .delete(`${SERVER_URL}/rest/scrap?userid=${this.$cookie.get('userId')}&restid=${restaurant.id}`)
          .then((response) => {
            // reponse : 해당 음식점의 좋아요 갯수
            restaurant.scrap = response.data;
          })  
        }
    },
  },
  components: {},
};
</script>