// JavaScript Document
function commit() {
	window.location.href="..?token="+window.sessionStorage.getItem("CCNtoken");		
			var m = jQuery("#mail").val();
			var n = jQuery("#name").val();
			var p = jQuery("#pwd").val();
	
	
			var emreg=/^\w{3,}(\.\w+)*@[A-z0-9]+(\.[A-z]{2,5}){1,2}$/;
	
			if(m==''){
				alert("邮箱地址不能为空");
				return;
			}
			else if(emreg.test(m)==false){
				alert("请输入正确的邮箱地址");
				return;
			} 
			if(n==''){
				alert("昵称不能为空");
				return;
			}
			if(p==''){
				alert("密码不能为空");
				return;
			}
	
			if(p.length<5){
				alert("密码位数至少大于5");
				return;
			}
	
			
	
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
//							window.sessionStorage.setItem("CCNtoken",obj.data);
//							alert(window.sessionStorage.getItem("CCNtoken"));
							alert("注册成功");
							window.location.href="../login";
						}
						else{
							alert("注册失败：邮箱已被注册");
						}
                    }else{
                        alert(this.status);
                    }
                }
            }
            // 3. 开启通道
            xhr.open("POST", "/register", true)
 			xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded")
			xhr.send("mail="+m+"&name="+n+"&password="+p)
            // 4. 发送请求
            xhr.send()
		}