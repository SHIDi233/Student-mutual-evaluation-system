// JavaScript Document
			// 发送AJAX POST请求
            // 1. 创建AJAX核心对象
            var xhr = new XMLHttpRequest();
            // 2. 注册回调函数
            xhr.onreadystatechange = function (){
                if (this.readyState == 4) {
                    if (this.status == 200) {
						var res = xhr.responseText;
						var obj = JSON.parse(res);
						if(obj.code==1){
							alert("success");
//							window.location.href="https://www.bilibili.com";
						}
						else{
							alert("false");
						}
//						if(res=="true"){
//							alert("success");
//							window.location.href="https://www.bilibili.com";
//						}
//						else{
//							alert("false");
//						}
                    }else{
                        alert(this.status);
						fail&&fail(newError('接口请求失败'));
                    }
                }
            }
            // 3. 开启通道
            xhr.open("POST", "/login", true)
 			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
			xhr.send("username="+a+"&password="+p)
            // 4. 发送请求
            xhr.send()