import Vue from 'vue'
import App from './App.vue'
// import VueRouter from 'vue-router';
import router from './routes'
import store from './vuex/store'
import vuetify from '@/plugins/vuetify'

import VueCookie from 'vue-cookie'

import VueChatScroll from 'vue-chat-scroll';
import firestoreConfig from './firestoreConfig'
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