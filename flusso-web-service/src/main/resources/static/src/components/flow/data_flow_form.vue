<template>
    <el-dialog
            title="新建数据流程"
            :close-on-click-modal="false"
            :visible.sync="visible"
    >
        <el-form :model="dataProcessFlow" ref="flowAddForm" label-width="120px">
            <el-form-item label="名称" prop="flowName" >
                <el-input type="text" v-model="dataProcessFlow.dataProcessFlowName" ></el-input>
            </el-form-item>
			 <el-form-item  label="外部类加载路径" prop="classPath">
                <el-input type="text" v-model="dataProcessFlow.loadExternalLibsPath"></el-input>
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
                dataProcessFlow: {
				    dataProcessFlowName:'',
				    loadExternalLibsPath:''
				}
            }
        },
		 methods: {
            init() {            
			   this.visible=true
            },
			submitForm(){				 
			    console.log("add flow",this.dataProcessFlow);
				this.post('/api/v1/flow',this.dataProcessFlow).then((result) => {
				             this.$message('流程保存成功!');
						     this.$emit('initFlowPanel', this.dataProcessFlow);	
							 this.visible=false;	 
                             this.$refs['flowAddForm'].resetFields();	
				 
        
                });
                             			
			  
	        }
        }
	  
    }
</script>

<style scoped>

</style>
