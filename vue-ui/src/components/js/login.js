// JavaScript Document	
import axios from 'axios'
function regist() {
	window.location.href="../register";
}

function getValue() {
	var a = jQuery("#account").val();
	var p = jQuery("#pwd").val();
	axios.post('https://www.bilibili.com', {  }).then(response => {
		console.log(response.data)
	}).catch(error => {
		console.log(error)
	})
	// 发送AJAX POST请求
	// 1. 创建AJAX核心对象
// 	var xhr = new XMLHttpRequest();
// 	// 2. 注册回调函数
// 	xhr.onreadystatechange = function (){
// 		if (this.readyState == 4) {
// 			if (this.status == 200) {
// 				var res = xhr.responseText;
// 				var obj = JSON.parse(res);
// 				if(obj.code==1){
// 					window.sessionStorage.setItem("CCNtoken",obj.data);
// 					alert(window.sessionStorage.getItem("CCNtoken"));
// 					window.location.href="../";
					
// //							var result="";
// //							jQuery.ajax({
// //								"type":"get",
// //								"url":"../index",
// //								headers: {
// //        							token:  window.sessionStorage.getItem("CCNtoken")
// //    							},
// //								"success":function(rel){
// ////									var newPage = window.open("about:blank", "_blank");
// ////            						//将后台传过来的html页面写到新打开的浏览器窗口中显示
// ////            						newPage.document.write(rel.data);
// ////									$("html").html($("html", rel).html());
// //									var content = rel.responseText;
// //									var data = new Blob([content],{type:"text/html"});
// //                    				var downloadUrl = window.URL.createObjectURL(data);
// //                    				var anchor = document.createElement("a");
// //                    				anchor.href = downloadUrl;
// //									anchor.click();
// //
// //								}
// //							});
// 				}
// 				else{
// 					alert("false");
// 				}
// 			}else{
// 				alert(this.status);
// 			}
// 		}
// 	}
// 	// 3. 开启通道
// 	xhr.open("POST", "/login", true)
// 	 xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
// 	xhr.send("mail="+a+"&password="+p)
// 	// 4. 发送请求
// 	xhr.send()
}