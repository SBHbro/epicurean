<template>
    <div style="margin-bottom: 30px;">
        <form @submit.prevent="createMessage">
            <div class="form-group">
                <input style = "width : 80%; display : inline" type="text" name="message" class="form-control" placeholder="Enter message ..." v-model="newMessage">
               <v-btn style = "width : 20%" class="btn btn-primary" type="submit" name="action">전송</v-btn>
               <!-- <button style = "float : right" class="btn btn-primary" type="submit" name="action">Submit</button> -->
                <p class="text-danger" v-if="errorText">{{ errorText }}</p>
            </div>
            
        </form>
    </div>
</template>

<script>
// import fb from '@/firebase/init';


export default {
    name: 'CreateMessage',
    props: ['id','rid','to','myNickName','roomName'],
    data() {
        return {
            newMessage: null,
            errorText: null
        }
    },
    created(){
    },
    methods: {
        createMessage () {
            if (this.newMessage) {
                window.db.collection('chat').doc(this.rid).collection('messages').add({
                    from : this.id,
                    message: this.newMessage,
                    time: Date.now()
                }).catch(err => {
                    // console.log(err);
                });
                window.db.collection('chat').doc(this.rid).set({
                    update: Date.now(),
                    lastMessage : this.newMessage
                },{merge : true})
                this.newMessage = null;
                this.errorText = null;

                var a;
                    var message = '';
                //this.to가 배열일경우
                // // console.log(Array.isArray(this.to));
                if(Array.isArray(this.to)){
                    a = this.to;
                }
                //배열이 아닐 경우 배열로 만들어야함.
                else{
                    a = [];
                    a.push(this.to);
                }
                if(a.length == 1){
                    message = this.myNickName + "님이 1대1 메시지를 보냈습니다.";
                }
                else{
                    message = this.myNickName + "님이 " + this.roomName + " 에서 메시지를 보냈습니다.";
                }

                for(var i = 0;i<a.length;i++){
                    // // console.log(this.myNickName);
                    //새로운 알람을 보낸다.
                    window.db.collection('alarm').doc('chat').collection('messages').add({
                        to : a[i],
                        from : this.id,
                        message: message,
                        time: Date.now(),
                        roomid : this.rid,
                        confirm : false
                    }).catch(err => {
                        // console.log(err);
                    });
                }
            } else {
                this.errorText = "A message must be entered first!";
            }
        }
    }    
}
</script>
