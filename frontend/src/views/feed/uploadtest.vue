<template>
    <div class="container chat">
        <h2 class="text-primary text-center">Real-Time Chat</h2>
        <h5 class="text-secondary text-center">Powered by Vue.js & Firebase</h5>
        <div class="card">
            <div class="card-body">
                <p class="text-secondary nomessages" v-if="messages.length == 0">
                    [No messages yet!]
                </p>
                <div class="messages" v-chat-scroll="{always: false, smooth: true}">
                    <div v-for="message in messages" :key="message.id">
                        <div v-if="message.id == userid" style="text-align : right">
                             <span class="text-info">[{{ message.name }}]: </span>
                            <span>{{message.message}}</span>
                            <span class="text-secondary time">{{message.timestamp}}</span>
                        </div>
                        <div v-else>
                            <span class="text-info">[{{ message.name }}]: </span>
                            <span>{{message.message}}</span>
                            <span class="text-secondary time">{{message.timestamp}}</span>
                        </div>
                    </div>
                </div>
            </div>
            <div class="card-action">
                <CreateMessage :name="name" :id="userid"/>
            </div>
        </div>
    </div>
</template>

<script>
import CreateMessage from '../../components/common/CreateMessage';
// import fb from '@/firebase/init';
import moment from 'moment';
export default {
    name: 'UploadTest',
    props: ['name'],
    components: {
        CreateMessage
    },
    data() {
        return {
            userid : 2,
            messages: []
        }
    },
    created() {
        let ref = window.db.collection('chat').doc('유저이름').collection('채팅방이름').orderBy('id');
        ref.onSnapshot(snapshot => {
            snapshot.docChanges().forEach(change => {
                if (change.type == 'added') {
                    let doc = change.doc;
                    this.messages.push({
                        id: doc.data().id,
                        name: doc.data().nickname,
                        message: doc.data().message,
                        timestamp: moment(doc.data().time)
                    });
                }
            });
        });
    }
}
</script>

<style>
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
    max-height: 300px;
    overflow: auto;
}
</style>