<template>
    <div>
        <el-header>
            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
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
        <div class="demo-input-suffix" style="font-size: 30px;" >
            作业名：{{ this.hwName }}
            <!-- <el-input v-model="hwName" placeholder="作业名"></el-input> -->
        </div>
        <!-- <div>{{  }}</div>
        <div>
            <el-button>

            </el-button> 
        </div> -->
        <el-divider></el-divider>   
        <div>
            <!-- 作业内容： -->
            <div v-html="compiledMarkdown">
            </div>
        </div>
        <el-divider></el-divider>   
        <div style="font-size: 30px;">
            编写作业：
        </div>
        <div> 
            <div v-html="compiledMarkdown_2"></div>
            <!-- <mavon-editor ref=md :toolbars="markdownOption" @imgAdd="$imgAdd" v-model="hwWrite">
                <template slot="left-toolbar-after">
                    <button
                    type="button"
                    @click="$fileAdd"
                    class="op-icon el-icon-paperclip"
                    aria-hidden="true"
                    title="上传附件"
                    ></button>
                </template>
            </mavon-editor> -->
        </div>
        <div>
            <!-- <el-button @click="save">暂存</el-button>
            <el-button type="primary" @click="submit">提交</el-button> -->
        </div>

        <div class="block">
  </div>
    </div>
</template>

<script>
    import axios from 'axios'
    import global from './GlobalPage.vue'
    const restweburl = global.ip;

    import { marked } from "marked";

    export default {
        
      data() {
        return {
            hwName:'',
            hwContent:'loading...',
            hwWrite:" ",
            
            markdown: "# loading...",
            html: "",

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
            name:''
        }
      },
      created(){
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



        var params = new URLSearchParams();
        params.append('hwID',this.$route.params.hwID);

        axios.post(restweburl+'getSavedHomework-stu',params).then(response => {
            if(response.data.code==1){
            if(response.data.msg=='success'){
                this.hwName = response.data.data.name;
                this.hwContent = response.data.data.homework;
                this.hwWrite = response.data.data.content;
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

      computed: {
        compiledMarkdown() {
            return marked.parse(this.hwContent);
        },
        compiledMarkdown_2() {
            return marked.parse(this.hwWrite);
        },
      },
    }
</script>
