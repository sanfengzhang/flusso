import axios from "axios";
import qs from "qs";

//添加请求拦截器
axios.interceptors.request.use(config =>{return config;},error => { return Promise.reject(error);});

//添加响应拦截器
axios.interceptors.response.use(response => { return response;},error => {return Promise.resolve(error.response);});

axios.defaults.baseURL = "http://localhost:8080/ETL";
axios.defaults.responseType = 'json' // 默认数据响应类型
axios.defaults.headers.post["Content-Type"] = "application/json;charset=UTF-8";
axios.defaults.headers.get["Content-Type"] = "application/x-www-form-urlencoded; charset=UTF-8";
//axios.defaults.withCredentials = true
axios.defaults.timeout = 10000



 export function  post(url, body) {
    return new Promise((resolve, reject) =>{
			axios.post(url, body).then(response => {				
				if(response){
				  if(response.status === 500){
					 this.$message.error(response.data.errResult[0]);
			      }else{                    			
					resolve(response.data.data)
				  }	
				}else{
			      console.error("请求失败："+url);
				  this.$message.error('请求失败');	
				}			    
			});
			
	}).catch(err =>		
		     {
				  this.$message.error('请求失败'+err);
			      console.error("errorr:"+err)
		     });
    }				 
				 
				 
  
  
 export function  get(url, params) {
    return new Promise((resolve, reject) =>{
	  axios.get(url, {params: params}).then(response => {
				if(response){
				  if(response.status === 500){
					  this.$message.error(response.data.errResult[0]);
			      }else{                    			
					resolve(response.data)
				  }	
				}else{
				  console.error("请求失败："+url);
				  this.$message.error('请求失败');	
				}			    
			});
			
	  }).catch(err =>		
		     {
				  this.$message.error('请求失败'+err);
			      console.error("errorr:"+err)
		     });
    }
	
	
  export function uuid() {
	 var len=32
	 var radix=16
     var chars = '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz'.split('');
     var uuid = [], i;
     radix = radix || chars.length;
  
     if (len) {
       // Compact form
       for (i = 0; i < len; i++) uuid[i] = chars[0 | Math.random()*radix];
     } else {
       // rfc4122, version 4 form
       var r;
  
       // rfc4122 requires these characters
       uuid[8] = uuid[13] = uuid[18] = uuid[23] = '-';
       uuid[14] = '4';
  
       // Fill in random data.  At i==19 set the high bits of clock sequence as
       // per rfc4122, sec. 4.1.5
       for (i = 0; i < 36; i++) {
         if (!uuid[i]) {
           r = 0 | Math.random()*16;
           uuid[i] = chars[(i == 19) ? (r & 0x3) | 0x8 : r];
         }
       }
     }
  
     return uuid.join('');
 }

  


