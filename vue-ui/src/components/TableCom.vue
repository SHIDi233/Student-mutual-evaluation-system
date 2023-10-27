<template>
  <div>
    <el-button @click="addBtn()" type="1" fixed="right">新建仓库</el-button>
    <el-button @click="msgBtn()" type="1" fixed="right">消息</el-button>
  <el-table
    :data="tableData"
    style="width: 100%"
    max-height="8 50">
    <el-table-column
      fixed
      prop="ctime"
      label="日期"
      width="150">
    </el-table-column>
    <el-table-column
      prop="wname"
      label="项目名称"
      width="220">
    </el-table-column>
    <el-table-column
      prop="adminID"
      label="负责人"
      width="220">
    </el-table-column>
    <el-table-column
      prop="describe"
      label="简介"
      width="220">
    </el-table-column>
    <el-table-column
      label="操作"
      width="80">
      <template slot-scope="scope">
        <el-button
        @click.native.prevent="startRow(scope.$index, scope.row,scope.$ElTableColumn)"
          type="text"
          size="small">
          进入
        </el-button>
        
      </template>
    </el-table-column>
  </el-table>
</div>
</template>

<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  // const restweburl = "http://192.168.10.167:8888/";
  export default {
    created() {
      axios.post(restweburl + "Wares", {})
        .then((res) => {
          this.tableData = res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });
  },
    methods: {
      startRow(index, rows) {
        // // rows.splice(index, 1);
        // // alert(rows.data);
        console.log(rows);
        // alert(rows.wname)
        this.$router.push(`/home/projectPage/${rows.wname}/intro`)
      },
      addBtn() {
        // console.log(rows);
        // alert(rows.wname)
        this.$router.push(`/home/apply`)
      },
      msgBtn() {
        // console.log(rows);
        // alert(rows.wname)
        this.$router.push(`/home/allmsg`)
      }
    },
    data() {
      return {
        tableData: [
        ]
      }
    }
  }
</script>