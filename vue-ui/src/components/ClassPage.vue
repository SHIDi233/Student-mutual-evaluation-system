<template>
  <div>
    <!-- 班级列表 -->
    <div style="margin-top: 10px;margin: 0 auto;">
      <el-table
        v-loading="lodingTable"
        :data="tableData.filter(data => !search || data.className.toLowerCase().includes(search.toLowerCase())) || data.classID==search"
        style="width: 1050px;margin: 0 auto;font-size: 15px;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"
        :row-style="{height:'80px'}"
        :cell-style="{padding:'0px'}">
        <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <div style="margin-left:50px;">
              <el-form-item label="班级编号:">
                <span>{{ props.row.classID }}</span>
              </el-form-item>
              <el-form-item label="课程名字:">
                <span>{{ props.row.className }}</span>
              </el-form-item>
              <el-form-item label="课程简介:">
                <span>{{ props.row.introduction }}</span>
              </el-form-item>
              <el-divider></el-divider>
              <el-form-item label="授课老师:">
                <span>{{ props.row.teacher }}</span>
              </el-form-item>
              <el-form-item label="班级人数:">
                <span>{{ props.row.numOfStu }}</span>
              </el-form-item>
            </div>
            <div style="float:right;margin-left:30px;">
              <template>
                <el-popconfirm style="margin-right:30px;" title="确定删除班级吗" @confirm="handleDelete(scope.row)">
                  <!-- <el-button slot="reference"  type="text" size="small">删除</el-button> -->
                  <el-button slot="reference"
                    size="mini"
                    type="danger"
                    @click="handleDelete(scope.row)">删除班级</el-button>
                </el-popconfirm>
              </template>
            </div>
          </el-form>
        </template>
      </el-table-column>
        <el-table-column
          prop="classID"
          label="班级编号"
          width="120"
          >
        </el-table-column>
        <el-table-column
          prop="className"
          label="名称"
          width="120">
        </el-table-column>
        <el-table-column
          prop="introduction"
          label="简介"
          width="320">
        </el-table-column>
        <el-table-column
          label="操作"
          width="400">
          <template slot="header" slot-scope="scope" >
            <el-button size="mini" type="primary" @click="drawer_2 = true" style="float: right;margin-left: 10px;margin-bottom: 10px;">加入班级</el-button>
            <el-button v-if="!b" size="mini" @click="drawer = true " :loading="loading" :disabled="b" style="float: right;margin-left: 10px;">新建班级</el-button>
            <el-input 
              size="mini"
              v-model="search"
              style="width:200px;float: right;"
              placeholder="输入课程名字搜索"/>
            <el-button v-if="1<0">{{ scope.className }}</el-button>
          </template>
          <template slot-scope="scope">
            <el-button style="float:right;margin-right:100px;" @click="handleClick(scope.row)" type="text" size="small">进入</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 创建班级 -->
    <el-drawer
      title="创建页"
      :visible.sync="drawer"
      :direction="direction">
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

    <!-- 加入班级 -->
    <el-drawer
      title="加入页"
      :visible.sync="drawer_2"
      :direction="direction">
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

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 90px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

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

        search: ''
      }
    }
  }
</script>