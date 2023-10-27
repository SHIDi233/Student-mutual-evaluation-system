
import Vue from "vue";
import VueRouter from 'vue-router'
import login from "../components/LoginPage.vue";
import register from "../components/RegisterPage.vue";
import index from "../components/IndexPage.vue";
import msg from "../components/MsgPage.vue";
import change from "../components/ChangeInfo.vue"
import self from "../components/UserHome.vue"

import cls from "../components/ClassPage.vue"
import info from "../components/ClassInfoPage.vue"


//2.使用路由
Vue.use(VueRouter);

//3.创建VueRouter的实例
const router = new VueRouter({
    //tips:不想要 #（锚点）就添加下面代码
     mode:'history', 
    //4.配置路由的path和组件
    routes :[
        {
          path: "/",
          name:'login',
          component: login,
        },
        {
          path: "/login",
          name:'login',
          component: login,
        },
        {
          path: "/register",
          name:'register',
          component: register,
        },
        {
          path: "/home",
          name:'index',
          component: index,
          children:[
            {
              path: "/msg",
              name:'msg',
              component: msg,
            },
            {
              path: "/chg",
              name:'chg',
              component: change,
            },
            {
              path: "/self",
              name:'self',
              component: self,
            },
            {
              path: "/class",
              name:'cls',
              component: cls,
            },
            {
              path: "/class/:classID",
              name:'clsInfo',
              component: info,
            }
          ]
        },
        ]

      
});

//获取原型对象上的push函数
const originalPush = VueRouter.prototype.push
//修改原型对象中的push方法
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}


// router.beforeEach((to, from, next) => {
//   const token = window.localStorage.getItem('CCNtoken');
//   // 有token
//   if (token) {
//     // 直接放行
//     if(to.path === '/'){
//       next({"path": "/home" })
//     }
//     // else if(to.path === '/home/projectPage'){
//     //   return next({ "path": "/home/projectPage/intro"})
//     // }
//     else{
//       next();
//     }
    
//   } else {  // 否则是没有
//       if(to.path === '/home/projectPage'){
//     return next({ "path": "/home/projectPage/intro"})
//   }
//     // 如果去的是登录页
//     if (to.path === '/login' || to.path==='/register') {
//       // 直接放行
//       next();
//     } else {
//       return next({ "path": "/login" })
//     }
//   }
//   next();
// });
// 5.导入路由实例
export default router
