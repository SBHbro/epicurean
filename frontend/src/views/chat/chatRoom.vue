<template>
  <div>
         <!-- 상단 툴바 -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">채팅</p>
      <v-spacer></v-spacer>
    </v-toolbar>

                 <v-list three-line v-for="(item, index) in chatroom" :key='index' style ="padding-top: 1px; padding-bottom: 1px;">
                    <template >
                        
                        <v-list-item
                           @click="chatGo(item)"
                        >
                        <v-list-item-avatar>
                            <v-img :src="`https://don.sbbro.shop/api/img/user?imgname=`+item.viewImg"></v-img>
                        </v-list-item-avatar>

                        <v-list-item-content>
                            <v-list-item-title > {{item.name}} <span class="grey--text text--lighten-1">{{item.id.length}}</span> </v-list-item-title>
                            <v-list-item-subtitle>{{item.lastMessage}}&mdash; <span class='text--primary'>{{item.time | moment("from","now")}}</span></v-list-item-subtitle>
                        </v-list-item-content>
                        </v-list-item>
                        <v-divider style="margin-top: 1px; margin-bottom: 1px;"
                        :key="index"
                        ></v-divider>
                    </template>
                    </v-list>

            <!-- </v-card> -->
  </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";
import moment from 'moment';

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";


export default {

    data(){
        return{
            chatroom : [],
            name : '',
            responseNickName : []
        }
    },
    beforeCreate(){
        window.db.collection('test').where('id', 'array-contains', this.$cookie.get('userId')).orderBy('update','desc').get()
                 .then(snapshot=>{
                  //없을경우
                      if(snapshot.empty){
                        // console.log("없다");
                        return;
                        }

                    //있을경우
                      snapshot.forEach(doc=>{

                          //axios로 id배열 보내서 닉네임 배열로 바꿔온다.
                          axios
                            .post(
                                `${SERVER_URL}/chat/nickname`,doc.data().id
                            )
                            .then((response) => {
                                this.responseNickName = response.data;
                                // // console.log(this.responseNickName);
                                var chatName;
                                    //수신하는 사람
                                    var to = doc.data().id;
                                    var idx = to.indexOf(this.$cookie.get('userId')) 
                                    if (idx > -1) to.splice(idx, 1)
                                    // // console.log('수신인');
                                    // // console.log(to)
                                //채팅방이름이 Room일경우 (1대1 대화일 경우)
                                // // console.log(doc.data().name);
                                    var arrayforname = doc.data().id;
                                    var toforname = arrayforname.indexOf(to[0]);
                                if(doc.data().name == 'Room')
                                {
                                    // // console.log(doc.data().id.indexOf('2'));
                                    chatName = this.responseNickName[toforname];
                                    // // console.log(arrayforname,toforname,chatName,to)
                                }
                                //아닐경우 -> firestore에 저장된 이름
                                else{
                                    chatName = doc.data().name;
                                }



                                var chatData = {
                                name : chatName,
                                id : doc.data().id,
                                lastMessage : doc.data().lastMessage,
                                rid : doc.id,
                                nickName : this.responseNickName,
                                to : to,
                                time : doc.data().update,
                                // time : moment(doc.data().update).format('LLL'),
                                }
                            // // console.log(chatData);
                             axios.post(
                                `${SERVER_URL}/userpage/getuserpost`,chatData.id
                            )
                            .then((response) => {
                                // // console.log('응답',response);
                                chatData.img = response.data;
                                this.chatroom.push(chatData);
                                //채팅방 출력이미지
                                 if(doc.data().name == 'Room') // 1대일대화방일경우
                                {
                                    chatData.viewImg = chatData.img[toforname];
                                }
                                //아닐경우 -> firestore에 저장된 이름
                                else{
                                    var a = Math.floor(Math.random()*chatData.id.length);
                                    chatData.viewImg = chatData.img[a];
                                }
                                //시간순으로 정렬
                                this.chatroom.sort((a, b) => {
                                    return -1 * (a.time - b.time);
                                    });
                           })
                            .catch((error) => {
                                // console.log(error.response);
                            });

                        //  // console.log(doc.id, '=>',doc.data());
                           })
                            .catch((error) => {
                                // console.log(error.response);
                            });
                          
                                // // console.log(this.responseNickName);

                         
                       })
                  })
                  .catch(err => {
                     // console.log(err);
                  });
    },
    methods:{
        chatGo(room){
            // // console.log(room);
           
                            
         this.$router.push({name: 'Chat', params: {room: room}});
        },
        addRoom(){
             if (this.name) {
                window.db.collection('test').where('id', 'array-contains', this.name).orderby('time').get()
                .then(snapshot=>{
                    //없을경우
                    if(snapshot.empty){
                        // console.log("없다");
                        return;
                    }

                    //있을경우
                    snapshot.forEach(doc=>{
                        // // console.log(doc.id, '=>',doc.data());
                    })
                })
                .catch(err => {
                    // console.log(err);
                });
                this.newMessage = null;
                this.errorText = null;
            } else {
                this.errorText = "A message must be entered first!";
            }
        }
    }
}
</script>

<style>

</style>