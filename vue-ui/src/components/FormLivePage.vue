<template>
    <div>
      <el-main>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
  <el-form-item label="会议名称" prop="wName">
    <el-input type="input" v-model="ruleForm.wName"></el-input>
  </el-form-item>
  <el-form-item label="活动时间" required>
    <el-col :span="11">
      <el-form-item prop="startTime">
        <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.startTime" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>
      </el-form-item>
    </el-col>
    <el-col class="line" :span="2">-</el-col>
    <el-col :span="11">
      <el-form-item prop="date2">
        <el-date-picker placeholder="选择时间"  v-model="ruleForm.startTime2" value-format="yyyy-MM-dd" style="width: 100%;"></el-date-picker>
      </el-form-item>
    </el-col>
  </el-form-item>
  <el-form-item label="即时配送" prop="delivery">
    <el-switch v-model="ruleForm.delivery"></el-switch>
  </el-form-item>
  <el-form-item label="原因" prop="content">
    <el-input type="textarea" v-model="ruleForm.content"></el-input>
  </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
</el-form>
</el-main>
    </div>
</template>

<script>
import axios from 'axios'
import global from './GlobalPage.vue'
  const restweburl = global.ip;
    export default {
    data() {
      return {
        ruleForm: {
          wName:'',
          startTime:'',
          content:'',
        },
        rules: {
          wName: [
            { required: true, message: '请填写操作对象账号', trigger: 'blur' }
          ],
          startTime: [
            { required: true, message: '请填写审批性质', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '请填写会议的原因和讨论内容', trigger: 'blur' }
          ],
          name: [
            { required: true, message: '请填写会议名称', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var params = new URLSearchParams();
                // params.append('wName',this.ruleForm.wName);
                params.append('wName',this.$parent.$route.params.id);
                params.append('content',this.ruleForm.content);
                params.append('startTime',this.ruleForm.startTime);
                axios.post(restweburl+'/applyLive', params).then(response => {
                    if(response.data.code==1){
                        alert("提交成功");
                        this.$router.push('./intro');
                    }
                    else{
                        alert("异常");
                    }
                }).catch(error => {
                    console.log(error)
                    alert("请求失败")
                })
          } else {
            console.log('error submit!!');
            return false;
          }
        });
      },
      resetForm(formName) {
        this.$refs[formName].resetFields();
      }
    }
  }
</script>