<template>
    <div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">

    <el-form-item label="仓库名称" prop="id">
      <el-input v-model="ruleForm.id"></el-input>
    </el-form-item>
    <el-form-item label="简介" prop="desc">
      <el-input v-model="ruleForm.desc"></el-input>
    </el-form-item>
  <el-form-item>
    <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
    <el-button @click="resetForm('ruleForm')">重置</el-button>
  </el-form-item>
</el-form>
    </div>
</template>

<script>
import axios from 'axios'
import global from './GlobalPage.vue'
  const restweburl = global.ip;
    export default {
    data() {
      return {
        // userID:'1',
        // wareName:'1',
        id:'',
        desc:'',
        ruleForm: {
          id:'',
          desc:'',
        },
        rules: {
          id: [
            { required: true, message: '请填写仓库名称', trigger: 'blur' }
          ],
          desc: [
            { required: true, message: '请填写仓库简介', trigger: 'blur' }
          ]
        }
      };
    },
    methods: {
      submitForm(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            var params = new URLSearchParams();
                // params.append('sID',this.userID);
                params.append('wName',this.ruleForm.id);
                params.append('description',this.ruleForm.desc);

                axios.post(restweburl+'applyWare', params).then(response => {
                    if(response.data.code==1){
                        alert("提交成功");
                        this.$router.push('projectsBox')
                    }
                    else{
                        alert(response.data.msg);
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