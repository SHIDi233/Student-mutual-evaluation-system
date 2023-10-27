<template>
    <div>
        <el-table
    :data="tableData"
    style="width: 100%"
    max-height="8 50"
    v-loading="loading">
    <el-table-column
      fixed
      prop="type"
      label="类型"
      width="150">
    </el-table-column>
    <el-table-column
      fixed
      prop="time"
      label="日期"
      width="250">
    </el-table-column>
    <el-table-column
      fixed
      prop="noteID"
      label="单号"
      width="170">
    </el-table-column>
    <el-table-column
      prop="scope"
      label="范围"
      width="100">
    </el-table-column>
    <el-table-column
      prop="content"
      label="简介"
      width="320">
    </el-table-column>
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
      </template>
    </el-table-column>
  </el-table>

  <el-drawer
      title="加入页"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose">
      <span>由此来审批!</span>

      
      <el-button type="primary" @click="Click_2()"  >拒绝</el-button>
      <el-button type="primary" @click="Click()"  >同意</el-button>
    </el-drawer>
    </div>
</template>

<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  // const restweburl = "http://192.168.10.167:8888/";
  export default {
    
    methods: {
      Click() {
        this.bc=true;
        var params = new URLSearchParams();
        params.append('noteID',this.noteID);
        params.append('grant','1');
        params.append('type',this.type);
        
        axios.post(restweburl + "classApprove",params)
        .then((res) => {
            if(res.data.msg=='success'){
              this.$message({
              message: '审批通过',
              type: 'success'
            });
            this.drawer=false;
            this.input_code="";
          }
          else{
            this.$message.error('创建失败');
          }
           
        })
        .catch(function (error) {
            console.log(error);
        })
      },
      Click_2() {
        this.bc=true;
        var params = new URLSearchParams();
        params.append('noteID',this.noteID);
        params.append('grant','-1');
        params.append('type',this.type);
        
        axios.post(restweburl + "classApprove",params)
        .then((res) => {
            if(res.data.msg=='success'){
              this.$message({
              message: '审批拒绝',
              type: 'success'
            });
            this.drawer_2=false;
            this.input_code="";
          }
          else{
            this.$message.error('创建失败');
          }
           
        })
        .catch(function (error) {
            console.log(error);
        })
      },
      startRow(index, rows) {
        console.log(index,rows);
        // this.$router.push(`approval/${rows.type}/${rows.id}/${rows.sendTime}/${rows.senderName}/${rows.content}`)
        this.drawer=true;
        this.nid = rows.noteID;

        this.noteID = rows.noteID;
        this.type = rows.type;
        // alert(this.nid);
        // axios.get(restweburl + "Notations")
        // .then((res) => {
        //   this.tableData = res.data.data;
        //   this.loading=false;
        // })
        // .catch(function (error) {
        //   console.log(error);
        // });
      }
    },
    data() {
      return {
        loading:true,
        tableData: [
        ],
        drawer: false,
        nid:"",
      }
    },
    created() {
      axios.get(restweburl + "Notations")
        .then((res) => {
          this.tableData = res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
  },
  }
</script>