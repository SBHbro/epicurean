
<template>
  <div class="map_wrap_1">
        <!-- SPEED DIAL -->
        <v-speed-dial
          v-model="fab"
          absolute
          direction="top"
          transition="slide-y-reverse-transition"
          retain-focus-on-click="true"
          style="bottom:200px; right:20px;"
        >
          <!-- BUTTON ACTIVATOR -->
          <template v-slot:activator>
            <v-btn
              v-model="fab"
              color="blue darken-2"
              dark small fab
            >
              <v-icon v-if="fab">mdi-close</v-icon>
              <v-icon v-else>mdi-dots-vertical</v-icon>
            </v-btn>
          </template>

          <!-- 현위치 버튼 -->
          <v-btn fab dark small color="pink" @click="moveCurPosition">
            <v-icon color="white">mdi-map-marker</v-icon>
          </v-btn>

          <!-- 밋업 생성 페이지 이동 버튼 -->
          <!-- router.push({name : "JoinInfo", params : userInfo}); -->
          <v-btn fab dark small color="green" @click="$router.push({name : 'CreateMeetup', params : targetLocation})">
              <v-icon color="white" >mdi-pencil-plus</v-icon>
          </v-btn>

          <!-- 밋업 필터링 버튼 -->
          <v-btn fab small dark color="indigo" @click="menu=true">
            <v-icon color="white">mdi-tune-variant</v-icon>
          </v-btn>
        </v-speed-dial>

        <!-- 필터창  -->
          <div class="text-center">
            <v-menu
              transition="slide-y-transition"
               v-model="menu"
              :close-on-content-click="false"
              :min-width="375" :max-width="375"
              :position-y="235"
              :position-x="574"
              z-index="100"

            >
            <!-- POP OVER -->
              <v-card class="mx-auto">
                <v-list color="">
                  <v-list-item>
                    <v-list-item-content>
                      <v-list-item-title class="text-h6">밋업 필터</v-list-item-title>
                      <v-list-item-subtitle>취향에 맞게 밋업을 검색해보세요 :)</v-list-item-subtitle>
                    </v-list-item-content>
                  </v-list-item>
                </v-list>
                <v-divider class="my-0"></v-divider>
                <v-list>
                  <!-- 선호 음식 -->
                  <v-list-item>
                    <v-dialog v-model="modalFoods" :max-width="330">
                      <template v-slot:activator="{ on, attrs }">
                        <v-combobox
                          ref="select1"
                          v-model="selectedFoods"
                          label="선호음식을 선택해주세요"
                          multiple small-chips
                          prepend-icon="mdi-food"
                          v-on="on" v-bind="attrs"
                          auto clearable
                        >
                             <template v-slot:selection="data">
                                <v-chip
                                  color="primary" small
                                >
                                  <span class="mr-2">
                                    {{data.item}}
                                  </span>
                                  <v-icon small @click="data.parent.selectItem(data.item)">
                                    mdi-close
                                  </v-icon>
                                </v-chip>
                            </template>
                        </v-combobox>
                      </template>

                      <!-- FOOD DIALOG -->
                      <v-card>
                        <v-card-title class="subtitle">선호음식
                          <v-spacer></v-spacer>
                          <v-btn outlined rounded small
                            class="text-caption font-weight-bold"
                            :class="{'primary--text': isAllFoodsSelect}"
                            @click="doAllFoodsSelect"
                          >
                            <v-icon small>mdi-check</v-icon>                            
                            전체
                          </v-btn>
                          <!-- <v-btn icon>
                            <v-icon>mdi-close</v-icon>
                          </v-btn> -->
                        </v-card-title>
                        
                        <v-divider class="mt-1 mb-6"></v-divider>
                        <v-card-text>
                          <v-btn-toggle v-model="selectedFoods" multiple color="primary">
                            <v-row justify="center">
                              <v-btn tile outlined large v-for="food in foods" :key="food" :value="food"
                              active-class="primary--text">
                                {{food}}
                              </v-btn>
                            </v-row>
                          </v-btn-toggle>
                        </v-card-text>
                        <v-card-actions>
                          <v-btn block color="primary" @click="modalFoods = false">확인</v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-list-item>

                  <!-- 성향 -->
                  <v-list-item>
                    <v-dialog v-model="modalProps" :max-width="330">
                      <template v-slot:activator="{ on, attrs }">
                        <v-combobox
                          ref="select2"
                          v-model="selectedProps"
                          label="성향을 선택해주세요"
                          multiple small-chips
                          prepend-icon="mdi-heart"
                          v-on="on" v-bind="attrs"
                          auto clearable
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

                      <!-- PERSONALITIES DIALOG -->
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
                          <!-- <v-btn icon>
                            <v-icon>mdi-close</v-icon>
                          </v-btn> -->
                        </v-card-title>
                        
                        <v-divider class="mt-1 mb-6"></v-divider>
                        <v-card-text>
                          <v-btn-toggle v-model="selectedProps" multiple color="primary">
                            <v-row justify="center">
                              <v-btn tile outlined large v-for="props in personalities" :key="props" :value="props"
                              active-class="primary--text">
                                {{props}}
                              </v-btn>
                            </v-row>
                          </v-btn-toggle>
                        </v-card-text>
                        <v-card-actions>
                          <v-btn block color="primary" @click="modalProps = false">확인</v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-list-item>

                  <!-- 밋업 기간 -->
                  <v-list-item>
                    <v-dialog
                      ref="dialog" v-model="modalCalendar"
                      :return-value.sync="dates"
                      persistent width="290px"
                    >
                      <template v-slot:activator="{ on, attrs }">
                        <v-text-field
                          prepend-icon="mdi-calendar-month-outline"
                          v-model="dateRangeText"
                          label="기간을 선택해주세요"
                          readonly
                          v-bind="attrs"
                          v-on="on"
                        ></v-text-field>
                      </template>

                      <v-date-picker v-model="dates" range scrollable show-current no-title
                        :min="new Date().toISOString().substr(0, 10)"
                        max="2050-01-01"
                      >
                        <v-spacer></v-spacer>
                        <v-btn text color="primary" @click="modalCalendar = false">Cancel</v-btn>
                        <v-btn text color="primary" @click="setDate">OK</v-btn>
                      </v-date-picker>
                    </v-dialog>
                  </v-list-item>

                  <!-- 밋업 인원 -->
                  <p class="ml-12 mb-n2 text-caption">인원을 선택해주세요</p>
                  <v-list-item>
                      <v-range-slider
                        v-model="personnel"
                        track-color="grey"
                        prepend-icon="mdi-account-group"
                        :tick-labels="['2','3','4','5','6','7','8']"
                        min="2" max="8"
                        ticks="always" tick-size="2"
                      >
                      </v-range-slider>
                  </v-list-item>
                </v-list>

                <!-- POP OVER FOOTER-->
                <v-card-actions>
                  <v-spacer></v-spacer>
                  <v-btn text @click="menu = false">닫기</v-btn>
                  <v-btn color="primary" text @click.stop="doFilter" @click="menu=false">적용</v-btn>
                </v-card-actions>
              </v-card>
            </v-menu>
          </div>





    <div id="map1" style="position:absolute">map</div>
    <!-- <div id="menu_wrap" class="bg_white"> -->
        <!-- <div class="option"> -->
            <div>
              <v-text-field @keyup.enter="search" v-model="keyword"
              placeholder="지역을 입력하세요.."
              append-icon="mdi-magnify"
              background-color="white"
              solo rounded clearable
              style="position:absolute; left:10px; top:10px; z-index:20; opacity:.8;"
              > 
              </v-text-field>

              <!-- <div class="search-box">
                <input type="text" />
                <span></span>
              </div> -->
            </div>


            <!-- snackbar -->
             <v-snackbar
              v-model="snackbar"
              :color="snackbarColor"
              timeout="3000"
            >
              {{snackbarText}}

              <template v-slot:action="{ attrs }">
                <v-btn
                  dark
                  text
                  v-bind="attrs"
                  @click="snackbar = false"
                >
                  닫기
                </v-btn>
              </template>
            </v-snackbar>
        <!-- </div> -->
    <!-- </div> -->
    <div class="custom_zoomcontrol radius_border">
      <span @click="zoomIn">
        <img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_plus.png" alt="확대" />
      </span>
      <span @click="zoomOut">
        <img src="https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/ico_minus.png" alt="축소" />
      </span>
    </div>

  </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const CURLAT = 36.3587222, CURLNG = 127.3439205;
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data: () => {
    return {
      map: "",
      targetLocation :{
        lat : '',
        lng : '',
      },
      keyword : '',
      meetups : '',
      markers : [],
      overlays : [],

      isAllFoodsSelect : false,
      isAllPropsSelect : false,
      modalFoods : false,
      foods: ['한식', '중식', '양식', '일식', '분식', '뷔페'],
      selectedFoods : [],

      modalProps : false,
      personalities : [
        "낙천적", "부정적", "내향적", "외향적", "충동적", "사교적",
        "대담함", "성실함", "냉정함", "온화함", "신중함", "게으름"
      ],
      selectedProps : [],
      
      
      modalCalendar: false,
      dates: [new Date().toISOString().substr(0, 10),new Date().toISOString().substr(0, 10)],

      personnel : [2,8],

      menu: false,
      fab : false,

      snackbar : false,
      snackbarText : '',
      snackbarColor : '',

      geocoder : '',
      curRegion : '',
    };
  },
  computed: {
    dateRangeText () {
      if(this.dates[0] > this.dates[1]){
        this.swap(this.dates);
      }
      return this.dates.join(' ~ ')
    }
  },
  watch : {
  },
  mounted() {
    // 현재 위치 확인
    if (navigator.geolocation && Object.keys(this.$route.params).length == 0) {
      navigator.geolocation.getCurrentPosition(pos => {
        this.targetLocation.lat = pos.coords.latitude;
        this.targetLocation.lng = pos.coords.longitude;
      });
    }

    // 카카오 맵 로딩
    if (window.Kakao && window.Kakao.maps) {
      console.log("이미 로딩됨");
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "https://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=6192aa4fccde619ea3ac09491e57abd0&libraries=services";
      document.head.appendChild(script);
    }
  },
  methods: {
    clearMarkersAndOverays(){
      for(let i=0; i<this.markers.length;i++){
        this.markers[i].setMap(null);
      }
      this.markers = [];
      this.overlays = [];
    },

    // 지역(구)의 변경감지하는 메소드
    checkChangeRegion(result, status){
      if(status == kakao.maps.services.Status.OK){
        let newRegion = result[0].region_2depth_name;
        if(this.curRegion != newRegion){
          this.curRegion = newRegion;
          this.clearMarkersAndOverays();
          this.getNearMeetups(result[0]);
          this.showSnackBar("[ " + result[0].region_1depth_name + " " + newRegion + " ] 를 조회합니다 :)", "success");
        }
      }
    },
    // 좌표로 행정동 주소 정보를 요청합니다
    searchAddrFromCoords(coords, callback) {
      this.geocoder.coord2RegionCode(coords.getLng(), coords.getLat(), callback);         
    },

    // 좌표로 법정동 상세 주소 정보를 요청합니다
    searchDetailAddrFromCoords(coords, callback) {
      this.geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    },
    showSnackBar(content, color){
      this.snackbarText = content;
      this.snackbarColor = color;
      this.snackbar = true;
    },
    setDate(){
      if(this.dates.length == 1){
        this.dates.push(this.dates[0]);
      }
      this.$refs.dialog.save(this.dates);
    },
    doAllFoodsSelect(){
      this.$nextTick( () => {
        if(this.selectedFoods.length===this.foods.length){
          this.isAllFoodsSelect = false;
          this.selectedFoods=[];
        }
        else{
          this.foods.forEach(food => {
            if(this.selectedFoods.indexOf(food) == -1){
              this.selectedFoods.push(food)
            }
          });
          this.isAllFoodsSelect = true;
        }
      })
    },
    doAllPropsSelect(){
      this.$nextTick( () => {
        if(this.selectedProps.length===this.personalities.length){
          this.isAllPropsSelect = false;
          this.selectedProps=[];
        }
        else{
          this.personalities.forEach(props => {
            if(this.selectedProps.indexOf(props) == -1){
              this.selectedProps.push(props)
            }
          });
          this.isAllPropsSelect = true;
        }
      })
    },
    doFilter(){
      this.meetups.forEach((meetup, index) => {
        var isPersonalites = false;
        var isDate = false;
        var isPersonnel = false;
        var isCategory = false;
        // 카테고리 필터링
        for(let i = 0; i< this.selectedFoods.length; i++){
          // 밋업이 필터 카테고리를 하나라도 포함하고 있으면
          if(meetup.category == this.selectedFoods[i]){
            isCategory = true;
            break;
          }
        }
        // 성향 필터링
        for(let i = 0; i<this.selectedProps.length; i++){
          // 밋업이 필터성향을 하나라도 포함하고 있으면
          if(meetup.personalities.indexOf(this.selectedProps[i]) > -1){
            isPersonalites = true;
            break;
          }
        }

        // 인원 필터링
        if(meetup.curPersonnel >= this.personnel[0] && meetup.curPersonnel <= this.personnel[1]){
          isPersonnel = true;
        }
        
        // 날짜 필터링
        if(new Date(meetup.date.slice(0, 16)) >= new Date(this.dates[0]) && new Date(meetup.date.slice(0, 16)) <= new Date(this.dates[1])){
          isDate = true;
        }
        
        if(isPersonalites && isDate && isPersonnel && isCategory){
          this.markers[index].setMap(this.map)
        }
        else{
          this.markers[index].setMap(null)
        }
        this.showSnackBar("필터가 설정되었습니다. :)", "info");

      });

      
    },
    moveCurPosition(){
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
          var coords = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
          this.map.panTo(coords);
        });
        this.showSnackBar("현재 위치로 이동합니다.", "info");
     }
    },
    swap(dates) {
        let temp = this.dates[0];
        this.dates[0] = this.dates[1];
        this.dates[1] = temp;
    },
    initMap() {
      if(Object.keys(this.$route.params).length > 0){
        this.targetLocation.lat = this.$route.params.lat;
        this.targetLocation.lng = this.$route.params.lng;
      }


      // map 초기설정
      var container = document.getElementById("map1");
      var options = {
        center: new kakao.maps.LatLng(this.targetLocation.lat, this.targetLocation.lng),
        level: 3
      };
      this.map = new kakao.maps.Map(container, options);
      this.geocoder = new kakao.maps.services.Geocoder();

      // 페이지 시작 시, 현재 지역(구) 정보를 저장
      this.geocoder.coord2RegionCode(this.map.getCenter().getLng(), this.map.getCenter().getLat(), (result, status)=>{
        this.curRegion = result[0].region_2depth_name;
      });         
      
      // Map Zoom In/Out 또는 중심 좌표 변경 시
      // 바뀐 지역(구) 정보 조회해서
      // 현재 지역과 바뀐 지도의 지역이 다르면
      // 해당 지역의 밋업 리스트 불러오기
      kakao.maps.event.addListener(this.map, 'idle', () => {
        this.targetLocation.lat = this.map.getCenter().getLat();
        this.targetLocation.lng = this.map.getCenter().getLng();
        this.searchAddrFromCoords(this.map.getCenter(), this.checkChangeRegion);
      })

      // 좌표->주소
      this.geocoder.coord2Address(CURLNG, CURLAT, (result, status) => {
        if(status === kakao.maps.services.Status.OK){

          // 현재 주소
          var address = result[0].address;
          if(result[0].road_address){
            address = result[0].road_address;
          }

          // 해당 위치 주변의 밋업 리스트          
          this.getNearMeetups(address);
        }
      });
    },
    zoomIn() {
      this.map.setLevel(this.map.getLevel() - 1);
    },
    zoomOut() {
      this.map.setLevel(this.map.getLevel() + 1);
    },
    toggleInfoWindow(map, marker, overlay, overlays) {
      return function() {
        // 떠있는 인포윈도우 다 제거
        overlays.forEach(element => {
            if (element != overlay && element.getMap() != null) {
              element.setMap(null);
            }
        });

        // 클릭한 마커의 인포윈도우만 띄우기
        if (overlay.getMap() == null) {
          overlay.setMap(map);
        } else {
          overlay.setMap(null);
        }
      }
    },
    search(){
        if (!this.keyword.replace(/^\s+|\s+$/g, '')) {
          alert('키워드를 입력해주세요!');
          return false;
        }
        // 장소 검색 객체를 생성합니다
        // var geocoder = new kakao.maps.services.Geocoder();
        
        // 주소로 좌표를 검색합니다
        this.geocoder.addressSearch(this.keyword, (result, status) => {

            // 정상적으로 검색이 완료됐으면 
            if (status === kakao.maps.services.Status.OK) {
                var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                this.showSnackBar("[ " + this.keyword + " ] 으로 이동합니다 :)", "info");
                // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
                this.map.setCenter(coords);
            } else{
              this.showSnackBar("없는 지역이예요.. 다시 확인해주세요 :(", "error");
            }
        });    
    },
    getNearMeetups(address){
      // console.log(address);
      axios
            .get(`${SERVER_URL}/meetup/search/location/${address.region_1depth_name.slice(0, 2)} ${address.region_2depth_name}`)
            .then((response) => {
              // 밋업 리스트
              // console.log(response)
              if(response.data.data == "success"){
                this.meetups = response.data.object;
                    // console.log(this.meetups)
                    var imageSrc = "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/markerStar.png"; 
                    this.meetups.forEach(meetup => {
                      // // console.log(meetup)
                      this.geocoder.addressSearch(meetup.address, (result, status) => {
                          if (status === kakao.maps.services.Status.OK) {
                              var coords = new kakao.maps.LatLng(result[0].y, result[0].x);
                              // marker 생성
                              var markerImage = new kakao.maps.MarkerImage(imageSrc, new kakao.maps.Size(24, 35));
                              var marker = new kakao.maps.Marker({
                                title: meetup.title,
                                position: coords,
                                image : markerImage,
                                clickable: true,
                              });

                              // custom overlay infowindow 생성
                              var overlay = new kakao.maps.CustomOverlay({    
                                position: marker.getPosition(),
                                content: 
                                  `
                                  <div class="overlay_wrap">
                                    <div class="overlay_info">
                                        <a href="#/map/detailMeetup?meetupId=${meetup.id}&lat=${this.targetLocation.lat}&lng=${this.targetLocation.lng}"><strong>${meetup.title}</strong></a>
                                        <div class="desc">
                                            <img src="${meetup.img}" width="56" height="56" alt="">
                                            <div class="date"><label>일시 : </label> ${meetup.date.slice(0, 16)}</div>
                                            <div class="address"><label>위치 : </label> ${meetup.location}</div>
                                            <div class="personnel"><label>인원 : </label> ${meetup.curPersonnel} / ${meetup.maxPersonnel}</div>
                                        </div>
                                    </div>
                                  </div>
                                   `
                              });
                              this.overlays.push(overlay);
                              this.markers.push(marker);
                              kakao.maps.event.addListener(marker, "click", this.toggleInfoWindow(this.map, marker, overlay, this.overlays));
                              marker.setMap(this.map);
                              
                          } 
                      });    
                        
                    });

              }
          
   

            })
    }
  }
}


</script>


<style>
#map1 {
  width: 100%;
  height: 969px;
}

.map_wrap_1 {
  overflow: hidden;
  width: 100%;
  height: 969px;

}
.radius_border {
  border: 1px solid #919191;
  border-radius: 5px;
}
.custom_zoomcontrol {
  position: absolute;
  bottom: 100px;
  right : 20px;
  width: 36px;
  height: 80px;
  overflow: hidden;
  z-index: 15;
  background-color: #f5f5f5;
}
.custom_zoomcontrol span {
  display: block;
  width: 34px;
  height: 40px;
  text-align: center;
  cursor: pointer;
}
.custom_zoomcontrol span img {
  height: 35px;
  padding: 12px 0;
  border: none;
}
.custom_zoomcontrol span:first-child {
  border-bottom: 1px solid #bfbfbf;
}

.overlay_wrap {position: absolute;top:-177px;left:-143px;}
.overlay_info {width:288px;height:133px;border-radius: 6px; margin-bottom: 12px; float:left;position: relative; border: 1px solid #ccc; border-bottom: 2px solid #ddd;background-color:#fff;}
.overlay_info:nth-of-type(n) {border:0; box-shadow: 0px 1px 2px #888;}
.overlay_info a {display: block; background: #FFA726 url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/arrow_white.png) no-repeat right 14px center; text-decoration: none; color: #fff; padding:12px 36px 12px 14px; font-size: 14px; border-radius: 6px 6px 0 0}
.overlay_info a strong {background:url(https://image.flaticon.com/icons/svg/581/581725.svg) no-repeat; background-size:16px 16px;color:white; padding: 0 0 0 20px;}
.overlay_info .desc {padding:14px;position: relative; min-width: 190px; height: 56px}
.overlay_info img {vertical-align: top;}
.overlay_info .date {font-size: 12px; color: #333; position: absolute; left: 80px; right: 14px; top: 16px; white-space: normal}
.overlay_info .address {font-size: 12px; color: #333; position: absolute; left: 80px; right: 14px; top: 32px; white-space: normal}
.overlay_info .personnel {font-size: 12px; color: #333; position: absolute; left: 80px; right: 14px; top: 48px; white-space: normal}
.overlay_info:after {content:'';position: absolute; margin-left: -11px; left: 50%; bottom: -12px; width: 22px; height: 12px; background:url(https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png) no-repeat 0 bottom;}





</style>

