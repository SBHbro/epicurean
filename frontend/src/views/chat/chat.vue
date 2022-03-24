<template>
<div style = "padding :0 ">


      <!-- 상단 툴바 -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">{{this.room.name}}</p>
      <v-spacer></v-spacer>
    </v-toolbar>


   <div class="container chat">
        <!-- <h4 class="text-primary text-center">{{ this.room.name }}</h4> -->
        <div class="card">
            <div class="card-body">
                <p class="text-secondary nomessages" v-if="messages.length == 0">
                    [No messages yet!]
                </p>
                <!-- {{messeages}}아니 -->
                <div class="messages" v-chat-scroll="{always: false, smooth: true}">
                    <div v-for="(message,i) in messages" :key="i">
                        <div v-if="message.userid == userid" style="text-align : right" class = "myMsg">
                            <!-- <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
                                <img
                                :src="`https://don.sbbro.shop/api/img/user?imgname=`+message.img"
                                />
                            </v-list-item-avatar>
                             <span class="text-info">[{{ message.nickname }}]: </span> -->
                            <span class="msg">{{message.message}}</span>
                            <span class="text-secondary time">{{message.time}}</span>
                        </div>
                        <div v-else style = "text-align : left" class="anotherMsg">
                            <v-list-item-avatar size ="25" style="cursor:pointer;" @click="gotoProfile(lst)">
                                <img
                                :src="`https://don.sbbro.shop/api/img/user?imgname=`+message.img"
                                />
                            </v-list-item-avatar>
                            <span class="" style ="font-size: 1.1em; color= gray;">{{ message.nickname }}<br></span>
                            <span class="msg">{{message.message}}</span>
                            <span class="text-secondary time">{{message.time}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card-action">
                <CreateMessage :id="userid" :rid="this.room.rid" :to="this.room.to" :myNickName="this.myNickName" :roomName="this.room.name" />
            </div>
        </div>
    <br><br>
    </div>
</div>
</template>

<script>
import CreateMessage from '../../components/common/CreateMessage';
import moment from 'moment';

import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";



export default {
    props: ['room'],
    components: {
        CreateMessage
    },
    data(){
        return {
            // room :{},
            messages : [],
            userid : this.$cookie.get('userId'),
            // to : []
            myNickName : '',
            userImg :[],

        }
    },
    created(){
                this.getimg();
              var confirmChat = confirm("대화하시겠습니까?");
                if(confirmChat == false){
                    this.$router.go(-1);
                }
              this.view(this.room);

                    //접속한 방의 알림을 모두 읽음으로 변경.
                      window.db.collection('alarm').doc('chat').collection('messages').where('roomid','==',this.room.rid).where('to','==',this.userid).get()
                      .then(snapshot =>{
                          if(snapshot.empty) {
                            //   // console.log('채팅이 없다')
                          }

                          snapshot.forEach(doc =>{
                             window.db.collection('alarm').doc('chat').collection('messages').doc(doc.id).set({
                                    confirm : true
                                },{merge:true}).catch(err => {
                                    // // console.log(err);
                                });
                          })
                      })

                 

              //내 닉네임 구하는 부분
               axios
                            .post(
                                `${SERVER_URL}/chat/nickname`,[this.userid]
                            ).then((response) =>{
                                // // console.log('내 닉네임');
                                // // console.log(response);
                                this.myNickName = response.data[0];
                                // // console.log(this.myNickName);
                            }).catch((err) =>{

                            })

            },
    methods:{
        getimg(){
                 //axios로 id배열 보내서 프로필이미지 배열로 바꿔온다.
                          axios
                            .post(
                                `${SERVER_URL}/userpage/getuserpost`,this.room.id
                            )
                            .then((response) => {
                                // // console.log('응답',response);
                                this.userImg = response.data;
                           })
                            .catch((error) => {
                                // // console.log(error.response);
                            });
        },
        view(room){
            // // console.log('이미지',this.userImg);
             window.db.collection('chat').doc(this.room.rid).collection('messages').orderBy('time').onSnapshot(snapshot=>{
                    
                  snapshot.docChanges().forEach(change =>{
                    if (change.type == 'added'){
                    //   // console.log(change.doc.data());
                      var doc = change.doc;

                      //닉네임 구하기
                      var id = doc.data().from;
                      var num = this.room.id.indexOf(id*1);   
                      if(num == -1){
                          num = this.room.id.indexOf(id);
                      }    
             
                    //   // console.log("--------------------")
                    //   // console.log(id)
                    //   // console.log(typeof(id));
                    //   // console.log(num)              
                    //   // console.log(this.room.id)
                    //   // console.log(this.room.nickName[num])
                    //   // console.log(this.room.nickName)

                    

                      var ms ={
                        message : doc.data().message,
                        nickname : this.room.nickName[num],
                        time : moment(doc.data().time).format('LLL'),
                        userid : doc.data().from,
                        img : this.room.img[num]
                        // to : to
                      }
                    //   // console.log('ms');
                    //   // console.log(ms);

                      this.messages.push(ms);

                    }
                  })
                })
        }
    }

}
</script>

<style>
.card-body{
    max-height : 667px;
    min-height: 447px;
}

.chat h2{
    font-size: 2.6em;
    margin-bottom: 0px;
}
.chat h5{
    margin-top: 0px;
    margin-bottom: 40px;
}
.chat span{
    font-size: 1.2em;
}
.chat .time{
    display: block;
    font-size: 0.7em;
}
.messages{
    max-height: 647px;

    /* height : 400px; */
    overflow: auto;
}

.myMsg {
    text-align: right;
}

.anotherMsg {
    text-align: left;
    margin-bottom: 5px;
}

.msg {
    display: inline-block;
    border-radius: 15px;
    padding: 7px 15px;
    margin-bottom: 10px;
    margin-top: 5px;

}

.anotherMsg > .msg {
    background-color: #f1f0f0;

}

.myMsg > .msg {
    background-color: #0084FF;
    color: #fff;
}
</style>