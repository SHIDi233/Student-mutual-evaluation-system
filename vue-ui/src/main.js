import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router';
import router from './router/index'
import axios from 'axios';
import ElementUI from 'element-ui'
import leMarkdownEditor from 'le-markdown-editor'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(ElementUI)
Vue.use(VueRouter);
Vue.use(leMarkdownEditor)


// axios.defaults.baseURL = 'E:\\ST\\html\\vue\\gitccn\\dist';
// 添加请求拦截器
axios.interceptors.request.use(function (config) {
  // 在发送请求之前做些什么
  // 判断是否存在token,如果存在将每个页面header添加token
  // config.headers['token']="eyJhbGciOiJIUzI1NiJ9.eyJtYWlsIjoiMjk4MjQzNzEzOUBxcS5jb20iLCJJRCI6MSwiZXhwIjoxNjg2NzYyMDYzfQ.RusKVZEKZEuMKVJPQc20iQZs-_3towb6UqdIa1g46do";
  if (localStorage.getItem("CCNtoken")) {
    config.headers['token']= localStorage.getItem("CCNtoken");
  }
  return config
}, function (error) {
  router.push('/login')
  return Promise.reject(error)
});

new Vue({
  
  render: h => h(App),
  router,
  axios
  
}).$mount('#app')
