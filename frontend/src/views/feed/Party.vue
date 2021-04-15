<template>
  <div class="user join">
    <v-bottom-navigation
      v-if="$route.name === 'Party'"
      scroll-target="#scroll-area-2"
      hide-on-scroll
      scroll-threshold="500"
      absolute
      color="white"
      horizontal
    >
      <v-btn-toggle v-model="text" tile color="deep-purple accent-3" group>
        <!-- <v-col cols="6"> -->
          <v-btn @click="$router.go(-1)" value="center">취소</v-btn>
        <!-- </v-col> -->

        <!-- <v-col cols="6"> -->
          <v-btn value="right" @click="meetUp">생성</v-btn>
        <!-- </v-col> -->
      </v-btn-toggle>
    </v-bottom-navigation>
    <v-app>
      <v-toolbar-title>
        <v-toolbar dark>
          <a @click="$router.go(-1)">
            <i class="fas fa-chevron-left back"></i>
          </a>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
          <p class="my-auto">Meet Up</p>
          <v-spacer></v-spacer>
          <v-spacer></v-spacer>
        </v-toolbar>
      </v-toolbar-title>
      <br />

      <div class="party wrapC">
        <div class="input-group mb-3">
          <v-text-field v-model="title" solo label="제목"></v-text-field>
        </div>

        <div class="input-group mb-3">
          <v-textarea v-model="content" solo name="input-7-4" label="일정 내용을 입력하세요."></v-textarea>
        </div>

        <v-form>
          <!-- <v-row> -->
          <v-text-field
            solo
            label="장소"
            :append-icon="isColor ? 'mdi-map-marker orange--text text--lighten-2' : 'mdi-map-marker'"
            @click:append="isColor = !isColor"
            @click:input="isClick = !isClick"
          ></v-text-field>
          <!-- </v-row> -->
        </v-form>

        <!-- <v-row> -->
        <v-menu
          ref="menu"
          v-model="menu"
          :close-on-content-click="false"
          :return-value.sync="date"
          transition="scale-transition"
          offset-y
          min-width="300px"
        >
          <template v-slot:activator="{ on, attrs }">
            <v-text-field
              solo
              v-model="date"
              label="날짜"
              :append-icon="isColor1 ? 'mdi-calendar-check orange--text text--lighten-2' : 'mdi-calendar-check'"
              @click:append="isColor1 = !isColor1"
              readonly
              v-bind="attrs"
              v-on="on"
            ></v-text-field>
          </template>
          <v-date-picker
            :min="new Date().toISOString().substr(0, 10)"
            max="2050-01-01"
            v-model="date"
            no-title
            scrollable
          >
            <v-spacer></v-spacer>
            <v-btn text color="primary" @click="menu = false">Cancel</v-btn>
            <v-btn text color="primary" @click="$refs.menu.save(date)">OK</v-btn>
          </v-date-picker>
        </v-menu>
        <!-- </v-row> -->

        <!-- dropdown -->
        <div id="dropdown-example">
          <v-overflow-btn
            v-model="count"
            solo
            class="my-2"
            :items="dropdown_font"
            label="인원"
            style="margin:0px;"
            target="#dropdown-example"
          ></v-overflow-btn>
        </div>
      </div>
    </v-app>
  </div>
</template>

<script>
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "http://localhost:8080";

export default {
  data: () => ({
    // date: new Date().toISOString().substr(0, 10),
    date: null,
    menu: false,
    isColor: false,
    isColor1: false,
    isClick: false,

    dropdown_font: ["1", "2", "3", "4"],
  }),

  mounted() {
    if (window.kakao && window.kakao.maps) {
      this.initMap();
    } else {
      const script = document.createElement("script");
      /* global kakao */
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=722f0c506c2743378fac318284106578";
      document.head.appendChild(script);
    }
  },
  methods: {
    meetUp() {
      if (this.title.length === 0) {
        alert("제목을 작성해주세요.");
        return;
      }

      if (this.content.length === 0) {
        alert("내용을 작성해주세요.");
        return;
      }

      if (this.date.length === 0) {
        alert("날짜를 선택해주세요.");
        return;
      }

      if (this.count.length === 0) {
        alert("인원을 선택해주세요.");
        return;
      }
      // console.log(this.title);
      // console.log(this.title.length);
      // console.log(this.content);
      // console.log(this.content.length);
      // console.log(this.date);
      // console.log(this.date.length);
      // console.log(this.count);
      // console.log(this.count.length);

      // axios
      //   .get(
      //     `${SERVER_URL}/party?title=${this.title}&content=${this.content}`
      //   )
    },
    initMap() {
      var container = document.getElementById("map");
      var options = {
        center: new kakao.maps.LatLng(33.450701, 126.570667),
        level: 3,
      };

      var map = new kakao.maps.Map(container, options);
      // map.setMapTypeId(kakao.maps.MapTypeId.HYBRID);
    },
  },
};
</script>

<style>
.back {
  color: white !important;
}
.nothome {
  padding: 0px !important;
}
.party {
  padding: 0px 12px;
}
#map {
  width: 100px;
  height: 100px;
  z-index: 10;
}
.section .article {
  position: relative;
  color: black;
}
div {
  display: block;
}
.tit {
  position: absolute;
  top: 8px;
  left: 0;
  font-weight: bold;
}
/* .btn-person{
  margin: 0px;
} */
</style>
