<template>
    <div>
        <!-- <el-aside>
        <div class="block">
    <el-carousel trigger="click" width='300px' height="150px">
      <el-carousel-item v-for="item in 4" :key="item">
        <h3 class="small">{{ item }}</h3>
      </el-carousel-item>
    </el-carousel>
    </div>
    </el-aside> -->
    <el-main>
        <TableCom/>
    </el-main>
    </div>
</template>

<script>
import TableCom from "../components/TableCom.vue";
    export default {
        components:{
            TableCom
        }
    }
</script>