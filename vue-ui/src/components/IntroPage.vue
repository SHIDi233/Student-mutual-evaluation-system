<template>
    <div>
      <div class="context" v-html="compiledMarkdown"></div>
    </div>
</template>

<script>
  import axios from 'axios'
  import global from './GlobalPage.vue'
  const restweburl = global.ip;
  // const restweburl = "http://192.168.10.167:8888/";
  import { marked } from "marked";
  export default {
    methods: {
    },
    created() {
      axios.get(restweburl + "readme", {params:{"wName":this.$parent.$route.params.id}})
        .then((res) => {
          this.articleDetail.context=res.data;
        })
        .catch(function (error) {
          console.log(error);
        });
        
    },
    computed: {
      compiledMarkdown() {
        return marked.parse(this.articleDetail.context);
    },
  },
    data() {
      return {
        articleDetail: {
        context:
          '网络连接中。。。',
      }, 
      }
    }
  }
</script>