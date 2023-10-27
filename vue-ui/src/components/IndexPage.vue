<template>
    <div>
        <el-container>
        <el-header>
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                <el-menu-item index="1">班级</el-menu-item>
                <el-menu-item index="2">申请进度</el-menu-item>
                <el-menu-item index="3">消息中心</el-menu-item>
                <!-- <el-menu-item index="4">审批进度</el-menu-item> -->
                <el-submenu index="5">
                        <template slot="title">
                            <el-avatar :size="35" :src="header_url"></el-avatar>
                        </template>
                    <el-menu-item index="6">个人中心</el-menu-item>
                    <el-menu-item index="7">修改信息</el-menu-item>
                    <el-menu-item index="8">注销</el-menu-item>
                </el-submenu>

            </el-menu>
        </el-header>
        <el-container>
            <el-aside width="200px">Aside

            </el-aside>
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
        </el-container>
    </div>
      
</template>
<script>
    import axios from 'axios'
    import global from './GlobalPage.vue'
    const restweburl = global.ip;
    export default {
        data() {
            return {
                activeIndex: '3',
                activeIndex2: '3',
                fits: ['contain'],
                header_url: ''
            };
        },
        created() {
        axios.post(restweburl + "header")
            .then((res) => {
                this.header_url = res.data.data;
            })
            .catch(function (error) {
                console.log(error);
            });
        },
        methods: {
            handleSelect(key, keyPath) {
                console.log(key, keyPath);
                if(key==3){//消息中心
                    this.$router.push("msg");
                }
                if(key==6){//个人中心
                    this.$router.push("self");
                    // this.header_url= 'https://bjtu-ccn-hp.obs.cn-north-4.myhuaweicloud.com/header/aa.jpg';
                }
                if(key==7){//修改信息
                    this.$router.push("chg");
                }
                if(key==8){//注销
                    localStorage.removeItem("CCNtoken");
                    this.$router.push("../../login");
                }
                if(key==1){//班级
                    console.log(key, keyPath);
                    this.$router.push("class");
                }
                
            },
            closeID(){
                localStorage.removeItem("CCNtoken");
                this.$router.push("../../login");
            }
        }
    }
</script>


