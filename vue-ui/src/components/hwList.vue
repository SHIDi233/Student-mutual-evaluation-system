<template>
  <div>
      <el-container>
      <el-header>
          <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
              <el-menu-item index="1">班级</el-menu-item>
              <el-menu-item index="3">
                  <el-badge :value="numUnRead" :hidden="numUnRead===0" class="item">消息中心</el-badge>
              </el-menu-item>
              <el-submenu index="5" style="float: right;">
                      <template slot="title">
                          <el-avatar :size="35" :src="header_url"></el-avatar>
                      </template>
                  <el-menu-item index="6">个人中心</el-menu-item>
                  <el-menu-item index="7">修改信息</el-menu-item>
                  <el-menu-item index="8">注销</el-menu-item>
              </el-submenu>
              <el-menu-item v-loading="this.name===''" style="float: right;font-weight: bold;">你好，{{this.name}}，欢迎使用CCN互评系统👋</el-menu-item>

          </el-menu>
      </el-header>
      <el-container>
          <el-aside width="200px">

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
              activeIndex: '0',
              activeIndex2: '3',
              fits: ['contain'],
              header_url: '',

              name:'',
              numUnRead:0,
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

          axios.post(restweburl + "info")
          .then((res) => {
              this.name = res.data.data.name;
              // this.label = res.data.data.introduction;
              // this.context=res.data.data.readme;
              // this.school=res.data.data.school+'/'+res.data.data.majority;
          })
          .catch(function (error) {
              console.log(error);
          });

          axios.post(restweburl + "getUnread")
          .then((res) => {
              this.numUnRead = res.data.data;
          })
          .catch(function (error) {
              console.log(error);
          });
      },
      methods: {
          handleSelect(key, keyPath) {
              console.log(key, keyPath);
              if(key==3){//消息中心
                  this.$router.push("/msg");
              }
              if(key==6){//个人中心
                  this.$router.push("/self");
                  // this.header_url= 'https://bjtu-ccn-hp.obs.cn-north-4.myhuaweicloud.com/header/aa.jpg';
              }
              if(key==7){//修改信息
                  this.$router.push("/chg");
              }
              if(key==8){//注销
                  localStorage.removeItem("CCNtoken");
                  this.$router.push("../../login");
              }
              if(key==1){//班级
                  console.log(key, keyPath);
                  this.$router.push("/class");
              }
              if(key==2){//作业中心
                  this.$router.push("/hw");
              }
              
          },
          closeID(){
              localStorage.removeItem("CCNtoken");
              this.$router.push("../../login");
          }
      }
  }
</script>


