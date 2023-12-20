<style src="./css/login.css" scoped></style>
<template>
<body>
    <div class="login">
        <div class="box">
            <p class="table">Git CCN</p>
            <br>
            <input id="mail" type="text" placeholder="邮箱" value="" v-model="mail">
			<input id="name" type="text" placeholder="真实姓名" value="" v-model="name">
            <input id="pwd" type="password" placeholder="密码" value="" v-model="password">
			<a href="#" class="go" v-on:click="commit">提交</a>
			<br>
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
                mail:"",
                name:"",
                password:""
            }
        },
        methods: {
            commit: function () {
                var m = this.mail;
                var n = this.name;
                var p = this.password;

                var emreg=/^\w{3,}(\.\w+)*@[A-z0-9]+(\.[A-z]{2,5}){1,2}$/;
	
			if(m==''){
				alert("邮箱地址不能为空");
				return;
			}
			else if(emreg.test(m)==false){
				alert("请输入正确的邮箱地址");
				return;
			} 
			if(n==''){
				alert("昵称不能为空");
				return;
			}
			if(p==''){
				alert("密码不能为空");
				return;
			}
	
			if(p.length<5){
				alert("密码位数至少大于5");
				return;
			}

            var params = new URLSearchParams();
                params.append('mail',this.mail);
                params.append('name',this.name);
                params.append('password',this.password);

                axios.post(restweburl+'register', params).then(response => {
                    console.log(response.data)
                    // var obj = JSON.parse(response.data);
                    if(response.data.code==1){
                        alert("注册成功");
                        window.localStorage.setItem("CCNtoken",response.data.data);
                        this.$router.push('/chg')
                    }
                    else{
                        alert("注册失败：邮箱已被注册");
                    }
                }).catch(error => {
                    console.log(error)
                    alert("请求失败")
                })
            },
        }
    }
</script>