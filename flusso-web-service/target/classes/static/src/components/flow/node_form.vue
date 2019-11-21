<template>
    <el-dialog
            title="节点信息"
            :close-on-click-modal="false"
            :visible.sync="visible"
    >
        <el-form  :model="node"  ref="dataForm" label-width="120px">
            <el-form-item label="名称">
                <el-input v-model="node.name"></el-input>
            </el-form-item>
              <el-form-item label="类名称">
                <el-input   v-model="node.commandInstanceEntity.command.commandClazz"></el-input>
            </el-form-item>
            <el-form-item label="转换名称">
                <el-input v-model="node.commandInstanceEntity.command.commandMorphName"></el-input>
            </el-form-item>
			<div v-for="item in node.commandInstanceEntity.cmdInstanceParams">
			 <el-form-item :label="item.fieldName">
                <el-input v-model="item.fieldValue"></el-input>	
              </el-form-item>				
			</div>
       <!-- <el-form-item label="ico">
                <el-input v-model="node.ico"></el-input>
            </el-form-item>-->
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button @click="visible = false" icon="el-icon-close">取消</el-button>
            <el-button type="primary" icon="el-icon-check">确定</el-button>
        </span>
    </el-dialog>
</template>

<script>
    export default {
        data() {
            return {
                visible: false,
                node: {
				   commandInstanceEntity:{
				      commandClazz:{}
				   }
				}
            }
        },
        methods: {
            init(data, id) {			    	
			    this.node= data
				console.log('编辑节点信息', this.node)	
				console.log('编辑节点信息', this.node.commandInstanceEntity)		
                this.visible = true           
               
            }
        }
    }
</script>

<style scoped>

</style>
