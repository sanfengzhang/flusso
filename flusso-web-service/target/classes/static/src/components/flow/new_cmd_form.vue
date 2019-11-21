<template>
    <el-dialog
            title="新建命令"
            :close-on-click-modal="false"
            :visible.sync="visible"
    >
        <el-form :model="command" ref="newCmdForm" label-width="120px">
            <el-form-item label="命令名称" prop="commandName" >
                <el-input type="text" v-model="command.commandName" ></el-input>
            </el-form-item>
			 <el-form-item  label="类型" prop="commandType">
                <el-input type="text" v-model="command.commandType"></el-input>
            </el-form-item>
			 <el-form-item  label="类名称" prop="commandClazz">
                <el-input type="text" v-model="command.commandClazz"></el-input>
            </el-form-item>
			<el-form-item  label="别名" prop="commandMorphName">
                <el-input type="text" v-model="command.commandMorphName"></el-input>
            </el-form-item>
		<div v-if="command.cmdParams.length>0" v-for="(d,index) in command.cmdParams" :key="index">	
	      <el-form-item  :label="'参数-' + (index+1)"  prop="'cmdParams'+index"> 
			<el-input placeholder="请输入参数名称" v-model="d.fieldName" class="input-with-select">			 
                <el-select v-model="d.fieldType"  :label="index" :key="index" slot="prepend" placeholder="请选择参数类型" >
				  <el-option label="字符串" value="java.lang.String"></el-option>
                  <el-option label="整形" value="java.lang.Integer"></el-option>   
                  <el-option label="布尔类型" value="java.lang.Boolean"></el-option> 				  
                  <el-option label="浮点型" value="java.lang.Float"></el-option>
				  <el-option label="数组" value="java.util.ArrayList"></el-option>
				  <el-option label="键值对" value="java.util.Map"></el-option>
               </el-select>
               <template slot="append"> <i @click="add" class="el-icon-circle-plus"></i></template>					   
             </el-input>			
       </el-form-item>	  
	   </div>
      </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="visible = false " icon="el-icon-close">取消</el-button>
            <el-button type="primary"  icon="el-icon-check" @click="submitForm()">确定</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        data() {
            return {
                visible: false,
                command: {
				    commandName:'',
				    commandClazz:'',
					commandType: '',
					cmdParams:[{}]
				}
              }
         }
        ,
		 methods: {
            init() {            
			   this.visible=true
            },
			submitForm(){
			   console.log("创建命令",this.command)
               this.post('/api/v1/command',this.command).then((result) => {
				          this.$message('命令保存成功!');						    
						  this.visible=false;	 
                          this.$refs['newCmdForm'].resetFields();	
                           
                });			   
			  
	        },
		  add(){
		      
			  this.command.cmdParams.push({	       
			  })
			}
        }
	  
    }
</script>

<style>
  .el-select .el-input {
    width: 190px;
  }
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
