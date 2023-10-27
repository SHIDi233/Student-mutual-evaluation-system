<template>
    <div>
        <el-main>
            <el-descriptions title="仓库信息">
                <el-descriptions-item labelStyle="width: 150px" contentStyle="width: 250px">
                <template slot="label"><i class="el-icon-s-home"></i>仓库名</template>
                    {{this.$parent.$route.params.id}}
                </el-descriptions-item>
                <el-descriptions-item labelStyle="width: 150px" contentStyle="width: 250px">
                <template slot="label"><i class="el-icon-upload"></i>SSH url</template>
                    {{this.ap}}
                </el-descriptions-item>
            </el-descriptions>
        </el-main>

        <el-main>
            <el-table
    :data="tableData"
    style="width: 50%"
    max-height="8 50"
    v-loading="loading">
    <el-table-column
      fixed
      prop="isAdmin"
      label="身份"
      width="150">
      <template slot-scope="scope">
        <!-- {{ scope.row.type }} -->
        <div v-if="scope.row.isAdmin=='true'">
          管理员
        </div>
        <div v-else>
          成员
        </div>
      </template>
    </el-table-column>
    <el-table-column
      fixed
      prop="name"
      label="成员名"
      width="250">
    </el-table-column>
    <el-table-column
      fixed
      prop="mail"
      label="账号"
      width="210">
    </el-table-column>
    </el-table>
        </el-main>
    </div>
</template>

<script>
import axios from 'axios'
import global from './GlobalPage.vue'
const restweburl = global.ip;

export default {
    data(){
        return {
            ap:'...',
            loading:true,
            tableData: [
            ]
        };
    },
    created(){
        var params = new URLSearchParams();
        params.append('wName',this.$parent.$route.params.id);
        var c =this.$parent.$route.params.id;
        axios.get(restweburl + "getUrl", {params:{'wName':c}})
        .then((res) => {
            this.ap=res.data.data;
        })
        .catch(function (error) {
          console.log(error);
        });


        axios.get(restweburl + "getMembers", {params:{'wName':c}})
        .then((res) => {
          this.tableData = res.data.data;
          this.loading=false;
        })
        .catch(function (error) {
          console.log(error);
        });
    },
    computed(){
        
    }
}

</script>
