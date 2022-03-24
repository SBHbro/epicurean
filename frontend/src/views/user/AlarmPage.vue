<template>
   <div>

      <!-- 상단 툴바 -->
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto">알림센터</p>
      <v-spacer></v-spacer>
    </v-toolbar>

<br>

    <div v-if="chat.length+post.length+accept.length == 0" class="text-center" style="margin-top:50%;"> 
      <v-icon size="100" color="grey darken-2">mdi-file-image-outline</v-icon>
      <h3 class="mt-5 font-weight-bold">새로운 알림이 없습니다.</h3>
    </div>

    <div v-else>
    <v-expansion-panels >
      <v-divider>채팅</v-divider>
      <v-expansion-panel>
        <v-expansion-panel-header>
                {{chatCount}} 채팅 
          </v-expansion-panel-header>
        <div v-for="(list,i) in chat" :key="i">
          <div @click="onClick(list)">
            <v-expansion-panel-content >
              <v-list>
                <v-list-item>
                  <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
                    <img
                      :src="`https://don.sbbro.shop/api/img/user?imgname=`+list.img"
                    />
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>
                      {{list.message}}
                    </v-list-item-title>
                      <v-list-item-subtitle>
                        {{ list.time | moment("from","now")}}
                      </v-list-item-subtitle>
                  </v-list-item-content>
                      

                </v-list-item>
              </v-list>
                  <!-- <v-btn style="float : right;" class="btn btn-primary" @click="onClick(list)">확인</v-btn> -->

            </v-expansion-panel-content>
          </div>
        </div>
      </v-expansion-panel>
      </v-expansion-panels>

<br><br><br><br>
      <v-expansion-panels>
      <v-divider>게시글</v-divider>
      <v-expansion-panel >
        <v-expansion-panel-header>
                {{postCount}} 게시글
        </v-expansion-panel-header>
        <div v-for="(list,i) in post" :key="i">
          <div @click="onClick(list)">
            <v-expansion-panel-content >
                 <v-list>
                <v-list-item>
                  <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
                    <img
                      :src="`https://don.sbbro.shop/api/img/user?imgname=`+list.img"
                    />
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>
                      {{list.message}}
                    </v-list-item-title>
                      <v-list-item-subtitle>
                        {{ list.time | moment("from","now")}}
                      </v-list-item-subtitle>
                  </v-list-item-content>
                      

                </v-list-item>
              </v-list>
            </v-expansion-panel-content>
          </div>
        </div>
      </v-expansion-panel>
      </v-expansion-panels>

<br><br><br><br>
      <v-expansion-panels>
      <v-divider>요청</v-divider>
      <v-expansion-panel >
        <v-expansion-panel-header>
                {{acceptCount}} 요청
          </v-expansion-panel-header>
        <div v-for="(list,i) in accept" :key="i">
          <div @click="onClick(list)">
            <v-expansion-panel-content >
                <v-list>
                <v-list-item>
                  <v-list-item-avatar style="cursor:pointer;" @click="gotoProfile(lst)">
                    <img
                      :src="`https://don.sbbro.shop/api/img/user?imgname=`+list.img"
                    />
                  </v-list-item-avatar>
                  <v-list-item-content>
                    <v-list-item-title>
                      {{list.message}}
                    </v-list-item-title>
                      <v-list-item-subtitle>
                        {{ list.time | moment("from","now")}}
                      </v-list-item-subtitle>
                  </v-list-item-content>
                      

                </v-list-item>
              </v-list>
            </v-expansion-panel-content>
          </div>
        </div>
      </v-expansion-panel>

  </v-expansion-panels>
    </div>

         
      
  <br><br>
  </div>
</template>

<script>
import moment from 'moment';

import axios from "axios";
const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
  data(){
    return {
      chat : [],
      post : [],
      accept : [],
      chatCount : 0,
      postCount : 0,
      acceptCount : 0,
    }
  },
  created(){
    this.view('chat');
    this.view('like');
    this.view('comment');
    this.view('follow');
    this.view('meetup');
  },

  methods:{
    //type별로 알림목록을 가져온다.
    view(type){
      window.db.collection('alarm').doc(type).collection('messages').where('to', '==', this.$cookie.get('userId')).where('confirm','==',false).orderBy('time','asc').onSnapshot(snapshot=>{
        snapshot.docChanges().forEach(change =>{
          if(change.type == 'added'){
            var doc = change.doc;
                   //있을경우
                       var data = doc.data();
                        data.rid = doc.id;
                        data.type = type;
                        var img ='';
                       //axios로 id배열 보내서 프로필이미지 배열로 바꿔온다.
                         axios
                           .post(
                               `${SERVER_URL}/userpage/getuserpost`,[data.from]
                           )
                           .then((response) => {
                               // // console.log('응답',response);
                             // // console.log('data',data)
                             data.img = response.data[0]
                             if(type == 'chat'){
                               this.chatCount++;
                               this.chat.push(data);

                               //정렬
                               this.chat.sort((a, b) => {
                                    return -1 * (a.time - b.time);
                                    });
                             }
                             else if(type == 'like' || type == 'comment'){
                               this.postCount++;
                               this.post.push(data);
                               //정렬
                               this.post.sort((a, b) => {
                                    return -1 * (a.time - b.time);
                                    });
                             }
                             else if(type == 'follow' || type == 'meetup'){
                               this.acceptCount++;
                               this.accept.push(data);
                               //정렬
                               this.accept.sort((a, b) => {
                                    return -1 * (a.time - b.time);
                                    });
                             }
                          })
                           .catch((error) => {
                               // console.log(error.response);
                           });

          }
          else{
            // console.log(change.type);
          }
        })
      })
                //  .then(snapshot=>{
                //   //없을경우
                //       if(snapshot.empty){
                //         // // console.log("없다");
                //         return;
                //         }

                //   })
                //   .catch(err => {
                //      // console.log(err);
                //   });
    },

    //클릭했을 때 페이지 변경해주는 함수
    onClick(list){
      // console.log('click');
      if(list.type=='chat'){
        this.$router.push({ path: '/chatroom' });
      }
      else if(list.type=='like' || list.type =='comment'){
        if(list.type == 'like'){
          window.db.collection('alarm').doc('like').collection('messages').doc(list.rid).set({
            confirm : true
          },{merge : true})
        }
        else{
          window.db.collection('alarm').doc('comment').collection('messages').doc(list.rid).set({
            confirm : true
          },{merge : true})
        }

          axios
            .get(`${SERVER_URL}/userpage/getpost?postId=`+list.postid)
            .then((response) => {
                var post = response.data.data
                var comment = response.data.comment;
                // // console.log(post.post_content);
                // // console.log(comment);
               this.$router.push({name :'PostDetail', query: {post_content: post.post_content
                ,post_id : post.post_id, post_img_url : post.post_img_url,
                post_like : post.post_like, post_userid : post.post_userid, user_img:post.user_img,
                user_nickname : post.user_nickname, comment:comment , userId : this.userId}});
            })  
      }
      else if(list.type =='follow'){

         //팔로우 요청 알림을 모두 읽음으로 변경.
                      window.db.collection('alarm').doc('follow').collection('messages').where('to','==',this.$cookie.get('userId')).get()
                      .then(snapshot =>{
                          if(snapshot.empty) {
                              // // console.log('팔로우 요청이 없다.')
                          }

                          snapshot.forEach(doc =>{
                             window.db.collection('alarm').doc('follow').collection('messages').doc(doc.id).set({
                                    confirm : true
                                },{merge:true}).catch(err => {
                                    // // console.log(err);
                                });
                          })
                      })

        this.$router.push({ path: '/user/FollowRequestList' });
      }
      else if(list.type == 'meetup'){
         //팔로우 요청 알림을 모두 읽음으로 변경.
                      window.db.collection('alarm').doc('meetup').collection('messages').where('to','==',this.$cookie.get('userId')).get()
                      .then(snapshot =>{
                          if(snapshot.empty) {
                              // // console.log('팔로우 요청이 없다.')
                          }
                          snapshot.forEach(doc =>{
                             window.db.collection('alarm').doc('meetup').collection('messages').doc(doc.id).set({
                                    confirm : true
                                },{merge:true}).catch(err => {
                                    // // console.log(err);
                                });
                          })
                      })
            this.$router.push({ path: '/meetup/add' });
      }
    }
  }
}
</script>

<style>

</style>