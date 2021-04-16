<template>
  <div>
    <v-toolbar dense elevation="1">
      <v-icon @click="$router.push({name: 'Map', params : $route.params})">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">MeetUp</p>
      <v-spacer></v-spacer>
      <a @click="meetUp">
          <i class="fas fa-check"></i>
      </a>
    </v-toolbar>
      <br />


      <!-- 입력 폼 -->
      <div>
        <v-form ref="form">

          <!-- 밋업 타이틀 -->
          <v-col class="pb-0">
            <v-text-field
              :rules="[() => !!meetup.title]"
              outlined hide-details="auto" 
              v-model="meetup.title"
              label="제목"
              clearable
            >          
            </v-text-field>
          </v-col>

          <!-- 밋업 내용 -->
          <v-col class="pb-0">
            <v-textarea
              :rules="[() => !!meetup.content]"
              outlined hide-details="auto" 
              v-model="meetup.content"
              label="내용"
              clearable
            >          
            </v-textarea>
          </v-col>


          <!-- 밋업 장소 -->
          <v-col class="pb-0">
          <v-layout justify-center>
                <v-dialog v-model="dialog" scrollable max-width="300px">
                  <template v-slot:activator="{on}">
                    <v-text-field
                      :rules="[() => !!meetup.location]"
                      outlined hide-details="auto" 
                      slot="activator"
                      v-on="on"
                      v-model="meetup.location"
                      readonly
                      label="장소"
                    ></v-text-field>
                  </template>
                  
                  <!-- dialog -->
                  <v-card>



                   



                <v-tooltip top
                    nudge-bottom="20"
                    color="rgba(1, 1, 1, 0.7)"
                    :position-y="-10">
                  <template v-slot:activator="{ on, attrs }">
                    <v-card-title class="pb-0">
                      <v-text-field
                        v-model="keyword"
                        @keyup.enter="search"
                        placeholder='키워드 검색'
                        outlined clearable
                        prepend-inner-icon="mdi-magnify"
                        v-bind="attrs" v-on="on"
                        
                      />
                    </v-card-title>
                  </template>
                   <span class="text--caption">ex) 대전 맛집, 부산 맛찬들 ...</span>
                 </v-tooltip>

                    <v-card-text style="height: 300px;">
                        <!-- 음식점 리스트 들어감 -->
                        
                        <v-card class="mx-auto" max-width="500" flat>
                            <v-row dense>
                              <v-col v-for="restaurant in restaurants" :key="restaurant.restId" cols="12">
                                <v-hover v-slot:default="{ hover }">
                                  <v-card  :elevation="hover ? 12 : 2"
                                  :class="{ 'on-hover': hover }"
                                  @click="select(restaurant)">
                                    <v-card-actions>
                                      <div>
                                        <v-card-title v-text="restaurant.name"></v-card-title>
                                        <v-card-subtitle v-text="restaurant.location"></v-card-subtitle>
                                      </div>
                                      <v-spacer></v-spacer>
                                    </v-card-actions>
                                  </v-card>
                                </v-hover>
                              </v-col>
                            </v-row>
                          </v-card>


                    </v-card-text>
                    <v-divider class="mb-0"></v-divider>
                    <v-card-actions>
                      <v-btn color="black" text block class="mx-auto" @click="dialog = false">닫기</v-btn>
                    </v-card-actions>
                  </v-card>
                </v-dialog>
              </v-layout>
              </v-col>


          <!-- 밋업 날짜 -->
          <v-col class="pb-0">
            <v-dialog
              ref="dateDialog1" v-model="dateDialog1"
              :return-value.sync="date"
              width="290px"
            >
                <!-- 월/일 선택 -->
                <template v-slot:activator="{ on, attrs }">
                  <v-text-field
                    :rules="[() => !!meetup.date]"
                    outlined
                    hide-details
                    v-model="meetup.date"
                    label="날짜"
                    readonly
                    v-bind="attrs"
                    v-on="on"
                  ></v-text-field>
                </template>

                <v-date-picker
                  v-model="date"
                  :min="new Date().toISOString().substr(0, 10)"
                  max="2050-01-01"
                  scrollable
                  show-current no-title
                  width="290px"
                >
                  <v-spacer></v-spacer>
                  <v-btn text color="black" @click="dateDialog1 = false">취소</v-btn>
                  <v-btn text color="black" @click="dateDialog2 = true">확인</v-btn>

              </v-date-picker>
            </v-dialog>

            <v-dialog ref="dateDialog2" v-model="dateDialog2"
              :return-value.sync="time"
              width="290px"
            >
              <v-time-picker
                v-model="time"
                ampm-in-title
                width="260px"
              >
                <v-spacer></v-spacer>
                <v-btn text color="black" @click="menu2 = false">취소</v-btn>
                <v-btn text color="primary" @click="setDate">확인</v-btn>
              </v-time-picker>
            </v-dialog>
            </v-col>
            

          <!-- 밋업 인원 -->
            <v-col class="pb-0">
              <div id="dropdown-example">
                <v-select
                  hide-details
                  outlined
                  :rules="[() => !!meetup.maxPersonnel]"
                  v-model="meetup.maxPersonnel"
                  class="my-0 pa-0"
                  :items="dropdown_font"
                  label="인원"
                  target="#dropdown-example"
                ></v-select>
              </div>
            </v-col>
            

          <!-- 밋업 성향 -->
          <v-col class="pb-0">
            <v-dialog
              v-model="propsDialog"
              width="500"
            >
              <template v-slot:activator="{ on, attrs }">
                <v-combobox
                  ref="props"
                  :rules="[() => !!meetup.personalities[0]]"
                  hide-details="auto"
                  outlined
                  v-model="meetup.personalities" 
                  multiple small-chips
                  v-on="on" v-bind="attrs"
                  label="성향"
                  auto clearble
                  readonly
                >
                  <template v-slot:selection="data">
                    <v-chip class="mr-1 px-2"
                      color="primary" small
                    >
                      <span class="mr-1">
                        {{data.item}}
                      </span>
                      <v-icon small @click="data.parent.selectItem(data.item)">
                        mdi-close
                      </v-icon>
                    </v-chip>
                  </template>
                </v-combobox>
              </template>

              <v-card>
                  <v-card-title class="subtitle">성향
                    <v-spacer></v-spacer>
                    <v-btn outlined rounded small
                      class="text-caption font-weight-bold"
                      :class="{'primary--text': isAllPropsSelect}"
                      @click="doAllPropsSelect"
                    >
                      <v-icon small>mdi-check</v-icon>                            
                      전체
                    </v-btn>
                  </v-card-title>
                  <v-divider class="mt-1 mb-6"></v-divider>

                  <v-card-text>
                    <v-btn-toggle v-model="meetup.personalities" multiple color="primary">
                      <v-row justify="center">
                        <v-btn tile outlined large v-for="props in personalities" :key="props" :value="props"
                        active-class="primary--text">
                          {{props}}
                        </v-btn>
                      </v-row>
                    </v-btn-toggle>
                  </v-card-text>
                  <v-card-actions>
                    <v-btn block color="primary" @click="propsDialog = false">확인</v-btn>
                  </v-card-actions>
              </v-card>
             </v-dialog>
            </v-col>
          </v-form>
        </div>
<!-- 
        <div class="mt-8 mb-5 btns">
          <v-btn color="grey white--text" @click="$router.go(-1)" value="center">취소</v-btn>
          <v-btn color="warning" value="right" class="ml-3" @click="meetUp">생성</v-btn>
        </div> -->
  </div>
</template>

<script>
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data: () => {
    return {
      keyword : '',
      date: '',
      time: null,
      dateDialog1 : '',
      dateDialog2 : '',
      isClick: false,
      dialog: false,
      dialog2: false,


      restaurants : [],
      selectedRest : '',

      meetup : {
        title : '',
        content : '',
        location : '',
        address : '',
        date : '',
        maxPersonnel : '',
        master : '',
        img : '',
        personalities : [],
      },

      dropdown_font: ["1", "2", "3", "4", "5", "6", "7", "8"],

      propsDialog : false,
      personalities : [
        "낙천적", "부정적", "내향적", "외향적", "충동적", "사교적",
        "대담함", "성실함", "냉정함", "온화함", "신중함", "게으름"
      ],
      isAllPropsSelect : false,


      targetLocation : {
        lat : '',
        lng : ''
      },
      geocoder : '',
    }
  },

  mounted() {
      this.meetup.master = this.$cookie.get('userId');

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
          this.targetLocation.lat = pos.coords.latitude;
          this.targetLocation.lng = pos.coords.longitude;
        });
      }

      if (window.kakao && window.kakao.maps) {
        // // console.log("이미 로딩됨");
        this.initMap();
      } else {
        // console.log("카카오맵 로딩");
        const script = document.createElement("script");
        /* global kakao */
        script.onload = () => kakao.maps.load(this.initMap);
        script.src =
          "https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=6192aa4fccde619ea3ac09491e57abd0&libraries=services";
        document.head.appendChild(script);
      }
  },
  methods: {
    setDate() {
      this.dateDialog1 = false;
      this.dateDialog2 = false;
      this.meetup.date = this.date + " " + this.time;
    },
    initMap() {
        // 좌표->주소
        this.geocoder = new kakao.maps.services.Geocoder();
    },
    doAllPropsSelect(){
      this.$nextTick( () => {
        if(this.meetup.personalities.length===this.personalities.length){
          this.isAllPropsSelect = false;
          this.meetup.personalities=[];
        }
        else{
          this.personalities.forEach(props => {
            if(this.meetup.personalities.indexOf(props) == -1){
              this.meetup.personalities.push(props)
            }
          });
          this.isAllPropsSelect = true;
        }
      })
    },
    meetUp() {
      // this.createMeetUpChat();

      // 밋업 등록 폼 유효성 검사
      if (this.$refs.form.validate()){

        // 밋업 성향 배열을 String으로 변환
        var personalities = this.meetup.personalities.toString();
        personalities = `[${personalities}]`;

        // 새로운 밋업 객체 생성      
        var newMeetup = {
          title :this.meetup.title,
          content :this.meetup.content,
          location :this.meetup.location,
          address :this.meetup.address,
          date :this.meetup.date,
          maxPersonnel :this.meetup.maxPersonnel,
          master :this.meetup.master,
          img :this.meetup.img,
          category :this.meetup.category,
          personalities : personalities,
          restId : this.selectedRest.id,
        }

        // REST ID 추가 프로세스
        // DB에 존재하지 않는 음식점이라면 DB에 음식점 등록 후 REST ID 반환
        if(this.selectedRest.id == null){

          // img src 배열을 String으로 변환 "[src1,src2,...,src5]"
          var imgSrcs = this.selectedRest.img.toString();
          imgSrcs = `[${imgSrcs}]`;

          // 새로운 음식점 객체 생성
          var newRest = {
            category : this.selectedRest.category,
            img : imgSrcs,
            like : this.selectedRest.like,
            location : this.selectedRest.location,
            name : this.selectedRest.name,
            review : this.selectedRest.review,
            scrap : this.selectedRest.scrap,
            telphone : this.selectedRest.telphone,
            url : this.selectedRest.url,
            dist  : this.selectedRest.dist
          };

          // DB에 음식점 정보 등록 후 restId 반환
          axios
          .post(`${SERVER_URL}/rest`, newRest)
          .then((response) => {
            this.selectedRest.id = response.data;
              newMeetup.restId = this.selectedRest.id;

              // 밋업 객체 DB 등록
              axios
                .post(`${SERVER_URL}/meetup`, newMeetup)
                .then((response) => {
                  alert("밋업 등록이 완료됐습니다.");
                  this.createMeetUpChat()
                  this.$router.push("/map")
                })
                .catch((error) => {
                  // console.log('error보기')
                })
          });
        }

        // DB에 존재하는 음식점이라면
        else{
          // 밋업 객체 DB 등록
          axios
            .post(`${SERVER_URL}/meetup`, newMeetup)
            .then((response) => {
              this.createMeetUpChat()
              alert("밋업 등록이 완료됐습니다.");
              this.$router.push("/map")
            })
            .catch((error) => {
              // console.log('error보기')
            })
        }
      }
    },

    //밋업에대한 채팅방 등록
    createMeetUpChat(){

      const newRoomRef = window.db.collection('test').doc();
                            // console.log(newRoomRef.id);
                            // window.db.collection('test').doc(newRoomRef.id).collection('test').doc();

                             var res = window.db.collection('test').doc(newRoomRef.id).set({
                                id :[this.meetup.master],
                                time : Date.now(),
                                name : this.meetup.title
                            }).catch(err =>{
                                // console.log(err);
                            })

                            alert("새로운 채팅방 생성");
                            return;
                          
    },
    
    checkForm() {
      if (this.meetup.title.length < 1) this.error.title = "제목을 입력해주세요.";
      else { this.error.title = false; return; }
      if ( this.meetup.content.length < 1 ) this.error.content = "내용을 입력해주세요.";
      else {this.error.content = false;
      return;}
      if (this.meetup.location.length < 1)
        this.error.location = "음식점을 입력해주세요.";
      else {this.error.location = false;
      return;}
      if (this.meetup.date.length < 1) this.error.date = "날짜를 입력해주세요.";
      else {this.error.date = false;
      return;}
      if (this.meetup.maxPersonnel.length < 1) this.error.personnel = "인원을 입력해주세요.";
      else {this.error.personnel = false;
      return;}
      if (this.meetup.personalities.length < 1) this.error.personality = "성향을 입력해주세요.";
      else {this.error.personality = false;
      return;}
    },
    search(){
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
              }
            });
          })
          this.restaurants = restaurants;
        })
        .catch((error) => {
          // console.log(error.response);
        })
    },
    select(restaurant){
      this.selectedRest = restaurant;
      this.meetup.location = restaurant.name;
      this.meetup.address = restaurant.location;
      this.meetup.img = restaurant.img[0];
      this.meetup.category = restaurant.category;
      this.dialog = false;
    },
  },

};
</script>

<style>
* {box-sizing: border-box;}
.back {
  color: white !important;
}
.nothome {
  padding: 0px !important;
}
.party {
  padding: 0px 12px;
}
.section .article {
  position: relative;
  color: black;
}
div {
  display: block;
}
.tit {
  position: absolute;
  top: 8px;
  left: 0;
  font-weight: bold;
}
.fa-chevron-left {
  color: white; 
  margin-left: 7px;
}
/* .btn-person{
  margin: 0px;
} */
.search-bar{
  margin-top: 8px;
}
.size-css {
  overflow: hidden;
}
.btns {
  text-align: center;
}
.input-container {
  display: flex;
  width: 100%;
  height: 95%;
  margin-bottom: 15px;
}

/* Style the form icons */
.icon {
  padding: 10px;
  background: #FFA500;
  color: white;
  min-width: 45px;
  text-align: center;
}

/* Style the input fields */
.input-field {
  width: 100%;
  padding: 8px;
}

.input-field:focus {
  border: 2px solid #FFA500;
}
.btnbtn { 
  font-size: 2rem; 
} 
</style>