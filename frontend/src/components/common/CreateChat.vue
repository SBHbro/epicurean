<template>
    <!-- <div> -->
    <!-- <span style="display: inline-block;"> -->
                    <v-btn  icon @click="onClick">
                    <!-- <div> -->
                        <i 
                        style=" height: 19px; width: 19px;"
                        class="far fa-paper-plane"
                        ></i>
                    <!-- </div> -->
                    </v-btn>
                <!-- </span> -->
    <!-- </div> -->
</template>

<script>
import axios from "axios";
import router from "@/routes";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
    props: ['postuserid'],
    data(){
        return{
            userid : this.$cookie.get('userId'),
            data :[],
            nick : []
            // room : {}
        }
    },
    methods:{
        onClick(){
            // // console.log("hello");
            // // console.log(this.postuserid);
            // // console.log(this.userid);

            //클릭한 아이디 둘로 배열을 만든다 (정렬해서)
            if(this.postuserid==this.userid){
                // console.log("같다")
                return;
            }
            else if(parseInt(this.postuserid)>parseInt(this.userid)){
                // // console.log("pustuserid가 더 크다")
                this.data = [this.userid,this.postuserid];
            }
            else{
                // // console.log("userid가 더 크다.");
                this.data = [this.postuserid,this.userid];
            }
                // // console.log(this.data);

            //동일한 채팅방이 있는지 검사한다.
            window.db.collection('chat').where('id', '==', this.data).where('name','==','Room').get()
                 .then(snapshot=>{
                  //없을경우
                    var room = {};
                    // var ro = {};
                      if(snapshot.empty){
                            //만들기

                            //새로운 채팅방 생성 
                            const newRoomRef = window.db.collection('chat').doc();
                            // console.log(newRoomRef.id);
                            window.db.collection('chat').doc(newRoomRef.id).collection('test').doc();

                             var res = window.db.collection('chat').doc(newRoomRef.id).set({
                                id :this.data,
                                time : Date.now(),
                                name : "Room"
                            }).catch(err =>{
                                // console.log(err);
                            })

                            alert("새로운 채팅방 생성완료 다시 요청해주세요");
                            return;
                        }

                    //있을경우
                    else{
                        snapshot.forEach(doc=>{

                             
                            //데이터 집어넣기
                            room.id = this.data;
                            room.time = Date.now();
                            // room.name = doc.data().name;
                            room.rid = doc.id;
                            room.to = this.postuserid;
                         })
                    }

                    //닉네임 집어넣기
                    axios
                            .post(
                                `${SERVER_URL}/chat/nickname`,this.data
                            )
                            .then((response) => {
                                this.nickName
                                this.nick.push(response.data);
                                room.nickName = response.data;
                                room.name = response.data;
                           })
                            .catch((error) => {
                                // console.log(error.response);
                            });


                         this.goChat(room);
                  })
                  .catch(err => {
                     // console.log(err);
                  });
        },
        goChat(room){
            //데이터 넣고 채팅방으로 이동
            // // console.log("함수이동 ");
            // // console.log(room)

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
        }
    }
}
</script>

<style>

</style>