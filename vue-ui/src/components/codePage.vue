<template>
    <div>
      <el-select ref ='myselected' v-model="value" @change="change(value)" placeholder="请选择分支">
    <el-option
      v-for="item in options"
      :key="item"
      :label="item"
      :value="item">
    </el-option>
  </el-select>
        <el-table
    :data="tableData"
    style="width: 100%"
    max-height="650"
    v-loading="loading">
    <el-table-column
      fixed
      prop="type"
      label="类型"
      width="50">
      <template slot-scope="scope">
        <!-- {{ scope.row.type }} -->
        <div v-if="scope.row.type=='dir'">
          <i class="el-icon-folder"></i>
        </div>
        <div v-else>
          <i class="el-icon-document"></i>
        </div>
      </template>
    </el-table-column>
    <el-table-column
      prop="fileName"
      label="名称"
      width="220">
    </el-table-column>
    <el-table-column
      prop="description"
      label="提交注释"
      width="300">
    </el-table-column>
    <el-table-column
      prop="date"
      label="修改时间"
      width="200">
    </el-table-column>
    <el-table-column
      fixed="right"
      label="操作"
      width="220">
      <template slot-scope="scope">
        <el-button
        @click.native.prevent="startRow(scope.$index, scope.row)"
          type="text"
          size="small">
          打开
        </el-button>
      </template>
    </el-table-column>
  </el-table>
    </div>
</template>
<style>
    .el-table{
        text-align:center
    }
    .el-table-column.label{
        text-align:center
    }
</style>

<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  export default {
    created() {
      axios.get(restweburl + "getBranch", {params:{'wName':this.$parent.$route.params.id}})
        .then((res) => {
          console.log(res.data.data);
          this.options=res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        this.loading=true;
        axios.get(restweburl + "getFiles", {params:{'wName':this.$parent.$route.params.id,'path':'','branch':'master'}})
        .then((res) => {
          this.tableData=res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    computed(){
      
    },
    methods: {
      change(index){
        // alert(index);
        this.loading=true;
        axios.get(restweburl + "getFiles", {params:{'wName':this.$parent.$route.params.id,'path':'','branch':index}})
        .then((res) => {
          this.tableData=res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
      },
      startRow(index, rows) {
        this.loading=true;
        console.log(rows);
        axios.get(restweburl + "getFiles", {params:{'wName':this.$parent.$route.params.id,'path':rows.path+rows.fileName,'branch':'master'}})
        .then((res) => {
          this.tableData=res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
      },
    },
    data() {
      return {
        value:'',
        loading:true,
        now:'',
        tableData: [
          {
          type:'<i class="el-icon-folder"></i>',
          name:'image',
          description:'原神？启动！',
          date:'last month'
        },
          ],
          options: ['原神？启动！','Genshin Impact'],
      }
    }
  }
</script>