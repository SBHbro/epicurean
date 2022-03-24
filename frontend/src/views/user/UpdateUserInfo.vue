<template>
  <div>
    <v-toolbar class="mb-1" dense elevation="1">
      <v-icon @click="$router.go(-1)">
        mdi-arrow-left
      </v-icon>
      <v-spacer></v-spacer>
      <p class="my-auto text-center">프로필 수정</p>
      <v-spacer></v-spacer>
      <a @click="update">
          <i class="fas fa-check"></i>
      </a>
    </v-toolbar>
    
    <div class="entire">
    <!-- 선호 음식 -->
    <div class="label-with-input">
      <label class="userInfo">선호음식</label>
      <ul class="select-many">
        <li>
          <input type="checkbox" v-model="checkedFoods" id="han" value="1" name="food" />
          <label for="han" class="food">
            <img src="../../assets/images/food_han.png" alt="Food Image" />
            한식
          </label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedFoods" id="yang" value="2" name="food" />
          <label for="yang" class="food">
            <img src="../../assets/images/food_yang.png" alt="Food Image" />
            양식
          </label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedFoods" id="jung" value="3" name="food" />
          <label for="jung" class="food">
            <img src="../../assets/images/food_jung.png" alt="Food Image" />
            중식
          </label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedFoods" id="il" value="4" name="food" />
          <label for="il" class="food">
            <img src="../../assets/images/food_il.png" alt="Food Image" />
            일식
          </label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedFoods" id="bun" value="5" name="food" />
          <label for="bun" class="food">
            <img src="../../assets/images/food_bun.png" alt="Food Image" />
            분식
          </label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedFoods" id="bu" value="6" name="food" />
          <label for="bu" class="food">
            <img src="../../assets/images/food_bu.png" alt="Food Image" />
            뷔페
          </label>
        </li>
        <div class="error-text" v-if="error.foods">{{error.foods}}</div>
      </ul>
    </div>

    <!-- personality -->
    <div class="label-with-input">
      <label class="userInfo">성격</label>
      <ul class="select-many">
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="1"
            id="p1"
            name="personality"
          />
          <label for="p1" class="pers">낙천적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="2"
            id="p2"
            name="personality"
          />
          <label for="p2" class="pers">부정적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="3"
            id="p3"
            name="personality"
          />
          <label for="p3" class="pers">내향적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="4"
            id="p4"
            name="personality"
          />
          <label for="p4" class="pers">외향적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="5"
            id="p5"
            name="personality"
          />
          <label for="p5" class="pers">충동적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="6"
            id="p6"
            name="personality"
          />
          <label for="p6" class="pers">사교적</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="7"
            id="p7"
            name="personality"
          />
          <label for="p7" class="pers">대담함</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="8"
            id="p8"
            name="personality"
          />
          <label for="p8" class="pers">성실함</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="9"
            id="p9"
            name="personality"
          />
          <label for="p9" class="pers">냉정함</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="10"
            id="p10"
            name="personality"
          />
          <label for="p10" class="pers">온화함</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="11"
            id="p11"
            name="personality"
          />
          <label for="p11" class="pers">신중함</label>
        </li>
        <li>
          <input
            type="checkbox"
            v-model="checkedPersonalities"
            value="12"
            id="p12"
            name="personality"
          />
          <label for="p12" class="pers">게으름</label>
        </li>
        <div class="error-text" v-if="error.personalities">{{error.personalities}}</div>
      </ul>
    </div>

    <!-- interest -->
    <div class="label-with-input" style="margin-bottom:100px;">
      <label class="userInfo">관심사</label>
      <ul class="select-many-interest">
        <li>
          <input type="checkbox" v-model="checkedInterests" value="1" id="i1" name="interest" />
          <label for="i1" class="int">아이돌</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="2" id="i2" name="interest" />
          <label for="i2" class="int">콘서트</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="3" id="i3" name="interest" />
          <label for="i3" class="int">취업</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="4" id="i4" name="interest" />
          <label for="i4" class="int">게임</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="5" id="i5" name="interest" />
          <label for="i5" class="int">패션</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="6" id="i6" name="interest" />
          <label for="i6" class="int">다이어트</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="7" id="i7" name="interest" />
          <label for="i7" class="int">재테크</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="8" id="i8" name="interest" />
          <label for="i8" class="int">사업</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="9" id="i9" name="interest" />
          <label for="i9" class="int">경제</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="10" id="i10" name="interest" />
          <label for="i10" class="int">건강</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="11" id="i11" name="interest" />
          <label for="i11" class="int">스포츠</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="12" id="i12" name="interest" />
          <label for="i12" class="int">요리</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="13" id="i13" name="interest" />
          <label for="i13" class="int">군대</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="14" id="i14" name="interest" />
          <label for="i14" class="int">인테리어</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="15" id="i15" name="interest" />
          <label for="i15" class="int">여행</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="16" id="i16" name="interest" />
          <label for="i16" class="int">일러스트</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="17" id="i17" name="interest" />
          <label for="i17" class="int">동물</label>
        </li>
        <li>
          <input type="checkbox" v-model="checkedInterests" value="18" id="i18" name="interest" />
          <label for="i18" class="int">IT</label>
        </li>
        <div class="error-text" v-if="error.interests">{{error.interests}}</div>
      </ul>
    </div>
  </div>
  </div>
</template>

<script>
import axios from "axios";

const SERVER_URL = "https://don.sbbro.shop/api";
// const SERVER_URL = "http://localhost:8080";

export default {
  name: "JoinInfo",
  data() {
    return {
      checkedGender: "",
      checkedAge: "",
      checkedFoods: [],
      checkedPersonalities: [],
      checkedInterests: [],
      userId:"",
      error: {
        gender: false,
        age: false,
        foods: false,
        personalities: false,
        interests: false,
      },
    };
  },
  watch: {
    checkedGender: function () {
      if (this.checkedGender == "") this.error.gender = "성별을 선택해주세요.";
      else this.error.gender = false;
    },
    checkedAge: function () {
      if (this.checkedAge == "") this.error.age = "연령을 선택해주세요.";
      else this.error.age = false;
    },
    checkedFoods: function () {
      if (this.checkedFoods.length < 1)
        this.error.foods = "선호음식을 1개 이상 선택해주세요.";
      else this.error.foods = false;
    },
    checkedPersonalities: function () {
      if (this.checkedPersonalities.length < 1)
        this.error.personalities = "성격을 1개 이상 선택해주세요.";
      else this.error.personalities = false;
    },
    checkedInterests: function () {
      if (this.checkedInterests.length < 1)
        this.error.interests = "관심사를 1개 이상 선택해주세요.";
      else this.error.interests = false;
    },
  },
  methods:{
    update(){
      let userData = {
        userId : this.userId,
        userFavorite : this.checkedFoods,
        userPersonality : this.checkedPersonalities,
        userInterest  : this.checkedInterests
      };
      axios
        .put(
          `${SERVER_URL}/userpage/putuserInfo`, userData
        )
        .then((response)=>{
          if(response.data){
            alert("수정완료");
            this.$router.go(-1);
          }
        })
        .catch((error) => {
          // console.log(error.response);
        });
    }
  },
   created() {
    this.userId = this.$cookie.get("userId")
    axios
      .get(
        `${SERVER_URL}/userpage/getuserInfo?userId=${this.userId}`
      )
      .then((response) => {
        // alert(response.data.userFavorite);
        var tmp = response.data.userFavorite.replace('[','').replace(']',''); 
        var s = "";
        for (let i = 0; i < tmp.length; i++) {
          if(tmp[i] == ','){
            this.checkedFoods.push(s);
            s = "";
          }
          else if(tmp[i] == ' '){
            continue;
          }
          else{
            s += tmp[i];
          }
        }
        this.checkedFoods.push(s);
        tmp = response.data.userPersonality.replace('[','').replace(']',''); 
        s = "";
        for (let i = 0; i < tmp.length; i++) {
          if(tmp[i] == ','){
            this.checkedPersonalities.push(s);
            s = "";
          }
          else if(tmp[i] == ' '){
            continue;
          }
          else{
            s += tmp[i];
          }
        }
        this.checkedPersonalities.push(s);

        tmp = response.data.userInterest.replace('[','').replace(']',''); 
        s = "";
        for (let i = 0; i < tmp.length; i++) {
          if(tmp[i] == ','){
            this.checkedInterests.push(s);
            s = "";
          }
          else if(tmp[i] == ' '){
            continue;
          }
          else{
            s += tmp[i];
          }
        }
        this.checkedInterests.push(s);
      })
      .catch((error) => {
        // console.log(error.response);
      });
    },
    isVaildJoinInfo() {
      if (this.checkedGender == "") {
        this.error.gender = "성별을 선택해주세요.";
        return false;
      }
      if (this.checkedAge == "") {
        this.error.age = "연령을 선택해주세요.";
        return false;
      }
      if (this.checkedFoods.length < 1){
        this.error.foods = "선호음식을 1개 이상 선택해주세요.";
        return false;
      }
      if (this.checkedPersonalities.length < 1){
        this.error.personalities = "성격을 1개 이상 선택해주세요.";
        return false;
      }
      if (this.checkedInterests.length < 1){
        this.error.interests = "관심사를 1개 이상 선택해주세요.";
        return false;
      }
      return true;
    },
};
</script>

<style scoped>
.entire {
  padding: 18px;
}
.title {
  text-align: center;
}

.userInfo {
  margin: 18px 0 10px 25px;
}

.selectInfo {
  padding: 8px 25px;
  border: solid gray;
  border-radius: 15px;
  border-width: 1.5px;
  margin: 5px 5px 30px 0;
}

img {
  max-width: 37%;
  margin-right: 5px;
  height: auto;
  border-radius: 100px;
}

.next-btn {
  margin: 20px 20px;
  height: 200px;
  width: 80%;
  text-align: center;
}

.submitbtn {
  color: white;
}

.gender,
.age,
.pers,
.int {
  text-align: center;
  justify-content: center;
}

.select-gender,
.select-many {
  list-style-type: none;
  margin: 2px 0 0 0;
  padding: 0x;
}

.select-many-interest {
  list-style-type: none;
  margin: 2px 0 0 0;
  padding: 0x;
}

.select-gender li,
.select-many li {
  float: left;
  margin: 0 5px 0 0;
  width: 85px;
  height: 42px;
  position: relative;
}

.select-many-interest li {
  float: left;
  margin: 0 5px 0 0;
  width: 85px;
  height: 42px;
  position: relative;
}

.select-gender label,
.select-gender input {
  display: block;
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
}

.select-gender input[type="radio"],
.select-many input[type="checkbox"] {
  opacity: 0.011;
  z-index: 100;
}

.select-gender input[type="radio"]:checked + label {
  background: #ffb74d;
}

.select-many input[type="checkbox"]:checked + label {
  background: #ffb74d;
}

.select-many-interest input[type="checkbox"]:checked + label {
  background: #ffb74d;
}

.select-gender label {
  padding: 5px;
  border: 1.5px solid gray;
  border-radius: 15px;
  cursor: pointer;
  z-index: 90;
}

.select-many label {
  width: 100%;
  padding: 5px 8px 5px 8px;
  border: 1.5px solid gray;
  border-radius: 15px;
  cursor: pointer;
  z-index: 90;
}

.select-many-interest label {
  width: 100%;
  padding: 5px 8px 5px 8px;
  border: 1.5px solid gray;
  border-radius: 15px;
  cursor: pointer;
  z-index: 90;
}

.select-gender label:hover {
  background: #ddd;
}
</style>
