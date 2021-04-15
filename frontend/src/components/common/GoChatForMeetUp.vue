<template>
    <div>
    <span style="display: inline-block;">
                    <button @click="onClick" style="background: 0 0; border: 0; display: flex; padding: 8px;">
                    <div>
                        <i
                        style="display: block; position: relative; height: 24px; width: 24px;"
                        class="far fa-paper-plane"
                        ></i>
                    </div>
                    </button>
                </span>
    </div>
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
    props: ['meetup'],
    data(){
        return{
            userid : this.$cookie.get('userId'),
            data :[],
            nick : [],
            room : []
            // room : {}
        }
    },
    methods:{
        onClick(){
            // meetup의 이름으로 채팅방을 가져온다.
            window.db.collection('test').where('name', '==', this.meetup.title).get()
                 .then(snapshot=>{
                     if(snapshot.empty){
                         alert("생성된 채팅방이 없습니다.");
                        return;
                     }
                        snapshot.forEach(doc=>{
                            //데이터 가져오기
                            this.room.id = this.meetup.id;
                                //내가 포함된 채팅방이 아니면 return;
                                if(!doc.data().id.includes(this.userid))
                                {
                                    alert("내가 포함된 밋업이 아닙니다.")
                                    return;
                                }
                            // // this.room.time = Date.now();
                            this.room.name = doc.data().name;
                            this.room.rid = doc.id;

                            //밋업 참가 유저 정보 가져오기
                            this.getMeetUpUser(this.room);
                         })

                  })
                  .catch(err => {
                     // console.log(err);
                     // console.log("에러");
                  });
        },
        goChat(room){
            // 데이터 넣고 채팅방으로 이동
            axios
                            .post(
                                `${SERVER_URL}/userpage/getuserpost`,room.id
                            )
                            .then((response) => {
                                // // console.log('응답',response);
                                room.img = response.data;
                                this.$router.push({name: 'Chat', params: {room: room}});
                           })
                            .catch((error) => {
                                // console.log(error.response);
                            });
            this.$router.push({name: 'Chat', params: {room: room}});
        },

        getMeetUpUser(room){
            this.room = room;
             axios.post(`${SERVER_URL}/chat/meetup`,this.meetup.id)
                 .then(response=>{         
                     this.room.id = response.data;
                      //닉네임 집어넣기      
                      this.insertNickname(this.room)                                       
                  })
               .catch(error=>{
                // console.log("못받아왔어용");
              })
        },

        insertNickname(room){
            this.room = room;
             axios.post( `${SERVER_URL}/chat/nickname`,this.room.id)
                .then((response) => {
                     this.room.nickName = response.data;
                     
                     //데이터 넣고 채팅방으로 이동
                     this.goChat(this.room)
                     })
                .catch((error) => {
                     // console.log(error.response);
             });
        }
    }
}
</script>

<style>

</style>