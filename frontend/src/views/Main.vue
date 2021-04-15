<template>
  <v-app class="mypage">
    <!-- 상단 -->
    <v-toolbar-title>
      <Top></Top>
      <Topsub :proptoTopsub="users"></Topsub>
    </v-toolbar-title>
    <!-- 가운데 부분 -->
    <div>
      <!-- tab view -->
      <v-tabs color="orange" v-model="currentItem" fixed-tabs slider-color="orange">
        <v-tab @click="setItem(item)" v-for="item in items" :key="item" :href="'#tab-' + item">
          <v-icon v-if="item=='Profile'" >mdi-account-box</v-icon>
          <v-icon v-if="item=='History'">fas fa-list</v-icon>
          <v-icon v-if="item=='Message'">mdi-folder</v-icon>
        </v-tab>
      </v-tabs>

      <v-tabs-items v-model="currentItem">
        <v-tab-item v-for="item in items" :key="item" :value="'tab-' + item">
          <v-card flat>
            <!-- dm -->
            <Message v-if="item=='Message'" v-bind:userId="userId"></Message>
            <!-- history -->
            <History v-else-if="item=='History'" v-bind:userId="userId" ></History>
            <!-- profile -->
            <v-card-text v-else>
            
            <!-- 밋업 있을 때 -->
            <v-container v-show="listLen">
               <v-hover v-if="meetups.length > 0">진행중 밋업</v-hover>
              <v-row dense>
                <v-col
                  v-for="(meetup, i) in meetups"
                  :key="i"
                  cols="12"
                >
                  <v-card @click="showMenu(meetup, true)">
                    <div class="d-flex">
                      <v-avatar
                      class="ma-3"
                      size="85"
                      tile
                      >
                        <v-img :src="meetup.img"></v-img>
                      </v-avatar>
                      <div>
                        <v-card-title
                        class="headline"
                        v-text="meetup.title"
                        ></v-card-title>
                        <v-card-subtitle v-html="meetup.location+'<br>'+meetup.date.slice(0, 16)" ></v-card-subtitle>
                      </div>
                    </div>
                  </v-card>
                </v-col>
              </v-row>
              <v-hover v-if="closeMeetups.length > 0">종료 밋업</v-hover>
              <v-row dense>
                <v-col
                  v-for="(meetup, i) in closeMeetups"
                  :key="i"
                  cols="12"
                >
                  <v-card @click="showMenu(meetup,false)">
                    <div class="d-flex">
                      <v-avatar
                      class="ma-3"
                      size="85"
                      tile
                      >
                        <v-img :src="meetup.img"></v-img>
                      </v-avatar>
                      <div>
                        <v-card-title
                        class="headline"
                        v-text="meetup.title"
                        ></v-card-title>
                        <v-card-subtitle v-html="meetup.location+'<br>'+meetup.date.slice(0, 16)" ></v-card-subtitle>
                      </div>
                    </div>
                  </v-card>
                </v-col>
              </v-row>
            </v-container>
             <!-- 밋업 없을 때 --> 
            <div v-show="!listLen" style="margin-top:44%;text-align:center;"> 
              <i class="fab fa-meetup fa-6x"></i>
              <div class="black--text mt-5 h6 font-weight-bold">등록된 Meetup이 없습니다.</div>
            </div>

            </v-card-text>
          </v-card>
        </v-tab-item>
      </v-tabs-items>
    </div>
    <!-- dialog -->
    <v-dialog
      v-model="dialog"
      max-width="190"
      >
        <v-list> 
        <v-list-item @click="moveMeetupDetail">
          <v-list-item-title>밋업상세보기</v-list-item-title>
        </v-list-item>
        <v-list-item v-show="!close" @click="moveCreateReview">
          <v-list-item-title>리뷰작성</v-list-item-title>
        </v-list-item>
        <v-list-item v-show="!close" @click="moveCreateReviewComment">
          <v-list-item-title>리뷰댓글</v-list-item-title>
        </v-list-item>
        <v-list-item v-show="close" @click="cancelMeetup">
          <v-list-item-title>참여취소</v-list-item-title>
        </v-list-item>
    </v-list>
    </v-dialog>
  </v-app>
</template>


<script>
import Top from "../components/common/Top";
import Topsub from "../components/common/Topsub";
import Message from "../components/common/Message";
import History from "../components/common/History";


import "../assets/css/components.scss";
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  name: "components",
  components: {
    Top,
    Topsub,
    Message,
    History,
  },
  
  data: () => {
    return {
      tabColor: "",
      meetup: "",
      dialog:false,
      // 밋업 전체 데이터
      meetups: [], 
      closeMeetups:[],
      users: {},
      userId: "",
      close:false,
      contents: "",
      currentItem: "",
      listLen : false,
      items: ["Profile", "History", "Message"],
      buttons: [
        {
          title: "확인",
          highlight: true,
          onClick: () => {
          },
        },
        {
          title: "취소",
          onClick: () => {
          },
        },
      ],
    };
  },
  created() {
    this.created();
  },
  methods: {
    setItem(item){
      this.currentItem = "tab-"+item;
    },
    isNaN(){
      if(this.closeMeetups.length == 0 && this.meetups.length == 0){
        return true;
      }
      else
        return false;
    },
    created(){
      // this.currentItem = "tab-"+this.$route.query.currentItem,
      this.meetups = [],
      this.closeMeetups = []
      this.userId = this.$cookie.get("userId");
    axios
      .get(`${SERVER_URL}/userpage/getuser?userId=`+ this.userId)
      .then((response) => {
        this.users = response.data;
      })
      .catch((error) => {
        // // console.log(error.response);
      });
    //  meetup 정보 받아오기
    axios
      .get(
        `${SERVER_URL}/meetup/list/${this.userId}`
      )
      .then((response) => {
        let now = new Date();
        if(response.data.object.length > 0 ){
          this.listLen = true;
        }
        
        for (let i = 0; i <response.data.object.length; i++) {
          let time = new Date(response.data.object[i].date)
          if(time - now > 0){
            this.meetups.push(response.data.object[i])
            // // console.log(this.meetups)
          }
          else{
            this.closeMeetups.push(response.data.object[i])
          }
        }
      })
      .catch((error) => {
        // // console.log(error.response);
        this.meetupData = false;
      });
    },
    showMenu(item,flag){
      this.dialog = true;
      this.meetup = item;
      this.close = flag;
    },
    cancelMeetup(){
      if(this.meetup.master == this.userId){
        this.$alert("밋업주최자는 참여취소가 불가합니다.")
      }
      else{
        this.$confirm("참여취소 하시겠습니까?").then(() => {
      //do something...
        axios
        .delete(
          `${SERVER_URL}/meetup?meetupId=${this.meetup.id}&userId=${this.userId}`
        )
        .then((response) => {
            this.dialog = false
            this.created();
        })
        .catch((error) => {
          // // console.log(error.response);
        });
      });
      }
    },
    // 리뷰댓글 작성페이지 이동
    moveCreateReviewComment(){
      let flag = false;
      //내가 해당 밋업 마스터가 아닐때
      if(this.meetup.master != this.userId){
        // 마스터가 쓴 리뷰 있나 검사
        axios
        .get(
          `${SERVER_URL}/rest/review/${this.meetup.restId}`
        )
        .then((response) => {
          // 리뷰가 하나도 없을때
          if(!response.data.object){
            this.$alert("밋업 호스트가 작성한 리뷰가 없습니다.")
          }
          // 리뷰들 있을때
          else{
            let flag = false;
            let reviewList = response.data.object;
            // 반복 돌면서 마스터가 작성한 리뷰가 있냐?확인
            for (let i = 0; i < reviewList.length; i++) {
                // 해당 밋업의 방장이 작성한 리뷰가 있다
                if(reviewList[i].writer == this.meetup.master &&
                    reviewList[i].meetupId == this.meetup.id){
                    // 그 리뷰 페이지로 이동하자
                    router.push({name : "ReviewDetail", params : { review : reviewList[i]}});
                    flag = true;
                    break;
                }         
            }
            // 반복돌면서 검사했는데 마스터가 작성한 리뷰가 없었네?
            if(!flag){
              this.$alert("밋업 호스트가 작성한 리뷰가 없습니다.")
            }
          }
        })
        .catch((error) => {
          // // console.log(error.response);
        });
      }
      // 내가 밋업 마스터일때
      else{
        axios
        .get(
          `${SERVER_URL}/rest/review/${this.meetup.restId}`
        )
        .then((response) => {
          // 리뷰가 하나도 없을때
          if(!response.data.object){
            this.$alert("밋업 호스트님 리뷰를 작성해주세요")
          }
          // 리뷰들 있을때
          else{
            let flag = false;
            let reviewList = response.data.object;
            // 반복 돌면서 마스터가 작성한 리뷰가 있냐?확인
            for (let i = 0; i < reviewList.length; i++) {
                // 해당 밋업의 방장이 작성한 리뷰가 있다
                if(reviewList[i].writer == this.meetup.master &&
                    reviewList[i].meetupId == this.meetup.id){
                    // 그 리뷰 페이지로 이동하자
                    router.push({name : "ReviewDetail", params : { review : reviewList[i]}});
                    flag = true;
                    break;
                }         
            }
            // 반복돌면서 검사했는데 마스터가 작성한 리뷰가 없었네?
            if(!flag){
              this.$alert("밋업 호스트님 리뷰를 작성해주세여")
            }
          }
        })
        .catch((error) => {
          // // console.log(error.response);
        });
      }
    },
    // 리뷰작성페이지로 이동
    moveCreateReview(){
      //내가 해당 밋업 마스터가 아닐때
      if(this.meetup.master != this.userId){
        this.$alert("밋업 호스트만 작성가능합니다");
      }
      else{
        axios
        .get(
          `${SERVER_URL}/rest/review/${this.meetup.restId}`
        )
        .then((response) => {
          // 리뷰가 하나도 없을때 리뷰 작성하러 이동
          if(!response.data.object){
            this.$router.push({ name : 'AddReview', params : this.meetup });
          }
          // 리뷰들 데이터가 존재할때
          else{
            let flag = false;
            let reviewList = response.data.object;
            // 반복 돌면서 나(마스터)가 작성한 리뷰가 있냐?확인
            for (let i = 0; i < reviewList.length; i++) {
                // 해당 밋업의 방장(나)이 작성한 리뷰가 있다
                if(reviewList[i].writer == this.meetup.master &&
                    reviewList[i].meetupId == this.meetup.id){
                    // 그 리뷰 페이지로 이동하자
                    this.$alert("사용자(밋업 호스트님)께서 작성한 리뷰가 존재합니다.")
                    router.push({name : "ReviewDetail", params : { review : reviewList[i]}});
                    flag = true;
                    break;
                }         
            }
            // 반복돌면서 검사했는데 마스터(나)가 작성한 리뷰가 없었네?
            if(!flag){
              this.$alert("리뷰 작성 페이지로 이동합니다")
              this.$router.push({ name : 'AddReview', params : this.meetup });
            }
          }
        })
        .catch((error) => {
          // // console.log(error.response);
        });
      }
    },
    moveMeetupDetail(){
      this.$router.push('/map/detailMeetup?meetupId='+this.meetup.id);
    }
  },
};
</script>


<style>
.container nothome {
  padding: 0px !important;
}

.meetup-title {
  font-size: 22px;
}

.text-meetup {
  text-align: center;
}



</style>