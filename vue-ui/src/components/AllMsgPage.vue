<template>
    <div>
      <el-main>
        <el-table
    :data="tableData"
    style="width: 100%"
    max-height="8 50"
    v-loading="loading">
    <el-table-column
      fixed
      prop="type"
      label="类型"
      width="150">
    </el-table-column>
    <el-table-column
      fixed
      prop="sendTime"
      label="日期"
      width="250">
    </el-table-column>
    <el-table-column
      fixed
      prop="id"
      label="单号"
      width="170">
    </el-table-column>
    <el-table-column
      prop="senderName"
      label="发送人"
      width="100">
    </el-table-column>
    <el-table-column
      prop="content"
      label="简介"
      width="620">
    </el-table-column>
    <el-table-column

      label="操作"
      width="120">
      <template slot-scope="scope">
        <div v-if="scope.row.type!='NT'">
          <el-button
        @click.native.prevent="deleteRow(scope.$index, scope.row)"
          type="text"
          size="small">
          查看
        </el-button>
        </div>
      </template>
    </el-table-column>
  </el-table>
  </el-main>
    </div>
</template>

<script>

  // const restweburl = "http://192.168.10.167:8888/";
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  export default {
    
    methods: {
      deleteRow(index, rows) {
        console.log(rows);
        this.$router.push(`approval/${rows.type}/${rows.id}/${rows.sendTime}/${rows.senderName}/${rows.content}`)
      }
    },
    data() {
      return {
        loading:true,
        tableData: [
        ]
      }
    },
    created() {
      var params = new URLSearchParams();
      params.append('wName',this.$parent.$route.params.id);
      params.append('grant','-1');
      var c =this.$parent.$route.params.id;
      axios.get(restweburl + "Notations", {params:{'wName':c}})
        .then((res) => {
          this.tableData = res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
  },
  }
</script>