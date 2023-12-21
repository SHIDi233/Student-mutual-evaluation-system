<template>
    <div>
      <input
            type="file"
            name="filename"
            id="open"
            style="display: none"
            @change="changeFile"
          />
        <el-page-header @back="goBack" :content="ct" style="margin-bottom: 20px;">
        </el-page-header>

        <!-- 班级成员 -->
        <el-tabs type="border-card" v-model="activeName">
          <el-tab-pane label="班级成员" name="first">
            <el-table 
              
              :data="tableData"
              style="width: 980px;margin: 0 auto;font-size: 15px;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"
              max-height="8 50"
              v-loading="loading"
              :row-style="{height:'90px'}">
              <el-table-column
                fixed
                prop="role"
                label=""
                width="50">
                <template slot-scope="scope">
                  {{ scope.row.role===1? '老师':'学生' }}  
                </template>
              </el-table-column>
              
              <el-table-column
                  fixed
                  prop="header"
                  label=""
                  width="70">

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
                width="370">
              </el-table-column>
              <el-table-column
                
                label="操作"
                width="220">
                <template slot="header" slot-scope="scope" >
                  <el-button size="mini" style="margin-right: 100px;margin-top: 10px;float: right;" @click="upXls()">一键导入同学</el-button>
                  <el-button v-if="1<0">{{ scope.className }}</el-button>
                </template>
                <template slot-scope="scope">
                  <el-button
                  @click.native.prevent="startRow(scope.$index, scope.row)"
                    type="text"
                    size="small">
                    查看详细
                  </el-button>
                  <template>
                    <el-popconfirm @confirm="deleteRow(scope.$index, scope.row)"
                      title="确认删除同学吗">
                      <el-button
                        type="text"
                        size="small" slot="reference" style="margin-left: 15px;">
                        删除
                      </el-button>
                    </el-popconfirm>
                  </template>
                </template>
              </el-table-column>
            </el-table>
          </el-tab-pane>

          <!-- 班级作业 -->
          <el-tab-pane label="作业" name="second">
            <div>
              <el-table
                v-loading="loading"
                :data="data"
                :default-sort = "{prop: 'hwID', order: 'descending'}"
                style="width: 100%">
                <el-table-column
                  prop="hwID"
                  label="作业编号"
                  width="120">
                </el-table-column>
                <el-table-column
                  prop="name"
                  label="作业名称"
                  width="220">
                </el-table-column>
                <el-table-column
                  prop="ddl"
                  label="截止时间"
                  width="220">
                </el-table-column>
                <el-table-column v-if="this.role===1"
                  prop="isPublish"
                  label="是否发布"
                  width="120">
                  <template slot-scope="scope">
                    <span>{{scope.row.isPublish?'已发布':'待发布'}}</span>
                  </template>
                </el-table-column>
                <el-table-column v-if="this.role===0"
                  prop="isSubmit"
                  label="是否提交"
                  width="120">
                  <template slot-scope="scope">
                    <span>{{scope.row.isSubmit?'已提交':'待提交'}}</span>
                  </template>
                </el-table-column>
                <el-table-column
                  prop="cTime"
                  label="创建时间"
                  width="220">
                </el-table-column>
                <el-table-column v-if="this.role===0"
                  prop="evaScore"
                  label="分数"
                  width="120">
                  <template slot-scope="scope">
                    <span>{{scope.row.evaScore}}</span>
                      <el-button v-if="scope.row.evaScore!=null" type="text" style="margin-left: 22px;" @click="appeal(scope.row.hwID)">申诉</el-button>
                  </template>
                </el-table-column>
                <el-table-column
                  fixed="right"
                  label="操作"
                  width="180">
                  <template slot="header" slot-scope="scope" >
                    <el-button size="mini" v-if="!role==0" type="primary"  @click="publish()" :loading="loading" :disabled="role==0">新建作业</el-button>
                    <el-button v-if="1<0">{{ scope.className }}</el-button>
                  </template>
                  <template slot-scope="scope">
                    <el-button @click="handleClick(scope.row)" style="margin-left: 25px;" type="text" size="small">查看</el-button>
                    <el-button v-if="scope.row.isSubmit&&role==0" @click="handleClick_2(scope.row)" type="text" size="small">开始互评</el-button>
                  </template>
                </el-table-column>
              </el-table>
              </div>
          </el-tab-pane>

          <!-- 成绩分析 -->
          <el-tab-pane v-if="this.role===0" label="成绩分析(测试功能)" name="fourth">
            <el-tooltip class="item" effect="dark" content="Right Top 提示文字" placement="right-start">
              <!-- <el-button size="small" icon="el-icon-info" circle></el-button> -->
              <el-button @click="sendPost()">AI分析</el-button>
            </el-tooltip>
            <div>
              <pre style="font-size: 18px;width:60%;white-space: pre-wrap;">{{ response }}</pre>
              <el-skeleton v-if="this.chatAnime" :rows="3" animated />
            </div>
           
          </el-tab-pane>

          <!-- 讨论区 -->
          <el-tab-pane label="讨论区" name="third">
            <div style="padding-bottom: 10px;padding-top: 10px;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04);margin:auto;">
              <div style="margin:auto;width:70%;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">
                <div>
                  <div class="inputDeep">
                    <el-input
                      class="inputDeep"
                      type="textarea"
                      autosize
                      placeholder="请输入讨论主题"
                      v-model="inputTheme"
                      style="margin-top:10px;font-size:18px;font-weight: bolder;font-family: 'Helvetica Neue'">
                    </el-input>
                    <el-input
                      size="medium"
                      type="textarea"
                      :autosize="{minRows: 1}"
                      placeholder="请输入问题"
                      v-model="inputQuestion"
                      style="margin-top:10px;font-size:15px;font-family: 'Helvetica Neue'">
                    </el-input>
                  </div>
                </div>
                <div>
                  <el-button style="float: right;margin-right: 10px;margin-bottom: 10px;" size='small' type="primary" @click="sendTopic()">发起讨论</el-button>
                </div>
                <div style="clear:both;"></div>
              </div>
              <div style="margin:auto;padding-bottom: 10px;width:70%;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">
              <div v-if="dis.length>0" style="margin:auto;margin-top: 20px;width:100%;">
              <div v-for="item in dis" :key="item" class="text item" style="margin:auto;width:100%">
                <!-- <div style="margin:auto;width:100%;box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)"> -->
                  <div style="margin:auto;width:98%;margin-bottom: 10px;">
                    <el-col style="margin:auto;width:100%;margin-top: 10px;">
                      <el-card style="margin: auto;" shadow="hover" @click.native="openDisscuss(item.pID)">
                        <div style="display: flex; flex-direction: column;">
                          <span style="font-size: 26px;">{{item.pName}}</span>
                          <span style="margin-top: 14px;margin-left: 5px; font-size: 15px; color:rgb(128,128,128);">{{item.profile}}</span>
                        </div>
                        <div>
                          <span style="float: right; font-size: 12px;color:rgb(160,160,160);">{{item.cTime}}</span>
                          <span style="float: right; margin-right: 10px; font-size: 14px;color:rgb(160,160,160);">{{item.uName}}</span>
                          <el-avatar style="float: right;" :size="20" :src="item.head"></el-avatar>
                        </div>
                        <div style="clear:both;"></div>
                      </el-card>
                      <div style="clear:both;"></div>
                    </el-col>
                  </div>
                  <div style="clear:both;"></div>
                <!-- </div> -->
                <!-- <el-divider></el-divider> -->
                <div style="clear:both;"></div>
              </div>
              <div style="clear:both;"></div>
            </div>
          </div>
            </div>
          </el-tab-pane>

          <!-- 公告栏 -->
          <el-tab-pane label="公告栏" name="forth">
            <div style="box-shadow:0 2px 4px rgba(0, 0, 0, .12), 0 0 6px rgba(0, 0, 0, .04)">
              <div style ="margin-left: 20px;margin-right: 20px;margin-bottom: 0px;">
                <div v-if="this.role===1" >
                  <el-input
                    style ="margin-top : 20px;margin-bottom: 10px;"
                    type="textarea"
                    :rows="18"
                    placeholder="请输入内容"
                    v-model="not">
                  </el-input>
                  <el-button type="primary" style="float:right;margin-bottom: 10px;parent { overflow: hidden }" @click="sendNotation()">发布</el-button>
                  <div style="clear:both;"></div>
                </div>
                <div style="margin-top: 10px;" v-if="this.role===0">
                  <p style="white-space: pre-wrap;">{{ this.not }}</p>
                </div>
              </div>
            </div>
            
          </el-tab-pane>
        </el-tabs>
        <el-empty v-if="this.activeName==0" description="看点什么"></el-empty>
        <el-dialog
          title="发起讨论"
          :visible.sync="dialogVisible_2"
          width="30%"
          :before-close="handleClose">
          <el-input v-model="inputTheme" placeholder="请输入主题"></el-input>
          <el-input v-model="inputQuestion" placeholder="请输入问题"></el-input>
          <el-button @click="sendTopic()">发送</el-button>
        </el-dialog>
        
        <!-- 回复回帖 -->
        <el-dialog
          :title="pName"
          :visible.sync="dialogVisible_3"
          width="80%"
          fullscreen="true"
          :before-close="handleClose"
          >
          <div >
            <div style="margin:auto" v-if="reply!=''">
              <el-avatar style="float: left;" :size="40" :src="reply.head"></el-avatar>
              <div style="display: flex; flex-direction: column;">
                <span style="font-size: 18px;">{{reply.uName}}</span>
                <span style="font-size: 24px;margin-top: 10px;margin-left: 40px;">{{reply.pName}}</span>
                <span style="font-size: 18px;margin-top: 8px;margin-bottom: 10px;margin-left: 40px;">{{reply.content}}</span>
              </div>
              <span style="float: right; font-size: 12px;color:rgb(160,160,160);"> {{reply.cTime}}</span>
            </div>
            <el-divider></el-divider>
            <div style="margin-bottom: 1000px;" v-if="reply!=''" >
              <div v-if="reply.reply.length>0">
                <div v-for="item in reply.reply" :key="item" class="text item">
                  <div>
                    <el-col  style="margin-bottom: 10px;width:100%">
                      <el-card shadow="hover">
                        <el-avatar style="float: left;" :size="36" :src="item.head"></el-avatar>
                        <div style="display: flex; flex-direction: column;">
                          <span style="float: right; margin-left: 10px; font-size: 18px;color:rgb(160,160,160);">{{item.uName}}</span>
                          <span style="margin-left: 10px;font-size: 18px;">{{item.content}}</span>
                        </div>
                        <div>
                          <span style="float: right; font-size: 16px;color:rgb(160,160,160);">{{item.sendTime}}</span>
                          <span style="float: right; margin-right: 14px; font-size: 14px;color:rgb(160,160,160);">{{item.floor}}楼</span>
                        </div>
                      </el-card>
                    </el-col>
                  </div>
                </div>
              </div>
            </div>
            
            <div style="position: fixed;bottom: 0;left: 0;width: 100%;">
              <div>
                <el-input v-model="pContent" style="margin-left: 10px ;width: 90%;"></el-input>
                <el-button @click="recall()" type="primary"  style = "float: right;margin-right: 20px ;">发送</el-button>
              </div>
            </div>
          </div>
        </el-dialog>
        <el-dialog
          title="控制台输出"
          :visible.sync="dialogVisible"
          width="30%"
          :before-close="handleClose">
          <span style="white-space:pre-wrap;">{{ this.console }}</span>
          <span slot="footer" class="dialog-footer">
            <el-button type="primary" @click="dialogVisible = false;refresh()">O K</el-button>
          </span>
        </el-dialog>
    </div>
</template>

<style scoped>
 .inputDeep>>>.el-textarea__inner{
    border:0;
    resize: none;
  } 
</style>


<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  // const restweburl = "http://192.168.10.167:8888/";
  export default {
    
    methods: {
      recall(){
        var params4 = new URLSearchParams();
        params4.append('pID',this.pID);
        params4.append('content',this.pContent);
        axios.post(restweburl + "reply",params4)
        .then((res) => {
          if(res.data.msg=="success"){
            this.$message({
              message: '发送成功',
              type: 'success'
            });
            this.pContent='';
            this.openDisscuss(this.pID);
          }
          else{
            this.$message({
              message: res.data.msg,
              type: 'warning'
            });
          }
        })
        .catch(function (error) {
          console.log(error);
          this.$message({
            message: 'error',
            type: 'warning'
          });
        });
      },
      stopGenerating() {
        this.controller.abort()
        this.isStopped = true
      },
      restartGenerating() {
        this.controller = new AbortController()
        this.sendPost()
      },

      async sendPost() {
        this.chatAnime = true;

        this.controller = new AbortController()
        this.response = ''
        this.isStopped = false
        const response = await fetch(restweburl + "streamTest", {
          method: 'POST',
          headers: { 'Content-Type': 'application/json','token': localStorage.getItem("CCNtoken"), 'cID': this.$route.params.classID},
          // body: JSON.stringify({ name: this.name }),
          signal: this.controller.signal,
        })
        const reader = response.body.getReader()
        let a=1;let b=2
        while (b>a) {
          if (this.isStopped) break
          const { done, value } = await reader.read()
          if (done){
            this.chatAnime = false;
            break;
          }
          this.response += new TextDecoder().decode(value)
        }
      },

      ana(){
        var params4 = new URLSearchParams();
        params4.append('cID',this.$route.params.classID);
        axios.post(restweburl + "streamTest",params4)
        .then((res) => {
          alert(1)
          this.anad = res.data;
        })
        .catch(function (error) {
          console.log(error);
        });
      },
      appeal(hwID){
        var params = new URLSearchParams();
        params.append('hwID',hwID);
        axios.post(restweburl + "appeal",params)
        .then((res) => {
          if(res.data.msg=='success'){
            this.$message({
              message: '操作成功',
              type: 'success'
            });
          }
          else{
            this.$message({
              message: res.data.msg,
              type: 'warning'
            });
          }
        })
        .catch(function (error) {
          console.log(error);
        });
      },
      sendTopic(){
        if(this.inputQuestion=='' || this.inputTheme=='')
          return;
        var params = new URLSearchParams();
        params.append('cID',this.$route.params.classID);
        params.append('content',this.inputQuestion);
        params.append('name',this.inputTheme);
        params.append('teacherOnly',1);
        axios.post(restweburl + "createPost",params)
        .then((res) => {
          if(res.data.msg=='success'){
            this.$message({
              message: '发布成功',
              type: 'success'
            });
            this.inputQuestion=''; 
            this.inputTheme='';
            var params1 = new URLSearchParams();
            params1.append('cID',this.$route.params.classID);
            axios.post(restweburl + "getPosts",params1)
              .then((res) => {
                this.dis = res.data.data;
                this.console.log(res.data.data)
              })
            .catch(function (error) {
                console.log(error);
            })
          }
          else{
            this.$message({
              message: res.data.msg,
              type: 'warning'
            });
          }
        })
        .catch(function (error) {
          console.log(error);
        });
      },
      openDisscuss(id){
        this.pID = id;
        var params = new URLSearchParams();
                params.append('pID',id);
                axios.post(restweburl + "getReply",params)
                .then((res) => {
                  this.reply = res.data.data;
                  this.dialogVisible_3=true;
                })
                .catch(function (error) {
                  console.log(error);
                });
      },
      publish(){
          axios.get(restweburl + "createHomework")
            .then((res) => {
              let routeUrl = this.$router.resolve({
                path: `/hw/publish/${res.data.data}/${this.classID}`,
              });
              window.open(routeUrl .href, '_blank');
              // this.$router.push(`hw/publish/${res.data.data}`);
            })
            
        },
        handleClick(row){
          if(this.role==1){
            // this.$router.push(`hw/publish/${row.hwID}`);
            let routeUrl = this.$router.resolve({
                    path: `/hw/publish/${row.hwID}/${this.classID}`,
                    // query: {id:96}
              });
              window.open(routeUrl .href, '_blank');
          }
          else if(this.role==0){
            if(row.isSubmit){
              // this.$router.push(`hw/watch/${row.hwID}`);
              let routeUrl = this.$router.resolve({
                    path: `/hw/watch/${row.hwID}`,
                    // query: {id:96}
              });
              window.open(routeUrl .href, '_blank');

            }
            else{
              let routeUrl = this.$router.resolve({
                    path: `/hw/write/${row.hwID}`,
                    // query: {id:96}
              });
              window.open(routeUrl .href, '_blank');
              // this.$router.push(`hw/write/${row.hwID}`);
            }
        }
        },
        handleClick_2(row){
          
          let routeUrl = this.$router.resolve({
                    path: `/hw/evaluate/${row.hwID}`,
                    // query: {id:96}
              });
              window.open(routeUrl .href, '_blank');
        
        },
      goBack() {
        console.log('go back');
        this.$router.push('../class');
      },
      startRow(index, rows) {
        console.log(index,rows);
        // this.$router.push(`approval/${rows.type}/${rows.id}/${rows.sendTime}/${rows.senderName}/${rows.content}`)
        // this.drawer=true;
        // this.nid = rows.noteID;
        alert("该功能暂时不可用");
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
      },
      upXls(){
        document.getElementById("open").click();
      },
      changeFile() {
            // let $vm=this.$refs.md;
            const fu = document.getElementById("open");
            if (fu == null) return;

            this.upload=true;

            console.log(fu.files[0]);
            // 第一步.将图片上传到服务器.
            var formdata = new FormData();
            formdata.append('file', fu.files[0]);

            axios({
                url: restweburl+'uploadXls',
                method: 'post',
                data: formdata,
                headers: { 'cID': this.$route.params.classID },
              }).then(async (u) => {
                this.dialogVisible=true;
                for(let i=0;i<u.data.data.length;i++){
                  this.console+="> "+u.data.data[i]+"\n";
                  await new Promise(resolve => setTimeout(resolve, 200));
                }
                this.console+="> done.";
            })
        },
        refresh(){
          axios.get(restweburl + "getRole")
          .then((res) => {
            this.role = res.data.data;
            var params = new URLSearchParams();
            params.append('cID',this.$route.params.classID);
            if(res.data.data==0){
              axios.post(restweburl + "getHomework",params)
              .then((res) => {
                for(let i=0;i<res.data.data.length;i++){
                  // if(res.data.data[i].cID==this.$route.params.classID){
                    this.data.push(res.data.data[i]);
                  // }
                }
              })
              .catch(function (error) {
                  console.log(error);
              })
            }
            else if(res.data.data==1){
              axios.post(restweburl + "listHomework",params)
              .then((res) => {
                // alert(this.$route.params.classID)
                for(let i=0;i<res.data.data.length;i++){
                  console.log(res.data.data[i].cID)
                  if(res.data.data[i].cID==this.$route.params.classID){
                    this.data.push(res.data.data[i]);
                    console.log(this.data)
                  }
                }
              })
              .catch(function (error) {
                  console.log(error);
              })
            }
          })
          .catch(function (error) {
              console.log(error);
          })

          axios.get(restweburl+'getClasses').then(response => {
            if(response.data.code==1){
            if(response.data.msg=='success'){
                this.options = response.data.data;
            }
            else{
                this.$message.error(response.data.msg);
            }
            }
            else{
                alert("网络错误")
            }
          }).catch(error => {
              alert(error)
              alert("请求失败")
          })


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
        sendNotation(){
          var params = new URLSearchParams();
          params.append('cID',this.$route.params.classID);
          params.append('content',this.not);
          axios.post(restweburl + "createNotation",params)
          .then((res) => {
            this.$message({
            message: res.data.msg,
            type: 'success'
        });
          })
          .catch(function (error) {
            console.log(error);
          });
        }
    },
    data() {
      return {

        activeName:'first',

        chatAnime : false,

        classID:this.$route.params.classID,

        dis:[],

        loading:true,
        tableData: [
        ],
        drawer: false,
        nid:"",

        pID:0,
        pContent:'',

        ct:"",
        cd:"",

        data:[],

        anad:'',

        role:-1,

        classes:[],
        options:[],

        dialogVisible:false,
        dialogVisible_2:false,
        dialogVisible_3:false,
        console:"",

        inputQuestion:"",
        inputTheme:"",

        reply:"",

        not:"",

        name: '',
        response: '',
        controller: new AbortController(),
        isStopped: false

      }
    },
    created() {

      

      var params3 = new URLSearchParams();
          params3.append('cID',this.$route.params.classID);
          axios.post(restweburl + "getClassNotation",params3)
          .then((res) => {
            this.not = res.data.data;
          })
          .catch(function (error) {
            console.log(error);
          });

      axios.get(restweburl + "getRole")
          .then((res) => {
            this.role = res.data.data;
            var params = new URLSearchParams();
            params.append('cID',this.$route.params.classID);
            if(res.data.data==0){
              axios.post(restweburl + "getHomework",params)
              .then((res) => {
                for(let i=0;i<res.data.data.length;i++){
                  // if(res.data.data[i].cID==this.$route.params.classID){
                    this.data.push(res.data.data[i]);
                  // }
                }
              })
              .catch(function (error) {
                  console.log(error);
              })
            }
            else if(res.data.data==1){
              axios.post(restweburl + "listHomework",params)
              .then((res) => {
                // alert(this.$route.params.classID)
                for(let i=0;i<res.data.data.length;i++){
                  console.log(res.data.data[i].cID)
                  if(res.data.data[i].cID==this.$route.params.classID){
                    this.data.push(res.data.data[i]);
                    console.log(this.data)
                  }
                }
              })
              .catch(function (error) {
                  console.log(error);
              })
            }
          })
          .catch(function (error) {
              console.log(error);
          })

          axios.get(restweburl+'getClasses').then(response => {
            if(response.data.code==1){
            if(response.data.msg=='success'){
                this.options = response.data.data;
            }
            else{
                this.$message.error(response.data.msg);
            }
            }
            else{
                alert("网络错误")
            }
          }).catch(error => {
              alert(error)
              alert("请求失败")
          })


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

        var params1 = new URLSearchParams();
        params1.append('cID',this.$route.params.classID);
        axios.post(restweburl + "getPosts",params1)
          .then((res) => {
            this.dis = res.data.data;
            this.console.log(res.data.data)
          })
          .catch(function (error) {
              console.log(error);
          })

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