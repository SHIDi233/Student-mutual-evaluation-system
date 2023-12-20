<!-- <style src="./css/login.css" scoped></style> -->
<style scoped>
		*{
            padding: 0;
            margin: 0;
            text-decoration: none;
        }
        body{
			width:100%;
            height:100%;
            display: flex;
            justify-content: center;	
            align-items: center;
            height: 100vh;
            /* background-image: url('../img/back.jpg'); */
            background-size: cover;
        }
        .login{
            width: 550px;
            height: 400px;
            display: flex;
            justify-content: center;
            align-items: center;
            background: linear-gradient(
                to right bottom,
                rgba(255,255,255,.7),
                rgba(255,255,255,.5),
                rgba(255,255,255,.4)
            );
            /* 使背景模糊化 */
            backdrop-filter: blur(10px);
            box-shadow: 0 0 20px #a29bfe;
            border-radius: 15px;
        }

        .table{
            font: 900 40px '';
            text-align: center;
            letter-spacing: 5px;
            color: #3d3d3d;
        }

        .box{
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }

        .box input{
            width: 400px;
            height: 100%;
            margin-bottom: 20px;
            outline: none;
            border: 0;
            padding: 10px;
            background-color: transparent;
            border-bottom: 3px solid rgb(150, 150, 240);
            font: 900 16px '';
        }

        .go{
            text-align: center;
            display: block;
            left:0px;
            height: 44px;
            padding: 10px;
            font: 900 20px '';
            border-radius: 10px;
            margin-top: 15px;
            color: #fff;
            letter-spacing: 3px;
            background-image: linear-gradient(to left,
            #fd79a8,#a29bf6);
        }
</style>
<template>
    <body>
      <div class="login">
          <div class="box">
              <p class="table" style="font: 900 40px '';
            text-align: center;
            letter-spacing: 5px;
            color: #3d3d3d;">CCN作业互评系统</p>
              <br>
              <input id="account" type="text" placeholder="账号" value="" v-model="ac">
              <input id="pwd" type="password" placeholder="密码" value="" v-model="pw" >
              
              <!-- <a href="#" class="go" v-on:click="getValue" >登录</a>
              <a href="#" class="go" v-on:click="register">注册</a> -->
              
              <br>
              <div style="width: 100%; ">
                <el-button class="go" v-loading="loading" @click="getValue" style=" width: 100%;text-align: center;
            display: block;
            left:0px;
            height: 44px;
            padding: 10px;
            font: 900 20px '';
            border-radius: 10px;
            margin-top: 15px;
            color: #fff;
            letter-spacing: 3px;
            background-image: linear-gradient(to left,
            #fd79a8,#a29bf6);">登录</el-button>
              </div>
              <div>
                <el-button class="go" @click="register"
                style=" width: 100%;text-align: center;
            display: block;
            left:0px;
            height: 44px;
            padding: 10px;
            font: 900 20px '';
            border-radius: 10px;
            margin-top: 15px;
            color: #fff;
            letter-spacing: 3px;
            background-image: linear-gradient(to left,
            #fd79a8,#a29bf6);">注册</el-button>
              </div>
              
          </div>
          
      </div>
    </body>
</template>



<script>
    import axios from 'axios'
    import global from './GlobalPage.vue'
    const restweburl = global.ip;
    export default {
        data() {
            //message: 'Hello Vue.js!'
            return{
                ac:"",
                pw:"",

                loading:false,
            }
        },
        methods: {
            getValue: function () {
                // 获取message变量值将其转换成数组，逆转后再链接，再赋值给message
                // alert("登录");
                // var a = this.ac;
                // var p = this.pw;
                // alert(a);
                // alert(p);

                this.loading=true;

                var params = new URLSearchParams();
                params.append('mail',this.ac);
                params.append('password',this.pw);

                axios.post(restweburl+'login', params).then(response => {
                    console.log(response.data)
                    // var obj = JSON.parse(response.data);
                    // console.log(obj)
                    if(response.data.code==1){
                        window.localStorage.setItem("CCNtoken",response.data.data);
                        // alert(window.localStorage.getItem("CCNtoken"));
                        this.$router.push('/home');
                    }
                    else{
                        // alert("密码错误")
                        this.$message.error('账户/密码错误');
                        this.loading=false;
                    }
                }).catch(error => {
                    // alert(error)
                    // alert("请求失败")
                    this.$message.error(+error+' (网络错误)');
                    this.loading=false;
                })
            },
            register: function () {
                // 获取message变量值将其转换成数组，逆转后再链接，再赋值给message
                // alert("注册");
                this.$router.push('/register')
            }
        }
    }
</script>
