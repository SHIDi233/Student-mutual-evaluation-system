<template>
    <div>
        <!-- <el-header>
            
        </el-header> -->
        <el-main>
            <el-page-header @back="goBack" content="审批详细">
            </el-page-header>
        </el-main>
        <el-main>

        <el-table
    :data="tableData"
    style="width: 100%"
    max-height="8 50">
    <el-table-column
      fixed
      prop="type"
      label="类型"
      width="150">
    </el-table-column>
    <el-table-column
      fixed
      prop="sendTime"
      label="日期"
      width="250">
    </el-table-column>
    <el-table-column
      fixed
      prop="id"
      label="单号"
      width="170">
    </el-table-column>
    <el-table-column
      prop="senderName"
      label="发送人"
      width="100">
    </el-table-column>
    <el-table-column
      prop="content"
      label="简介"
      width="320">
    </el-table-column>
    
  </el-table>
        </el-main>
        <el-main>
            <el-button @click="acceptBtn()" type="primary">通过</el-button>
            <el-button @click="refuseBtn()" type="danger">拒绝</el-button>
        </el-main>
    </div>
</template>

<script>
    import axios from 'axios'
    import global from './GlobalPage.vue'
  const restweburl = global.ip;
    // const restweburl = "http://192.168.10.167:8888/";
    export default {
    created() {
      // this.tableData = this.$route.params;
    },
    methods: {
        acceptBtn(){
            var params = new URLSearchParams();
        if(this.$route.params.type=='SA'){
                params.append('saID',this.$route.params.id);
                params.append('grant','1');
            axios.post(restweburl + "SysAppr", params)
        .then((res) => {
          this.tableData = res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        }
        if(this.$route.params.type=='WA'){  
                params.append('waID',this.$route.params.id);
                params.append('grant','1');
            axios.post(restweburl + "WareAppr", params)
        .then((res) => {
          this.tableData = res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        }
      },
      refuseBtn(){
        var params = new URLSearchParams();
        if(this.$route.params.type=='SA'){
                params.append('saID',this.$route.params.id);
                params.append('grant','-1');
            axios.post(restweburl + "SysAppr", params)
        .then((res) => {
          this.tableData = res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        }
        if(this.$route.params.type=='WA'){
                params.append('waID',this.$route.params.id);
                params.append('grant','-1');
            axios.post(restweburl + "WareAppr", params)
        .then((res) => {
          this.tableData = res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        }
    },
    goBack(){
        this.$router.back();
    }
    },
    data() {
      return {
        tableData: [
          {
            "type" : this.$route.params.type,
            "id" : this.$route.params.id,
            "sendTime" : this.$route.params.sendTime,
            "senderName" : this.$route.params.senderName,
            "content" : this.$route.params.content,
          }
        ]
      }
    }
  }
</script>