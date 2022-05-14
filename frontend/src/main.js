import Vue from 'vue'
import App from './App.vue'
// import VueRouter from 'vue-router';
import router from './routes'
import store from './vuex/store'
import vuetify from '@/plugins/vuetify'

import VueCookie from 'vue-cookie'

import VueChatScroll from 'vue-chat-scroll';
//import firestoreConfig from './firestoreConfig'
var firestoreConfig = { apiKey: "AIzaSyBay9izl5mkxg7aqiIOeYh92-wiLhlwbXE",
authDomain: "donproject-80dad.firebaseapp.com",
projectId: "donproject-80dad",
storageBucket: "donproject-80dad.appspot.com",
messagingSenderId: "906263330950",
appId: "1:906263330950:web:5f7d3f927ad9c4093dd107",
measurementId: "G-7FX64GFJE5"};
Vue.use(VueChatScroll);

import VueSimpleAlert from "vue-simple-alert";
Vue.config.productionTip = false
window.Kakao.init("6192aa4fccde619ea3ac09491e57abd0");

import firebase from 'firebase'

console.log(firestoreConfig)
firebase.initializeApp(firestoreConfig);
firebase.firestore().settings({timestampsInSnapshots:true});

var db = firebase.firestore();

window.db = db;

// Vue.use(VueRouter)

Vue.use(VueCookie)
Vue.use(VueSimpleAlert)
// const router = new VueRouter({
//     routes,
// });

new Vue({
    VueSimpleAlert,
    vuetify,
    router,
    store,
    render: h => h(App),
}).$mount('#app');