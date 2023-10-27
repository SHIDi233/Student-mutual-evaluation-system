<template>
    <div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
    <!-- <el-form-item label="成员变动审批单" prop="name">
      <el-input v-model="ruleForm.name"></el-input>
    </el-form-item> -->
    <el-form-item label="变动性质" prop="pattern">
      <el-select v-model="ruleForm.pattern" placeholder="请选择">
        <el-option label="添加成员" value="inp"></el-option>
        <el-option label="移出成员" value="del"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="变动账号" prop="id">
      <!-- <el-select v-model="ruleForm.region" placeholder="请选择">
        <el-option label="添加成员" value="inp"></el-option>
        <el-option label="移出成员" value="del"></el-option>
      </el-select> -->
      <el-input v-model="ruleForm.id"></el-input>
    </el-form-item>
  <!-- <el-form-item label="活动时间" required>
    <el-col :span="11">
      <el-form-item prop="date1">
        <el-date-picker type="date" placeholder="选择日期" v-model="ruleForm.date1" style="width: 100%;"></el-date-picker>
      </el-form-item>
    </el-col>
    <el-col class="line" :span="2">-</el-col>
    <el-col :span="11">
      <el-form-item prop="date2">
        <el-time-picker placeholder="选择时间" v-model="ruleForm.date2" style="width: 100%;"></el-time-picker>
      </el-form-item>
    </el-col>
  </el-form-item>
  <el-form-item label="即时配送" prop="delivery">
    <el-switch v-model="ruleForm.delivery"></el-switch>
  </el-form-item> -->
  <!-- <el-form-item label="活动性质" prop="type">
    <el-checkbox-group v-model="ruleForm.type">
      <el-checkbox label="美食/餐厅线上活动" name="type"></el-checkbox>
      <el-checkbox label="地推活动" name="type"></el-checkbox>
      <el-checkbox label="线下主题活动" name="type"></el-checkbox>
      <el-checkbox label="单纯品牌曝光" name="type"></el-checkbox>
    </el-checkbox-group>
  </el-form-item>
  <el-form-item label="特殊资源" prop="resource">
    <el-radio-group v-model="ruleForm.resource">
      <el-radio label="线上品牌商赞助"></el-radio>
      <el-radio label="线下场地免费"></el-radio>
    </el-radio-group>
  </el-form-item> -->
  <el-form-item label="原因" prop="desc">
    <el-input type="textarea" v-model="ruleForm.desc"></el-input>
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
        ruleForm: {
          // name: '',
          // region: '',
          // date1: '',
          // date2: '',
          // delivery: false,
          // type: [],
          // resource: '',
          pattern:'',
          id:'',
          desc: ''
        },
        rules: {
          // name: [
          //   { required: true, message: '请输入活动名称', trigger: 'blur' },
          //   { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
          // ],
          // region: [
          //   { required: true, message: '请选择活动区域', trigger: 'change' }
          // ],
          // date1: [
          //   { type: 'date', required: true, message: '请选择日期', trigger: 'change' }
          // ],
          // date2: [
          //   { type: 'date', required: true, message: '请选择时间', trigger: 'change' }
          // ],
          // type: [
          //   { type: 'array', required: true, message: '请至少选择一个活动性质', trigger: 'change' }
          // ],
          // resource: [
          //   { required: true, message: '请选择活动资源', trigger: 'change' }
          // ],
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
                params.append('id',this.mail);
                params.append('name',this.name);
                params.append('password',this.password);

                axios.post(restweburl+'/', params).then(response => {
                    console.log(response.data)
                    var obj = JSON.parse(response.data);
                    if(obj.code==1){
                        alert("提交成功");
                        this.$router.push('intro')
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