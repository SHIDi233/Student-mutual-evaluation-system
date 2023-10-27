<template>
    <div>
        <el-page-header @back="goBack" :content="ct">
        </el-page-header>
        <el-button type="primary" @click="drawer = true"> </el-button>
            <el-table
    :data="tableData"
    style="width: 100%"
    max-height="8 50"
    v-loading="loading">
    <!-- <el-table-column
      fixed
      prop="type"
      label="类型"
      width="150">
    </el-table-column> -->
    <el-table-column
        fixed
        prop="header"
        label=""
        width="150">

        <template slot-scope="scope">
            <img :src="scope.row.header" width="40" height="40" class="head_pic"/>
        </template>
    </el-table-column>
    <el-table-column
      fixed
      prop="studentID"
      label="学号"
      width="250">
    </el-table-column>
    <el-table-column
      fixed
      prop="name"
      label="姓名"
      width="170">
    </el-table-column>
    <!-- <el-table-column
      prop="scope"
      label="范围"
      width="100">
    </el-table-column>
    <el-table-column
      prop="content"
      label="简介"
      width="320">
    </el-table-column> -->
    <el-table-column
      fixed="right"
      label="操作"
      width="120">
      <template slot-scope="scope">
        <el-button
        @click.native.prevent="startRow(scope.$index, scope.row)"
          type="text"
          size="small">
          查看详细
        </el-button>
        <el-button
        @click.native.prevent="deleteRow(scope.$index, scope.row)"
          type="text"
          size="small">
          删除
        </el-button>
      </template>
    </el-table-column>
  </el-table>
    </div>
</template>


<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  // const restweburl = "http://192.168.10.167:8888/";
  export default {
    
    methods: {
      goBack() {
        console.log('go back');
        this.$router.push('../class');
      },
      startRow(index, rows) {
        console.log(index,rows);
        // this.$router.push(`approval/${rows.type}/${rows.id}/${rows.sendTime}/${rows.senderName}/${rows.content}`)
        this.drawer=true;
        this.nid = rows.noteID;
        alert(this.nid);
        // axios.get(restweburl + "Notations")
        // .then((res) => {
        //   this.tableData = res.data.data;
        //   this.loading=false;
        // })
        // .catch(function (error) {
        //   console.log(error);
        // });
      },
      deleteRow(index, rows) {
        var params = new URLSearchParams();
        params.append('classID',this.$route.params.classID);
        params.append('uID',rows.uID);
        axios.post(restweburl + "kickMember",params)
        .then((res) => {
            if(res.data.msg=='success'){
                this.$message({
                    message: '踢出'+rows.name+'成功',
                    type: 'success'
                });
                var params = new URLSearchParams();
                params.append('classID',this.$route.params.classID);
                axios.post(restweburl + "listMember",params)
                .then((res) => {
                  this.tableData = res.data.data;
                  this.loading=false;
                })
                .catch(function (error) {
                  console.log(error);
                });
            }
            else{
                this.$message.error(res.data.msg);
            }
        })
        .catch(function (error) {
          console.log(error);
        });
      }
    },
    data() {
      return {
        loading:true,
        tableData: [
        ],
        drawer: false,
        nid:"",

        ct:"",
        cd:""
      }
    },
    created() {
        var params = new URLSearchParams();
        params.append('classID',this.$route.params.classID);
        axios.post(restweburl + "listMember",params)
        .then((res) => {
          this.tableData = res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });

        axios.get(restweburl + `getCode?classID=${this.$route.params.classID}`)
        .then((res) => {
          this.ct = res.data.data.className+' 代码('+res.data.data.code+')';
        })
        .catch(function (error) {
          console.log(error);
        });
  },
  }
</script>