<template>
    <div>
        <div>
            <el-button v-if="viewInfo.isDraw==false" @click="viewInfo.isDraw=true,$refs.md.blur(),$refs.md2.focus(),initCanvasSize();">画笔:关</el-button>
            <el-button v-if="viewInfo.isDraw==true" @click="viewInfo.isDraw=false">画笔:开</el-button>
            
            <el-button v-if="$route.params.uID==null" style="float: right;" @click="nextHw()">下一份</el-button>
            <el-button style="float: right;" :disabled="inputScore==''" v-loading="upload" type="primary" @click="commit()">提交评分</el-button>
            <el-input v-model="inputScore" placeholder="分数" style="width: 100px;float: right;"></el-input>
            
        </div>
        <div v-if="viewInfo.isDraw==true">
            <span class="demonstration">粗细</span>
            <el-slider style="width: 100px" v-model="viewInfo.cx" :min="min" :max=max></el-slider>
        </div>
        <div ref="cap">
            <div ref="md"  style="position:absolute;">
                <div v-html="html"></div>
            </div>
            <div ref="md2" style="position:absolute; top:y; left: x;">
                <canvas style="position:absolute; top:0px; left: 0px;" ref="canvas"  class="main-canvas" id="main-canvas"></canvas>
            </div>
        </div>
        
        <div>
            <el-input v-model="input" placeholder="请输入批注内容" style="position:fixed;bottom:0;"></el-input>
        </div>
    </div>
    
</template>

<script>
    import { marked } from "marked";
    import hljs from "highlight.js";

    import axios from 'axios'
    import global from './GlobalPage.vue'
import html2canvas from 'html2canvas';
    const restweburl = global.ip;

    export default {
        data() {
            return{
                eID:0,


                input:'',
                inputScore:'',
                min:1,
                max:8,
                homework:{
                    data:'# 111',
                    // html:'',
                },
                evaluation:{    
                    points:[],
                },
                position:{
                    x:1,
                    y:1,
                    y2:1,
                },
                viewInfo:{
                    isDraw:false,
                    isDrawing:false,
                    cx:4,
                },

                canvas: null,
                ctx: null,

                image:'',
                upload:false,
            }
        },
        mounted() {
            this.homework.data="## loading..."
            this.x=this.$refs.md.getBoundingClientRect().x;
            this.y=this.$refs.md.getBoundingClientRect().y;
            

            this.canvas = this.$refs.canvas;  
            this.ctx = this.canvas.getContext('2d');

            this.canvas.addEventListener('mousedown', this.handleMouseDown);
            this.canvas.addEventListener('mouseup', this.handleMouseUp);
            this.canvas.addEventListener('mousemove', this.handleMouseMove);

            this.initCanvasSize();
        },
        created(){
            if(this.$route.params.uID==null){
                var params = new URLSearchParams();
                params.append('hwID',this.$route.params.hwID);
                axios.post(restweburl+'getEvaluation',params).then(response => {
                if(response.data.code==1){
                    if(response.data.msg=='success'){
                        if(response.data.data.content!=null){
                            this.eID = response.data.data.eID;
                            this.homework.data = response.data.data.content;
                        }
                    }
                    else{
                        this.$message.error(response.data.msg);
                    }
                }
                else{
                    this.$message.error(response.data.msg);
                }
                }).catch(error => {
                    alert(error)
                    alert("请求失败")
                })
            }
            else{
                var params1 = new URLSearchParams();
                params1.append('hwID',this.$route.params.hwID);
                params1.append('sID',this.$route.params.uID);

                axios.post(restweburl+'getStudentHomework',params1).then(response => {
                if(response.data.code==1){
                    if(response.data.msg=='success'){
                        // if(response.data.data.name!=null){
                        //     this.hwName = response.data.data.name;
                        // }
                        // if(response.data.data.homework!=null){
                        //     this.hwContent = response.data.data.homework;
                        // }
                        if(response.data.data.content!=null){
                            this.homework.data = response.data.data.content;
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
            }
            
        },  
        computed: {
            html(){
                var rendererMD = new marked.Renderer();
                marked.setOptions({
                renderer: rendererMD,
                highlight: function(code) {
                    return hljs.highlightAuto(code).value;
                },
                pedantic: false,
                gfm: true,
                tables: true,
                breaks: true,
                sanitize: true,
                smartLists: true,
                smartypants: true,
                xhtml: true
                });
                
                return marked.parse(this.homework.data);
            }
        },
        methods:{
            changeToBlob(dataURL) {
            var arr = dataURL.split(","),
            type = arr[0].match(/:(.*?);/)[1],//获取MIME 类型，即image/png
            bstr = atob(arr[1]),
            count = bstr.length,
            u8arr = new Uint8Array(count);
            while (count--) {
            u8arr[count] = bstr.charCodeAt(count);
            }
            return new Blob([u8arr], {
            type: type,
            });
        },
        base64ImgtoFile(dataurl, filename = 'file') {
                let arr = dataurl.split(',')
                let mime = arr[0].match(/:(.*?);/)[1]
                let suffix = mime.split('/')[1]
                let bstr = atob(arr[1])
                let n = bstr.length
                let u8arr = new Uint8Array(n)
                while (n--) {
                    u8arr[n] = bstr.charCodeAt(n)
                }
                return new File([u8arr], `${filename}.${suffix}`, {
                    type: mime
                })
            },

            //截图
            capture(){
                html2canvas(this.$refs.cap,this.canvas,{height: this.canvas.scrollHeight,width: this.canvas.scrollWidth,}).then((canvas)=>
                {
                    let dataURL = canvas.toDataURL("image/png");
                    let file=this.base64ImgtoFile(dataURL)
                    // var blob = this.changeToBlob(dataURL);//获取blob对象

                    var formdata = new FormData();
                    formdata.append('file', file);
                    console.log(file);
                    axios({
                        url: restweburl+'uploadFile',
                        method: 'post',
                        data: formdata,
                    }).then((url) => {
                        this.img = url.data.data;

                        var params1 = new URLSearchParams();
                        params1.append('hwID',this.$route.params.hwID);
                        params1.append('eID',this.eID);
                        params1.append('score',this.inputScore);
                        params1.append('image',this.img);

                        axios.post(restweburl+'evaluate',params1).then(response => {
                        if(response.data.code==1){
                            if(response.data.msg=='success'){
                                this.$message({
                                    message: '提交成功',
                                    type: 'success'
                                });
                                this.upload=false;
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
                    })
                    // var url = URL.createObjectURL(blob);
                    // window.open(url);//创建一个新的浏览器窗口对象, 参数指定了该窗口将会打开的地址
                })
            },


            //初始化Canvas
            initCanvasSize(){
                // this.canvas.width = this.$refs.md.getBoundingClientRect().width;
                // this.canvas.height = this.$refs.md.getBoundingClientRect().height;
                this.canvas.width = window.innerWidth;
                this.canvas.height = window.innerHeight;
                this.y2=this.$refs.md.getBoundingClientRect().y+this.$refs.md.clientHeight;
                // this.draw();
            },
        
            //刷新渲染
            draw(){
                // 清除画布
                this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
                

                for (let i = 1; i < this.evaluation.points.length; i++) {

                    const point1 = this.evaluation.points[i - 1];
                    const point2 = this.evaluation.points[i];

                    this.ctx.strokeStyle = point1.color; // 设置线的颜色为蓝色
                    this.ctx.lineWidth = point1.cx; // 设置线的宽度为2
                    
                    if(point1.x===-10000 || point2.x===-10000){
                        continue;
                    }
                    
                    this.ctx.beginPath();
                    this.ctx.moveTo(point1.x, point1.y);
                    this.ctx.lineTo(point2.x, point2.y);
                    this.ctx.stroke();
                }
            },
            handleMouseDown(event) {
                const {
                    offsetX,
                    offsetY
                } = event;
                offsetX,
                offsetY

                this.initCanvasSize();

                if(this.viewInfo.isDraw){
                    this.viewInfo.isDrawing=true;
                    // this.evaluation.points.push({x:offsetX,y:offsetY});
                    
                }
                this.draw();

                
            },
            handleMouseMove(event) {
                const {
                    offsetX,
                    offsetY
                } = event;
                if(this.viewInfo.isDrawing){
                    this.evaluation.points.push({x:offsetX,y:offsetY,color:'red',cx:this.viewInfo.cx});
                    this.draw();
                }
            },
            handleMouseUp() {
                this.viewInfo.isDrawing=false;
                this.evaluation.points.push({x:-10000,y:-10000});
            },
            nextHw(){

                this.evaluation.points=[];
                this.draw();

                var params1 = new URLSearchParams();
                params1.append('hwID',this.$route.params.hwID);

                axios.post(restweburl+'getEvaluation',params1).then(response => {
                if(response.data.code==1){
                    if(response.data.msg=='success'){
                        this.eID=response.data.data.eID;
                        if(response.data.data.content!=null){
                            this.homework.data = response.data.data.content;
                        }
                    }
                    else{
                        this.$message.error(response.data.msg);
                    }
                }
                else{
                    this.$message.error(response.data.msg);
                }
                }).catch(error => {
                    alert(error)
                    alert("请求失败")
                })
            },
            commit(){
                this.upload=true;
                // var params1 = new URLSearchParams();
                // params1.append('hwID',this.$route.params.hwID);
                // params1.append('eID',this.eID);
                // params1.append('score',this.inputScore);


                this.capture();

                // params1.append('img',this.image);

                // axios.post(restweburl+'evaluate',params1).then(response => {
                // if(response.data.code==1){
                //     if(response.data.msg=='success'){
                //         this.$message({
                //             message: '提交成功',
                //             type: 'success'
                //         });
                //         this.upload=false;
                //     }
                //     else{
                //         this.$message.error(response.data.msg);
                //     }
                // }
                // else{
                //     alert("网络错误")
                // }
                // }).catch(error => {
                //     alert(error)
                //     alert("请求失败")
                // })
            }
        }

    }
</script>
