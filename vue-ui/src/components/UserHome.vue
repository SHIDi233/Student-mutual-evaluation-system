3<template>
    <div>
        <el-container>
            <el-aside width="300px">
                <div>
                    <el-avatar :size="160" :src="circleUrl"></el-avatar>
                </div>
                <div>
                    <el-descriptions :title="title" :colon="false" :column="1">
                        <el-descriptions-item :label="school" ></el-descriptions-item>
                        <el-descriptions-item :label="label" ></el-descriptions-item>
                    </el-descriptions>
                </div>
            </el-aside>
            <el-main  v-loading="loading">
                <div>
                    <el-button-group>
                        <el-button type="primary" @click="isEdit=true ">编辑</el-button>
                        <el-button type="primary" @click="isEdit=false ">预览</el-button>
                        <el-button type="primary" @click="upload">保存</el-button>
                    </el-button-group>
                    
                </div>
                    <!-- <div class="context" v-html="compiledMarkdown"></div> -->
                <div v-if="isEdit">
                    <mavon-editor ref=md @imgAdd="$imgAdd" v-model="markdown"/>
                </div>
                <div v-else v-html="html">
                </div>
            </el-main>
        </el-container>
        
       
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
            isEdit:false,

            title:"CCN_default_name",
            label:"",
            circleUrl:"",
            context:"111",
            school:"",
            loading: true,

            markdown: "# loading...",
            html: "",

            flag:"",
        }
      },
    //   components: {
    //     SimpleMDE
    //   },
    methods:{
        $imgAdd(pos, $file){
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
                $vm.$img2Url(pos, url.data.data);
            })
        },
        upload(){
            var params = new URLSearchParams();
            params.append('readme',this.markdown);
            axios.post(restweburl + "uploadReadme",params)
            .then((res) => {
                if(res.data.msg=='success'){
                        this.$message({
                        message: '修改成功',
                        type: 'success'
                        });
                }
                else{
                    this.$message.error('修改失败');
                }

            })
            .catch(function (error) {
                console.log(error);
            });
        }
    },
      created() {
        axios.post(restweburl + "header")
        .then((res) => {
            this.circleUrl = res.data.data;
        })
        .catch(function (error) {
            console.log(error);
        });
        axios.post(restweburl + "info")
        .then((res) => {
            this.title = res.data.data.name;
            this.label = res.data.data.introduction;
            this.context=res.data.data.readme;
            this.school=res.data.data.school+'/'+res.data.data.majority;
        })
        .catch(function (error) {
            console.log(error);
        });
      },
      computed: {
        compiledMarkdown() {
            return marked.parse(this.context);
        },
      },
      watch: {
        compiledMarkdown() {
            this.loading=false;
            this.markdown = this.context;
            this.html = marked.parse(this.context);
        },
        markdown(value) {
            this.html = marked(value);
        }
      }
    }

</script>
