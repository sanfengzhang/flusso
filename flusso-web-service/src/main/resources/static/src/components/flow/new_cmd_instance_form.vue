<template>
    <el-dialog
            title="新建命令实体"
            :close-on-click-modal="false"
            :visible.sync="visible"
    >
        <el-form :model="commandInstance" ref="newCmdInstanceForm" label-width="120px">
            <el-form-item label="命令实体名称" prop="commandInstanceName" >
                <el-input type="text" v-model="commandInstance.commandInstanceName" ></el-input>
        </el-form-item>
		
		<el-form-item label="命令" prop="command" >
            <el-select @change="selectCommand" v-model="commandInstance.command"    placeholder="请选择">			
               <el-option   v-for="item in commands"  :label="item.commandName"  :value="item.id">                   
               </el-option>			    		
            </el-select>
        </el-form-item>			
		
		
	    <el-form-item  v-for="(d,index) in cmdParams" :label="'参数-' + (d.fieldName)"  > 
			<el-input placeholder="请输入参数的值"  v-model="d.fieldValue" >  
                  <template slot="prepend">{{d.fieldType}}</template>		
             </el-input>			
        </el-form-item>

        <el-form-item v-if="selectCmd.commandMorphName==='branchPipe'" label="分支流程" prop="subFlows" > 	
            <el-select v-model="commandInstance.subFlows" multiple placeholder="请选择">
                <el-option v-for="item in subFlows"  :label="item.flowEntity.dataProcessFlowName" :value="item.flowEntity.id">
                </el-option>
            </el-select>		
	    </el-form-item>		

        <el-form-item v-if="selectCmd.commandMorphName==='callSubPipe'" label="子流程" prop="subFlows" > 	
            <el-select v-model="commandInstance.subFlows" multiple placeholder="请选择">
                <el-option v-for="item in subFlows"  :label="item.flowEntity.dataProcessFlowName" :value="item.flowEntity.id">
                </el-option>
            </el-select>		
	    </el-form-item>	
	
      
	   
      </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="visible = false" icon="el-icon-close">取消</el-button>
            <el-button type="primary"  icon="el-icon-check" @click="submitForm()">确定</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        data() {
            return {			   
                visible: false,
				commands: [],
				cmdParams:[],
				selectCmd:{},
				subFlows: [],
                commandInstance: {
				    commandInstanceName:'',				   
					cmdParams:[],
					subFlows:[],
					selectSubFlowClazz:''
				},				
              }
         }
        ,
		 methods: {
            init() {
             
			   this.visible=true
			   this.$nextTick(() => {
	             this.get('/api/v1/command',{}).then((data) => {                    
                      this.commands = data.data	                     		 
                  })
				   this.get('/api/v1/flow',{}).then((data) => {   
                     console.log("this.flows=",data.data)				   
                     this.subFlows = data.data				  
                  })
		       });
			   
            },
			selectCommand(id){				  
                   var command=null
				   this.commands.forEach(v=>{  
                      if(v.id==id){
					       command=v	
						   
						   this.cmdParams=command.cmdParams
						   this.selectCmd=command
						   return
					  }   
                })		   
			},
			submitForm(){			  
			  this.commandInstance['cmdParams']=this.cmdParams
			  console.log("创建命令",this.commandInstance)
			  this.post('/api/v1/command-instance',this.commandInstance).then((result) => {
				          this.$message('命令保存成功!');						    
						  this.visible=false;	 
                          this.$refs['newCmdInstanceForm'].resetFields();	
                           
                });
	        }
        }
	  
    }
</script>

<style>
 
  .input-with-select .el-input-group__prepend {
    background-color: #fff;
  }
</style>
