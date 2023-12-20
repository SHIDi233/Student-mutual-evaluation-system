<template>
<el-menu :default-active="intro"  class="el-menu-demo" width=660px mode="horizontal" router="true" @select="handleSelect">
    <el-menu-item index="intro">Home</el-menu-item>
    <el-menu-item index="msg">Msg</el-menu-item>
    <el-menu-item index="code">Code</el-menu-item>
    <el-menu-item index="setting">Settings</el-menu-item>
    <el-submenu index="2">
      <template slot="title">发起审批</template>
      <el-menu-item index="A">成员变动审批</el-menu-item>
      <el-menu-item index="B">分支改动审批</el-menu-item>
      <el-menu-item index="C">会议直播审批</el-menu-item>
    </el-submenu>
  </el-menu>
</template>
<style scoped>
    .el-menu-item{
        width:250px;
        text-align:center
    }
</style>
  <script>
    export default {
      data() {
        return {
          activeIndex: '1',
          activeIndex2: '1'
        };
      },
      methods: {
        handleSelect(key, keyPath) {
          console.log(key, keyPath);
        }
      }
    }
  </script>