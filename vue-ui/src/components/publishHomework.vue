<template>
    <div>
        <el-header>
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                <el-menu-item v-loading="this.name===''" style="float: right;font-weight: bold;">你好，{{this.name}}，欢迎使用CCN互评系统👋</el-menu-item>
            </el-menu>
        </el-header>
        <el-container>
        <el-aside>
        </el-aside>
        <el-main>
            <el-tabs>
            <el-tab-pane label="作业内容" name="first">
                <div class="demo-input-suffix"  >
            作业名：
            <el-input v-model="hwName" placeholder="作业名"></el-input>
        </div>
        <input
            type="file"
            name="filename"
            id="open"
            style="display: none"
            @change="changeFile"
          />
          <el-divider></el-divider>
        <div>
            作业内容：
            <el-main v-loading=upload>
                <div id="main">
                <mavon-editor ref=md :toolbars="markdownOption" @imgAdd="$imgAdd" v-model="hwContent">
                    <template slot="left-toolbar-after">
                    <button
                    type="button"
                    @click="$fileAdd"
                    class="op-icon el-icon-paperclip"
                    aria-hidden="true"
                    title="上传附件"
                    ></button>
                </template>
                </mavon-editor>
            </div>
            </el-main>
            
        </div>
        <div style="float: right;">
            <el-button @click="save">暂存</el-button>
            <el-button type="primary" @click="drawer = true">下一步</el-button>
        </div>
        <el-drawer
            title="设置分数与截止时间"
            :visible.sync="drawer"
            :direction="direction"
            :before-close="handleClose">
            <div style="margin-left: 20px;margin-right: 20px;">
                <span class="demonstration">设置分数</span>
                <el-input style="margin-top: 10px;" v-model="score"></el-input>
            </div>
            <el-divider></el-divider>
            <div style="margin-left: 20px;margin-right: 20px;">
                <div>
                    <span class="demonstration">设置截止时间</span>
                </div>
                
                <el-date-picker style="margin-top: 10px;"
                v-model="valueTime"
                type="datetime"
                placeholder="选择日期时间"
                default-time="00:00:00"
                value-format="yyyy-MM-dd-HH-mm-SS">
                </el-date-picker>
            </div>
            <div>
                <el-button style="margin-top: 10px;margin-left: 20px;" type="primary" @click="publish">发布作业</el-button>
            </div>
        </el-drawer>
        <div class="block">
  </div>
            </el-tab-pane>
            <el-tab-pane label="提交情况">
                <el-button @click="drawer_2=true">开启互评</el-button>
                <!-- <el-button @click="caculate()">计算分数</el-button> -->
                <el-table
                :data="stus"
                style="width: 100%"
                max-height="8 50"
                v-loading="loading"
                :default-sort = "{prop: 'evaScore', order: 'descending'}">
                <el-table-column
                    fixed
                    prop="uHead"
                    label=""
                    width="150">

                    <template slot-scope="scope">
                        <img :src="scope.row.uHead" width="40" height="40" class="head_pic"/>
                    </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="uID"
                    label="学生"
                    width="150">
                </el-table-column>
                <el-table-column
                    fixed
                    prop="uName"
                    label="学生"
                    width="150">
                </el-table-column>
                <el-table-column
                    fixed
                    prop="isSubmit"
                    label="是否提交"
                    width="250">
                    <template slot-scope="scope">
                    <span v-if="scope.row.isSubmit==true">已提交</span>
                    <span v-else>未提交</span>
                    </template>
                </el-table-column>
                <el-table-column
                    fixed
                    prop="evaScore"
                    label="分数"
                    width="250">
                </el-table-column>

                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                    <el-button
                    @click.native.prevent="startRow(scope.row)"
                        type="text"
                        size="small">
                        批改
                    </el-button>
                    <el-button
                    @click.native.prevent="startRow_2(scope.row)"
                        type="text"
                        size="small">
                        互评列表
                    </el-button>
                    </template>
                </el-table-column>
                </el-table>
            </el-tab-pane>
            <el-dialog title="互评详细" :visible.sync="dialogTableVisible">
            <el-table :data="gridData" v-loading="loadingDetail">
                <el-table-column property="eID" label="互评号" width="150"></el-table-column>
                <el-table-column property="name" label="姓名" width="200"></el-table-column>
                <el-table-column property="score" label="打分"></el-table-column>
                <el-table-column
                    fixed="right"
                    label="操作"
                    width="120">
                    <template slot-scope="scope">
                    <el-button
                    @click.native.prevent="startRow_3(scope.row)"
                        type="text"
                        size="small">
                        查看详细
                    </el-button>
                    </template>
                </el-table-column>
            </el-table>
            </el-dialog>
            <el-dialog width=1000 height="3000" title="详细" :visible.sync="dialogTableVisible_2">
                <div style="font-size: 20px;">
                    分数: {{ this.nscore }}
                </div>
                <div style="font-size: 20px;margin-top: 10px;">
                    点评内容: {{ this.ncomment }}
                </div>
                <div class="demo-image__preview" style="margin-top: 10px;">
                    <el-image  
                        style="width: 100%; height: 100%"
                        :src="nimg"
                        :preview-src-list="[nimg]">
                    </el-image>
                </div>
            </el-dialog>
            <el-drawer
                title="选择互评截止时间"
                :visible.sync="drawer_2"
                :direction="direction"
                :before-close="handleClose">
                <div style="margin-left: 20px;">
                    <div>
                        <span class="demonstration">设置截止时间</span>
                    </div>
                    <el-date-picker  style="margin-top: 10px;"
                    v-model="valueTime2"
                    type="datetime"
                    placeholder="选择日期时间"
                    default-time="00:00:00"
                    value-format="yyyy-MM-dd-HH-mm-SS">
                    </el-date-picker>
                </div>
                <div>
                    <el-button  style="margin-top: 10px;margin-right: 20px;float: right;" type="primary" @click="startEvaluation()">开始互评</el-button>
                </div>
            </el-drawer>
        </el-tabs>
        
        </el-main>
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
            drawer: false,
            drawer_2: false,
            direction: 'rtl',

            hwName:'',
            hwContent:'',

            isPreview:false,
            html:'',

            valueTime: '',
            valueTime2: '',

            upload:false,

            dialogTableVisible:false,
            dialogTableVisible_2:false,
            loadingDetail:false,


            classes:[],
            options:[],

            stus:[],

            name:'',

            nscore:0,
            ncomment:'',
            nimg : '',

            markdownOption:{
                bold: true, // 粗体
                italic: true, // 斜体
                header: true, // 标题
                underline: true, // 下划线
                strikethrough: true, // 中划线
                mark: true, // 标记
                superscript: true, // 上角标
                subscript: true, // 下角标
                quote: true, // 引用
                ol: true, // 有序列表
                ul: true, // 无序列表
                link: true, // 链接
                imagelink: true, // 图片链接
                code: true, // code
                table: true, // 表格
                // fullscreen: true, // 全屏编辑
                // readmodel: true, // 沉浸式阅读
                // htmlcode: true, // 展示html源码
                help: true, // 帮助
                /* 1.3.5 */
                // undo: true, // 上一步
                // redo: true, // 下一步
                // trash: true, // 清空
                // save: true, // 保存（触发events中的save事件）
                /* 1.4.2 */
                navigation: true, // 导航目录
                /* 2.1.8 */
                alignleft: true, // 左对齐
                aligncenter: true, // 居中
                alignright: true, // 右对齐
                /* 2.2.1 */
                // subfield: true, // 单双栏模式
                preview: true, // 预览
            },
            score:0,
        }
      },
      created(){
        var params = new URLSearchParams();
        params.append('hwID',this.$route.params.hwID);

        axios.post(restweburl+'getSavedHomework', params).then(response => {
            if(response.data.code==1){
            if(response.data.msg=='success'){
                if(response.data.data.name!=null){
                    this.hwName = response.data.data.name;
                }
                if(response.data.data.content!=null){
                    this.hwContent = response.data.data.content;
                }
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
        });

        var params1 = new URLSearchParams();
        params1.append('hwID',this.$route.params.hwID);

        axios.post(restweburl+'listStudentHomeworks', params1).then(response => {
            if(response.data.code==1){
            if(response.data.msg=='success'){
                this.stus=response.data.data;
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
      },
      methods:{
        caculate(){
            var params = new URLSearchParams();
              params.append('hwID',this.$route.params.hwID);
              axios.post(restweburl+'calculateScore',params).then(res => {
                console.log(res.data.data);
                var params1 = new URLSearchParams();
                params1.append('hwID',this.$route.params.hwID);

                axios.post(restweburl+'listStudentHomeworks', params1).then(response => {
                    if(response.data.code==1){
                    if(response.data.msg=='success'){
                        this.stus=response.data.data;
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
            }).catch(error => {
              alert(error)
              alert("请求失败")
            });

            
        },

        startEvaluation(){
            var params = new URLSearchParams();
              params.append('hwID',this.$route.params.hwID);
              params.append('ddl',this.valueTime2);
              axios.post(restweburl+'startEvaluation',params).then(res => {
                console.log(res.data.data);
                this.$message({
                    message: '开启互评成功',
                    type: 'success'
                  });
                  this.drawer_2=false;
            }).catch(error => {
              alert(error)
              alert("请求失败")
            });
        },

        startRow(row){
            let routeUrl = this.$router.resolve({
                    path: `/hw/evaluate/${this.$route.params.hwID}/${row.uID}`,
              });
              window.open(routeUrl .href, '_blank');
        },
        startRow_2(row){
            this.dialogTableVisible=true;
            this.loadingDetail=true;

            var params = new URLSearchParams();
              params.append('hwID',this.$route.params.hwID);
              params.append('sID',row.uID);
              axios.post(restweburl+'getEvaluatedMember',params).then(res => {
                this.loadingDetail=false;
                this.gridData=res.data.data;

            }).catch(error => {
              alert(error)
              alert("请求失败")
            });
        },
        startRow_3(row){
            
            this.dialogTableVisible_2=true;
            var params = new URLSearchParams();
              params.append('eID',row.eID);
              axios.post(restweburl+'getEvaluationResult',params).then(res => {
                this.nscore=res.data.data.score;
                this.ncomment=res.data.data.comment;
                this.nimg = res.data.data.image;
            }).catch(error => {
              alert(error)
              alert("请求失败")
            });
        },
        $imgAdd(pos, $file){

            this.upload=true;

            let $vm=this.$refs.md;
            // 第一步.将图片上传到服务器.
            var formdata = new FormData();
            formdata.append('file', $file);
            console.log($file);
            axios({
                url: restweburl+'uploadFile',
                method: 'post',
                data: formdata,
            }).then((url) => {
                this.$message({
                    message: '上传图片成功',
                    type: 'success'
                  });
                  this.upload=false;
                $vm.$img2Url(pos, url.data.data);
            })
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
                url: restweburl+'uploadFile',
                method: 'post',
                data: formdata,
                // headers: { 'Content-Type': 'multipart/form-data' },
            }).then((url) => {
                // 第二步.将返回的url替换到文本原位置![...](0) -> ![...](url)
                /**
                 * $vm 指为mavonEditor实例，可以通过如下两种方式获取
                 * 1. 通过引入对象获取: `import {mavonEditor} from ...` 等方式引入后，`$vm`为`mavonEditor`
                 * 2. 通过$refs获取: html声明ref : `<mavon-editor ref=md ></mavon-editor>，`$vm`为 `this.$refs.md`
                 */
                // $vm.$img2Url(url);
                this.$message({
                    message: '上传附件成功',
                    type: 'success'
                  });
                  
                if(this.hwContent==null)
                {
                    this.hwContent='['+fu.files[0].name+']'+'('+url.data.data+')';
                }
                else{
                    this.hwContent=this.hwContent+'['+fu.files[0].name+']'+'('+url.data.data+')';
                }
                this.upload=false;
                
            })
        },
        $fileAdd(){
            document.getElementById("open").click();
        },
        save(){
          var params = new URLSearchParams();
          params.append('hwID',this.$route.params.hwID);
          params.append('name',this.hwName);
          params.append('content',this.hwContent);

          axios.post(restweburl+'saveHomework', params).then(response => {
              if(response.data.code==1){
                if(response.data.msg=='success'){
                  this.$message({
                    message: '保存成功',
                    type: 'success'
                  });
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
        },
        publish(){
            var params = new URLSearchParams();
            params.append('hwID',this.$route.params.hwID);
            params.append('classes',[this.$route.params.classID]);
            params.append('name',this.hwName);
            params.append('content',this.hwContent);
            params.append('ddl',this.valueTime);
            params.append('score',this.score);

            axios.post(restweburl+'publishHomework', params).then(response => {
                if(response.data.code==1){
                    if(response.data.msg=='success'){
                    this.$message({
                        message: '发布成功',
                        type: 'success'
                    });
                    this.drawer=false;
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
        }
      }
      
    }
</script>
