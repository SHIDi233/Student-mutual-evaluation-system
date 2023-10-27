<template>
    <div>
      <el-main>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <el-form-item label="变动性质" prop="pattern">
      <el-select v-model="ruleForm.pattern" placeholder="请选择">
        <el-option label="添加成员" value="inp"></el-option>
        <el-option label="移出成员" value="del"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="变动账号" prop="id">

      <el-input v-model="ruleForm.id"></el-input>
    </el-form-item>

  <el-form-item label="原因" prop="desc">
    <el-input type="textarea" v-model="ruleForm.desc"></el-input>
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
        userID:'1',
        wareName:'1',
        ruleForm: {
          pattern:'',
          id:'',
          desc: ''
        },
        rules: {
          id: [
            { required: true, message: '请填写操作对象账号', trigger: 'blur' }
          ],
          pattern: [
            { required: true, message: '请填写审批性质', trigger: 'blur' }
          ],
          desc: [
            { required: true, message: '请填写进行该操作的原因，如成员加入或移出的原因', trigger: 'blur' }
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
                // params.append('wName',this.wareName);
                params.append('wName',this.$route.params.id);
                params.append('mail',this.ruleForm.id);
                params.append('content',this.ruleForm.desc);

                axios.post(restweburl+'invite', params).then(response => {
                    if(response.data.code==1){
                        alert("提交成功");
                        this.$router.push('intro')
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