<template>
  <div>
    <router-view></router-view>

    <div>
      <!-- <el-button type="primary"  @click="drawer_2 = true">加入班级</el-button> -->
      <el-button type="primary" @click="drawer_2 = true" style="float: right;margin-left: 10px;margin-bottom: 10px;">加入班级</el-button>
      <el-button v-if="!b" @click="drawer = true " :loading="loading" :disabled="b" style="float: right;margin-left: 10px;">新建班级</el-button>
    </div>
    <div style="margin-top: 10px;">
      <el-table
        v-loading="lodingTable"
        :data="tableData"
        border
        style="width: 100%">
        <el-table-column
          prop="classID"
          label="班级编号"
          width="120">
        </el-table-column>
        <el-table-column
          prop="className"
          label="名称"
          width="120">
        </el-table-column>
        <el-table-column
          prop="introduction"
          label="简介"
          width="120">
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="100">
          <template slot-scope="scope">
            <el-button @click="handleClick(scope.row)" type="text" size="small">查看</el-button>
            <template>
              <el-popconfirm style="margin-left: 18px;" title="确定删除班级吗" @confirm="handleDelete(scope.row)">
                <el-button slot="reference"  type="text" size="small">删除</el-button>
              </el-popconfirm>
            </template>
            <!-- <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button> -->
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <el-drawer
      title="创建页"
      :visible.sync="drawer"
      :direction="direction"
      :before-close="handleClose">
      <span style="margin-left: 20px;">由此来创建一个新的班级!</span>
      <el-divider></el-divider>
      <div style="margin-left: 20px;margin-right: 20px;">
        <el-input style="margin-top: 10px;"
          placeholder="请输入班级名称"
          v-model="input_name"
          clearable>
        </el-input>
        <el-input style="margin-top: 10px;"
          placeholder="请输入学科"
          v-model="input_sub"
          clearable>
        </el-input>
        <el-input style="margin-top: 10px;"
          type="textarea"
          :rows="2"
          placeholder="请输入课程简介"
          v-model="textarea">
        </el-input>
      </div>
      
      <el-button style="float:right;margin-right: 20px; margin-top: 10px;" type="primary" @click="Click()" :loading="bc" >创建</el-button>
    </el-drawer>
    <el-drawer
      title="加入页"
      :visible.sync="drawer_2"
      :direction="direction"
      :before-close="handleClose">
      <span style="margin-left: 20px;">由此来加入一个新的班级!</span>
      <div style="margin-left: 20px;margin-top: 10px;">
        <el-input 
          placeholder="请输入班级代码"
          v-model="input_code"
          @input="handlerChange"
          clearable>
        </el-input>
        <el-divider></el-divider>
        <el-descriptions :title="className" :colon="false" :column="1">
            <el-descriptions-item :label="classID" ></el-descriptions-item>
            <el-descriptions-item :label="subject" ></el-descriptions-item>
            <el-descriptions-item :label="introduction" ></el-descriptions-item>
        </el-descriptions>
        <el-divider></el-divider>
      </div>
      
      
      <el-button style="float:right;margin-right: 20px; margin-top: 10px;" type="primary" @click="Click_2()" :loading="bc_2" :disabled="b_2" >加入</el-button>
    </el-drawer>
  </div>
  
</template>

<script>
    import axios from 'axios'
    import global from './GlobalPage.vue'
    const restweburl = global.ip;
  export default {
    methods: {
      handleClick(row) {
        console.log(row);
        this.$router.push('class/'+row.classID);
      },
      handleDelete(row) {
        console.log(row);
        var params = new URLSearchParams();
        params.append('classID',row.classID);
        
        axios.post(restweburl + "deleteClass",params)
        .then((res) => {
          this.bc=false;
            if(res.data.msg=='success'){
              this.$message({
              message: '删除成功',
              type: 'success'
            });
            axios.get(restweburl + "getClasses")
            .then((res) => {
                this.tableData = res.data.data;
            })
            .catch(function (error) {
                console.log(error);
            });
          }
          else{
            this.$message.error('删除失败');
          }
        });
      },
      Click() {
        this.bc=true;
        var params = new URLSearchParams();
        params.append('className',this.input_name);
        params.append('introduction',this.textarea);
        params.append('subject',this.input_sub); 
        
        axios.post(restweburl + "createClass",params)
        .then((res) => {
          this.bc=false;
            if(res.data.msg=='success'){
              this.$message({
              message: '创建成功',
              type: 'success'
            });
          this.drawer=false;
          axios.get(restweburl + "getClasses")
          .then((res) => {
              this.tableData = res.data.data;
          })
          .catch(function (error) {
              console.log(error);
          });
          }
          else{
            this.$message.error('创建失败');
          }
        })
        .catch(function (error) {
            console.log(error);
        })
      },
      handlerChange(e) {
        this.bc_2 = true;
        this.b_2 = true;
          console.log(e);
          var params = new URLSearchParams();
          params.append('code',this.input_code);
          axios.post(restweburl + "searchClass",params)
          .then((res) => {
              if(res.data.msg=='success'){
                this.classID = res.data.data.classID;
                this.className = '班级名称：'+res.data.data.className;
                this.subject = '课程名字：'+res.data.data.subject;
                this.introduction = '课程简介：'+res.data.data.introduction;
                this.b_2 = false;
                this.bc_2 = false;
              // this.drawer=false;
              // axios.get(restweburl + "getClasses")
              // .then((res) => {
              //     this.tableData = res.data.data;
              // })
              // .catch(function (error) {
              //     console.log(error);
              // });
            }
            else{
              this.bc_2 = false;
              this.classID = "";
              this.className = "";
              this.subject = "";
              this.introduction = "";
            }
            
          })
          .catch(function (error) {
              console.log(error);
          })
      },
      Click_2() {
        this.bc=true;
        var params = new URLSearchParams();
        params.append('classID',this.classID);
        
        axios.post(restweburl + "joinClass",params)
        .then((res) => {
            if(res.data.msg=='success'){
              this.$message({
              message: '加入班级'+this.className+'成功',
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
      }
    },
    created() {
      axios.get(restweburl + "isTeacher")
        .then((res) => {
          this.b = res.data;
          this.b = this.b ? false:true;
          this.loading = false
        })
        .catch(function (error) {
            console.log(error);
        }),

        axios.get(restweburl + "getClasses")
        .then((res) => {
            this.tableData = res.data.data;
            this.lodingTable=false;
        })
        .catch(function (error) {
            console.log(error);
        });
      },

    data() {
      return {
        drawer: false,
        drawer_2: false,
        direction: 'rtl',
        tableData: [],
        loading: true,
        b:false,

        input_name:"",
        input_sub:"",
        textarea:"",
        bc:false,

        input_code:"",
        b_2:true,
        bc_2:false,

        classID:"",
        className:"",
        subject:"",
        introduction:"",
        classdata:[],

        lodingTable:true,
      }
    }
  }
</script>