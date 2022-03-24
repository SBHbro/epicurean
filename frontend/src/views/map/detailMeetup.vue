<template>

  <div class="user join">
      <v-toolbar-title>
        <v-toolbar class="mb-1" dense elevation="1">
          <v-icon @click="$router.push({name : 'Map', params : {lat : $route.query.lat, lng : $route.query.lng}})">
            mdi-arrow-left
          </v-icon>
          <v-spacer></v-spacer>
          <p class="my-auto">Meet Up</p>
          <v-spacer></v-spacer>
          <GoChatForMeetUp :meetup ="meetup" />
          <div>
          <button @click="dialog = true">
            <div style="padding: 2px; width: 24px; height: 24px;">
              <i class="fas fa-ellipsis-v"></i>
            </div>  
          </button>
          <v-dialog v-model="dialog" max-width="190">
            <v-list>
              <v-list-item>
                <v-btn block text color="orange" v-if="isGuest" @click="cancleMeetup">{{text}}</v-btn>
                <v-btn block text color="orange" v-else-if="meetup.curPersonnel==meetup.maxPersonnel" disabled>{{text}}</v-btn>
                <v-btn block text color="orange" v-else-if="isRequest" @click="cancleRequestMeetup">{{text}}</v-btn>
                <v-btn block text color="orange" v-else @click="requestDialog = true">{{text}}</v-btn>
              </v-list-item>
            </v-list>
          </v-dialog>
          </div>
        </v-toolbar>
      </v-toolbar-title>
    
      <!-- 입력 폼 -->
      <div class="party wrapC">

        <!-- 밋업 타이틀 -->
          <v-col class="pb-0">
            <v-text-field
              outlined hide-details="auto" 
              v-model="meetup.title"
              label="제목"
              readonly
            >          
            </v-text-field>
          </v-col>

          <!-- 밋업 내용 -->
          <v-col class="pb-0">
            <v-textarea
              outlined hide-details="auto" 
              v-model="meetup.content"
              label="내용"
              readonly
            >          
            </v-textarea>
          </v-col>
        <!-- 밋업 장소 -->
        <v-col class="pb-0">
          <v-text-field
            outlined hide-details="auto" 
            slot="activator"
            v-model="meetup.location"
            readonly
            label="장소"
          ></v-text-field>
        </v-col>

        <!-- 밋업 날짜 -->
        <v-col class="pb-0">
        <v-text-field
          outlined
          hide-details
          v-model="meetup.date"
          label="날짜"
          readonly
        ></v-text-field>
        </v-col>

        <!-- 밋업 인원 -->
        <v-col class="pb-0">
        <v-text-field 
        readonly
        class="my-0 pa-0"
        label="인원"
        hide-details
        outlined
        :value="meetup.curPersonnel + ' / ' + meetup.maxPersonnel "
        >
        </v-text-field>
        </v-col>

        <!-- 밋업 성향 -->
          <v-col class="pb-0">
            <v-combobox
              outlined
              hide-details="auto"
              v-model="meetup.personalities" 
              multiple small-chips
              label="성향"
              auto 
              readonly
            >
            <template v-slot:selection="data">
            <v-chip class="mr-1 px-2"
              color="primary" small
            >
            <span class="mr-1">
                        {{data.item}}
                      </span>
            </v-chip>
            </template>
          </v-combobox>
          </v-col>
      </div>
      <!-- 밋업 참석 신청 DIALOG -->
      <v-dialog v-model="requestDialog" max-width="290">
          <v-card>
            <v-card-title class="text-title">밋업 요청 메세지
              <v-spacer></v-spacer>
              <v-btn icon>
                <v-icon>mdi-close</v-icon>
              </v-btn>

            </v-card-title>
            <v-divider class="mt-1 mb-6"></v-divider>
            <v-card-text>
              <v-textarea v-model="requestMessage" color="orange lighten-1">
                <template v-slot:label>
                  <div class="small">
                    밋업 요청 메세지를 작성해주세요 :)
                  </div>
                </template>
              </v-textarea>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>

              <v-btn text @click="requestDialog = false">
                닫기
              </v-btn>

              <v-btn color="orange lighten-1" text
                @click="requestMeetup"
                @click.stop="requestDialog = false"
              >
                신청하기
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import GoChatForMeetUp from '../../components/common/GoChatForMeetUp';

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  components:{
    GoChatForMeetUp
  },
  data: () => {
    return {
      meetup : '',
      requestDialog : false,
      requestMessage : '',
      text : '참석신청',
      isGuest : false,
      isRequest : false,
      mynickname : '',
      dialog: false,
    }
  },
  created() {


     //유저의 닉네임 가져오기
     axios
      .post(`${SERVER_URL}/chat/nickname`,[this.$cookie.get('userId')])
      .then((response) => {
       this.mynickname = response.data[0];
      })
      .catch((error) => {
        // console.log(error.response);
      });

    var meetupId = this.$route.query.meetupId;
    
    /* 밋업 정보 불러오기 */
    this.init();

    /* 해당 밋업에 참석 신청중인지 확인 */
    axios
    .get(`${SERVER_URL}/meetup/request/${meetupId}`)
    .then((response) => {
      if(response.data == 'success'){
        let requests = response.data.object; 
        requests.forEach(request => {
          // if(request.guestId == "70"){
          if(request.guestId == this.$cookie.get("userId")){
            this.isRequest = request.id;
            this.text = "신청취소";
          }
        });
      }
    })

    /* 해당 밋업의 멤버 리스트 조회해서 참석중인지 확인 */
    axios
    .get(`${SERVER_URL}/meetup/members/${meetupId}`)
    .then((response) => {
      let guests = response.data.object;
      guests.forEach(guest => {
        // if(guest.userId == "70"){
        if(guest.userId == this.$cookie.get("userId")){
          this.isGuest = true;
          this.text = "참석취소";
        }
      });
    })  


  },
  methods: {
    init(){
      var meetupId = this.$route.query.meetupId;
      axios
      .get(`${SERVER_URL}/meetup/search/${meetupId}`)
      .then((response) => {
          let meetup = response.data;
          meetup.personalities = this.stringToArray(meetup.personalities);
          this.meetup = meetup;
      })
    },
    cancleMeetup(){
      // console.log(this.meetup)
      if(this.meetup.master == this.$cookie.get("userId")){
        this.$alert("밋업주최자는 참여취소가 불가합니다.")
      }
      else{
        var meetupId = this.$route.query.meetupId;
        axios
        .delete(`${SERVER_URL}/meetup?meetupId=${meetupId}&userId=${this.$cookie.get("userId")}`)
        .then((response) => {
          this.isGuest = false;
          this.isRequest = false;
          this.text = "참석신청";
          this.init();
          alert("밋업 참석이 취소되었습니다.");
        })  
      }
    },
    cancleRequestMeetup(){
      // console.log(this.isRequest)
      axios
      .delete(`${SERVER_URL}/meetup/request/${this.isRequest}`)
      .then((response) => {
        this.isRequest = false;
        this.text = "참석신청";
        alert("밋업 참석 신청이 취소되었습니다.");
      })  
    },
    requestMeetup(){
      let request = {
        meetupId : this.meetup.id,
        hostId : this.meetup.master,
        guestId : this.$cookie.get("userId"),
        // guestId : '70',
        requestMessage : this.requestMessage,
      };

       window.db.collection('alarm').doc('meetup').collection('messages').add({
                        to : request.hostId,
                        from : this.$cookie.get('userId'),
                        message: this.mynickname+"님이 "+this.meetup.title+"밋업에 참가요청을 보냈습니다.",
                        time: Date.now(),
                        meetupid : request.meetupId,
                        confirm : false
                    }).catch(err => {
                        // console.log(err);
                    });

      // // console.log(request);
      axios
      .post(`${SERVER_URL}/meetup/request`, request)
      .then((response) => {
        alert("신청이 완료되었습니다.")
        this.$router.push({name:'Map', params: ''});
      })  
    },
    stringToArray(strings){
        strings = strings.replace('[', '');
        strings = strings.replace(']', '');
        strings = strings.replace(/(\s*)/g, ''); // 모든공백제거
        strings = strings.split(",");
        return strings;
    }
    
  },

};
</script>

<style>
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
</style>