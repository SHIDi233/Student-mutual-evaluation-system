
<template>
    <div>
        <el-form ref="form" :model="form"  label-width="100px">

        <!-- <el-form-item label="真实姓名">
        <el-input v-model="form.name" width="500px"></el-input>
        </el-form-item> -->
        <el-form-item label="头像">
          <el-image
            style="width: 178px; height: 178px"
            lazy
            v-if="header_url"
            :src="header_url"
            :fit="fit">
          </el-image>
          <el-upload
            class="avatar-uploader"
            :action="uploadurl"
            :show-file-list="false"
            :headers="{'token':this.token}"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload">
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </el-form-item>
        
        <el-form-item label="学校">
        <el-select v-model="form.school" placeholder="请选择" :disabled="isWrite_2">
            <el-option label="北京交通大学" value="北京交通大学" ></el-option>
            <el-option label="其他学校" value="other"></el-option>
        </el-select>
        </el-form-item>

        <el-form-item label="学号">
        <el-input v-model="form.studentID" :disabled="isWrite"></el-input>
        </el-form-item>

        <el-form-item label="专业">
        <el-input v-model="form.majority" :disabled="isWrite_3"></el-input>
        </el-form-item>

        <el-form-item label="个人介绍">
        <el-input v-model="form.introduction"></el-input>
        </el-form-item>
        <el-form-item>
        <el-button type="primary" @click="onSubmit">提交修改</el-button>
        </el-form-item>
        </el-form>
    </div>
</template>

<style>
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>

<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
    export default {
      data() {
        return {
          header_url:"",

          uploadurl: restweburl+'uploadHeader',

          form: {
            name: '',
            school: '',
            majority: '',
            introduction: '',
            studentID:'',
            
            
          },
          isWrite:false,

          token: localStorage.getItem("CCNtoken"),
        }
      },
      created() {
        axios.post(restweburl + "header")
        .then((res) => {
            this.header_url = res.data.data;
        })
        .catch(function (error) {
            console.log(error);
        });
        axios.post(restweburl + "info")
        .then((res) => {
            this.form.name = res.data.data.name;
            this.form.introduction=res.data.data.introduction;
            this.form.school=res.data.data.school;
            this.form.majority=res.data.data.majority;
            this.form.studentID=res.data.data.studentID;
            if(this.form.studentID!=null){
              this.form.studentID=this.form.studentID+'(已认证)';
              this.isWrite=true;
              this.isWrite_2=true;
              this.isWrite_3=true;
            }
        })
        .catch(function (error) {
            console.log(error);
        });
      },
      methods: {
        onSubmit() {
          console.log('submit!');
          var params = new URLSearchParams();
          params.append('school',this.form.school);
          params.append('majority',this.form.majority);
          params.append('introduction',this.form.introduction);
          params.append('studentID',this.form.studentID);

          axios.post(restweburl+'modify', params).then(response => {
              console.log(response.data)
              if(response.data.code==1){
                if(response.data.msg=='success'){
                  this.$message({
                    message: '修改成功',
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
        handleAvatarSuccess(res, file) {
          this.imageUrl = URL.createObjectURL(file.raw);
          // let FormDatas = new FormData();
          // FormDatas.append('file',this.imageUrl);

          // axios.post({method: 'post',
          //     url: 'uploadHeader',
          //     timeout: 30000,
          //     data: FormDatas
          //   }).then(res=>{alert(res)})
        },
        beforeAvatarUpload(file) {
          // const isJPG = file.type === 'image/jpeg';
          const isLt2M = file.size / 1024 / 1024 < 12;

          // if (!isJPG) {
          //   this.$message.error('上传头像图片只能是 JPG 格式!');
          // }
          if (!isLt2M) {
            this.$message.error('上传头像图片大小不能超过 12MB!');
          }
          return isLt2M;
        },
      }
    }
  </script>