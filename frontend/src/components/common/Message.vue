<template>
  <v-app>
<v-container>
  <v-layout class="entireClass">
      <v-row>
      <v-col v-for="item in items" :key="item.title" class="d-flex child-flex" cols="4">
          <v-card flat tile class="d-flex">
              <v-img @click.stop="show(item)" :src="item.img[0]" aspect-ratio="1" class="grey lighten-2">
              <template v-slot:placeholder>
              <v-row
                  class="fill-height ma-0"
                  align="center"
                  justify="center"
              >
                  <v-progress-circular indeterminate color="grey lighten-5"></v-progress-circular>
              </v-row>
              </template>
              </v-img>
          </v-card>
      </v-col>    
      
      <!-- dialog -->
      <v-dialog
        v-model="dialog"
        max-width="190"
        >
         <v-list> 
          <v-list-item
          v-for="(iitem, index) in iitems"
          :key="index"
          @click="doit(iitem)"
          >
          <v-list-item-title>{{ iitem.title }}</v-list-item-title>
          </v-list-item>
      </v-list>
      </v-dialog>
      </v-row>
    
  </v-layout>
    <!-- 스크랩 없을 때 -->
    <div v-if="items.length == 0" class="text-center" style="margin-top:40%;"> 
      <v-icon size="80" color="grey darken-2">mdi-food</v-icon>
      <h3 class="mt-5 h6 font-weight-bold">스크랩한 음식점이 없습니다.</h3>
    </div>
</v-container>
  </v-app>
</template>


<script>

import axios from "axios";
import router from "@/routes";


const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "https://localhost:8080";

export default {
    props:['userId'],

    data () {
      return {
        restaurant: [],
        restid:[],
        items:[],
        iitems:[
        { title: '상세보기' },
        { title: '삭제' },
      ],
       dialog: false,
       data : [],
       list : [],
      }
    },
    created(){
      this.whenCreated();
    },
    methods: {
      whenCreated() {
        axios
        .get(`${SERVER_URL}/rest/scrap/${this.userId}`)
        .then((response) => {
          this.restid = response.data; // [11,12,13]
          for (var i=0; i<this.restid.length; i++) {
            axios
              .get(`${SERVER_URL}/rest/${this.restid[i]}`)
              .then((response) => {
                var restaurant = response.data;
                this.restaurant = restaurant
                // String 형태의 img src 5개를 파싱해서 배열로 만듬
                let imgSrcs = restaurant.img;
                imgSrcs = imgSrcs.replace('[', '');
                imgSrcs = imgSrcs.replace(']', '');
                imgSrcs = imgSrcs.replace(/(\s*)/g, ''); // 모든공백제거
                imgSrcs = imgSrcs.split(",");
                restaurant.img = imgSrcs;               
                this.items.push(restaurant); 
              })
              .catch((error) => {
              // console.log(error.response);
              });
            }
        })
        .catch((error) => {
          // console.log(error.response);
        });
      },
      show(item){
        this.dialog = true;
        this.list = item;
      },
      doit(iitem){
        if(iitem.title == '삭제') {
          // console.log(this.list);
          axios.delete(`${SERVER_URL}/rest/scrap?restid=${this.list.id}&userid=${this.userId}`)
          .then((response) => {
              this.dialog = false
              this.items = []
              this.whenCreated();
          })
        } else {
          router.push({name: "Detail", query: this.list});
        }
      }
    }
  }
</script>

<style>
  .entireClass{
    padding: 20px;
  }

  .aligncss {
    margin: 100px 0 0 0;
    color: rgba(0,0,0,.6);
    width: 100%;
    text-align: center;
    min-height: 300px
  }

</style>