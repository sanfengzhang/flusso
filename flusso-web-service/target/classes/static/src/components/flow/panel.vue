<template>
    <div v-if="easyFlowVisible" >
        <el-row>
            <el-col :span="4" ref="flowTool">
                <flowTool @addNode="addNode"></flowTool>							
            </el-col>          		 
            <el-col :span="20">
                <el-row>
                    <el-col :span="24">
                        <div style="margin-bottom: 5px; margin-left: 10px">
                           <!-- <el-link type="primary">{{data.name}}</el-link>-->
						    <el-button type="success" @click="dataReloadB" icon="el-icon-refresh">切换流程B</el-button>
                            <el-button type="info" icon="el-icon-document" @click="dataInfo">流程信息</el-button>
                           <!--  <el-button type="primary" @click="dataReloadA" icon="el-icon-refresh">切换流程A</el-button>
                           
                            <el-button type="warning" @click="dataReloadC" icon="el-icon-refresh">切换流程C</el-button>-->
                            <el-button type="warning" @click="changeLabel" icon="el-icon-refresh">设置线</el-button>
                            <el-button type="info" icon="el-icon-document" @click="addNewDataFlow">创建流程</el-button>	
							<el-button type="info" icon="el-icon-document" @click="saveDataFlow">保存当前流程</el-button>
                            <el-button type="info" icon="el-icon-document" @click="createNewCommand">创建命令</el-button>	
                            <el-button type="info" icon="el-icon-document" @click="createNewCommandInstance">创建命令实例</el-button>							
                            <el-select  @change="selectFlow" v-model="firstFlow">							   
							    <el-option  v-for="item in selectFlowData" :key="item.flowEntity.id"  :label="item.flowEntity.dataProcessFlowName" :value="item.flowEntity.id">
                                    {{item.flowEntity.dataProcessFlowName}}
                                </el-option>
							</el-select>				
                        </div>
                    </el-col>
                </el-row>
                <el-row>
                    <el-col :span="24">
                        <!--画布-->
                        <div id="flowContainer" class="container">
                            <template v-for="node in data.nodeList">
                                <flow-node
                                        v-show="node.show"
                                        :id="node.id"
                                        :node="node"
                                        @deleteNode="deleteNode"
                                        @changeNodeSite="changeNodeSite"
                                        @nodeRightMenu="nodeRightMenu"
                                        @editNode="editNode"									
                                >
                                </flow-node>
                            </template>
                        </div>
                    </el-col>
                </el-row>
            </el-col>			
        </el-row>
		

        <flow-info v-if="flowInfoVisible" ref="flowInfo" :data="data"></flow-info>
        <flow-node-form v-if="nodeFormVisible" ref="nodeForm"></flow-node-form>
		<flow-add-flow-form v-if="flowAddFormVisible" ref="addFlowForm" @initFlowPanel="initFlowPanel"></flow-add-flow-form>
		<flow-new-cmd-form v-if="newCmdFormVisible" ref="newCmdForm" @newCmdForm="newCmdForm"></flow-new-cmd-form>
		<flow-new-cmd-instance-form v-if="newCmdInstanceFormVisible" ref="newCmdInstanceForm" @newCmdInstanceForm="newCmdInstanceForm"></flow-new-cmd-instance-form>

    </div>

</template>

<script>   
    import draggable from 'vuedraggable'
    import {jsPlumb} from 'jsplumb'
    import flowNode from '@/components/flow/node'
    import flowTool from '@/components/flow/tool'
    import FlowInfo from '@/components/flow/info'
    import FlowNodeForm from './node_form'
	import FlowAddFlowForm from './data_flow_form'
	
	import FlowNewCmdInstanceForm from './new_cmd_instance_form'
	import FlowNewCmdForm from './new_cmd_form'
    import lodash from 'lodash'
    import {getDataA} from './data_A'
    import {getDataB} from './data_B'
    import {getDataC} from './data_C'
	import {getDataInit} from './data_init'

    export default {
        name: "easyFlow",
        data() {
            return {
                jsPlumb: null,// jsPlumb 实例
                easyFlowVisible: true,
                flowInfoVisible: false,
                nodeFormVisible: false,
				flowAddFormVisible: false,
				newCmdFormVisible: false,
				newCmdInstanceFormVisible: false,
                index: 1,
                // 默认设置参数
                jsplumbSetting: {
                    // 动态锚点、位置自适应
                    Anchors: ['Top', 'TopCenter', 'TopRight', 'TopLeft', 'Right', 'RightMiddle', 'Bottom', 'BottomCenter', 'BottomRight', 'BottomLeft', 'Left', 'LeftMiddle'],
                    Container: 'flowContainer',
                    // 连线的样式 StateMachine、Flowchart
                    Connector: 'Flowchart',
                    // 鼠标不能拖动删除线
                    ConnectionsDetachable: false,
                    // 删除线的时候节点不删除
                    DeleteEndpointsOnDetach: false,
                    // 连线的端点
                    // Endpoint: ["Dot", {radius: 5}],
                    Endpoint: ["Rectangle", {height: 10, width: 10}],
                    // 线端点的样式
                    EndpointStyle: {fill: 'rgba(255,255,255,0)', outlineWidth: 1},
                    LogEnabled: true,//是否打开jsPlumb的内部日志记录
                    // 绘制线
                    PaintStyle: {stroke: 'black', strokeWidth: 3},
                    // 绘制箭头
                    Overlays: [['Arrow', {width: 12, length: 12, location: 1}]],
                    RenderMode: "svg"
                },
                // jsplumb连接参数
                jsplumbConnectOptions: {
                    isSource: true,
                    isTarget: true,
                    // 动态锚点、提供了4个方向 Continuous、AutoDefault
                    anchor: "Continuous"
                },
                jsplumbSourceOptions: {
                    filter: ".flow-node-drag",/*"span"表示标签，".className"表示类，"#id"表示元素id*/
                    filterExclude: false,
                    anchor: "Continuous",
                    allowLoopback: false
                },
                jsplumbTargetOptions: {
                    filter: ".flow-node-drag",/*"span"表示标签，".className"表示类，"#id"表示元素id*/
                    filterExclude: false,
                    anchor: "Continuous",
                    allowLoopback: false
                },
                // 是否加载完毕
                loadEasyFlowFinish: false,
                // 数据
                data: {"nodeList":[]},
				firstFlow: '请选择',
				selectFlowData: {},
            }
        },
        components: {
            draggable, flowNode, flowTool, FlowInfo, FlowNodeForm,FlowAddFlowForm,FlowNewCmdForm,FlowNewCmdInstanceForm
        },
		
        mounted() {
		    this.$nextTick(() => {
                     this.get('/api/v1/flow',{}).then((data) => {
					 this.selectFlowData=data.data	
                     console.log("flowDataList",this.flowDataList)					 
                  });
             })	
		  
            this.jsPlumb = jsPlumb.getInstance()
           	  
        },
        methods: {
            jsPlumbInit() {
                const _this = this
                this.jsPlumb.ready(function () {

                    // 导入默认配置
                    _this.jsPlumb.importDefaults(_this.jsplumbSetting)
                    // 会使整个jsPlumb立即重绘。
                    _this.jsPlumb.setSuspendDrawing(false, true);
                    // 初始化节点
					
                    _this.loadEasyFlow()

                    // 单点击了连接线,
                    _this.jsPlumb.bind('click', function (conn, originalEvent) {
                        console.log("click", conn)

                        _this.$confirm('确定删除所点击的线吗?', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(() => {
                            _this.jsPlumb.deleteConnection(conn)
                        }).catch(() => {
                        })

                    })
                    // 连线
                    _this.jsPlumb.bind("connection", function (evt) {
                        console.log('connection', evt)
                        let from = evt.source.id
                        let to = evt.target.id						
                        if (_this.loadEasyFlowFinish) {
                            _this.data.lineList.push({
                                from: from,
                                to: to
                            })
                        }
                    })

                    // 删除连线
                    _this.jsPlumb.bind("connectionDetached", function (evt) {
                        console.log('connectionDetached', evt)
                        _this.deleteLine(evt.sourceId, evt.targetId)
                    })

                    // 改变线的连接节点
                    _this.jsPlumb.bind("connectionMoved", function (evt) {
                        console.log('connectionMoved', evt)
                        _this.changeLine(evt.originalSourceId, evt.originalTargetId)
                    })

                    // 单击endpoint
                    // jsPlumb.bind("endpointClick", function (evt) {
                    //   console.log('endpointClick', evt)
                    // })
                    //
                    // // 双击endpoint
                    // jsPlumb.bind("endpointDblClick", function (evt) {
                    //   console.log('endpointDblClick', evt)
                    // })

                    // contextmenu
                    _this.jsPlumb.bind("contextmenu", function (evt) {
                        console.log('contextmenu', evt)
                    })


                    // beforeDrop
                    _this.jsPlumb.bind("beforeDrop", function (evt) {
                        console.log('beforeDrop', evt)
                        _this.$message.error('beforeDrop');
                        _this.$message({
                            message: '恭喜你，这是一条成功消息',
                            type: 'success'
                        });
                        let from = evt.sourceId
                        let to = evt.targetId
                        if (from === to) {
                            _this.$message.error('不能连接自己');
                            return false
                        }
                        if (_this.hasLine(from, to)) {
                            _this.$message.error('不能重复连线');
                            return false
                        }
                        if (_this.hashOppositeLine(from, to)) {
                            _this.$message.error('不能回环哦');
                            return false
                        }
                        return true
                    })

                    // beforeDetach
                    _this.jsPlumb.bind("beforeDetach", function (evt) {
                        console.log('beforeDetach', evt)
                    })
                })
            },
            // 加载流程图
            loadEasyFlow() {
                console.log("loadEasyFlow:",this.data.nodeList.length)
                // 初始化节点
                for (var i = 0; i < this.data.nodeList.length; i++) {
                    let node = this.data.nodeList[i]
                    // 设置源点，可以拖出线连接其他节点
                    this.jsPlumb.makeSource(node.id, this.jsplumbSourceOptions)
                    // // 设置目标点，其他源点拖出的线可以连接该节点
                    this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
                    // jsPlumb.addEndpoint(node.id)
                    // 设置可拖拽
                    // jsPlumb.draggable(node.id, {
                    //     containment: 'parent',
                    //     grid: [10, 10]
                    // })

                    this.jsPlumb.draggable(node.id, {
                        containment: 'parent'
                    })

                    // jsPlumb.draggable(node.id)
                }

                // 初始化连线
                for (var i = 0; i < this.data.lineList.length; i++) {
                    let line = this.data.lineList[i]
                    this.jsPlumb.connect({
                        source: line.from,
                        target: line.to
                    }, this.jsplumbConnectOptions)
                }
                this.$nextTick(function () {
                    this.loadEasyFlowFinish = true
                })
            },
            getNodes() {
                console.log(jsPlumb)
                console.log(jsPlumb.Defaults)
            },
            getLines() {
                console.log('线', jsPlumb.getConnections())
            },
            // 删除线
            deleteLine(from, to) {
                this.data.lineList = this.data.lineList.filter(function (line) {
                    return line.from !== from && line.to !== to
                })
            },
            // 改变连线
            changeLine(oldFrom, oldTo) {
                this.deleteLine(oldFrom, oldTo)
            },
            // 改变节点的位置
            changeNodeSite(data) {
                for (var i = 0; i < this.data.nodeList.length; i++) {
                    let node = this.data.nodeList[i]
                    if (node.id === data.nodeId) {
                        node.left = data.left
                        node.top = data.top
                    }
                }
            },
            // 添加新的节点
            addNode(evt, nodeMenu, mousePosition) {
                console.log('添加节点', nodeMenu)
                let width = this.$refs.flowTool.$el.clientWidth
                const index = this.index++
                let nodeId = this.uuid()		  
			   // let nodeId = nodeMenu.id
                var left = mousePosition.left
                var top = mousePosition.top
                if (mousePosition.left < 0) {
                    left = evt.originalEvent.layerX - width
                }
                if (mousePosition.top < 0) {
                    top = evt.originalEvent.clientY - 50
                }
				console.log("从菜单添加新节点", nodeMenu)
                
				var node = {
                    id: nodeId,						
					commandInstanceEntity:nodeMenu.data,					
                    name: nodeMenu.name+'-'+ index,
                    left: left + 'px',
                    top: top + 'px',
                    ico: nodeMenu.ico,
                    show: true
                }
				console.log("从菜单添加新节点-结果", node)
                this.data.nodeList.push(node)				
                this.$nextTick(function () {				    
                    this.jsPlumb.makeSource(nodeId, this.jsplumbSourceOptions)

                    this.jsPlumb.makeTarget(nodeId, this.jsplumbTargetOptions)

                    this.jsPlumb.draggable(nodeId, {
                        containment: 'parent'
                    })

                })
            },
            // 是否具有该线
            hasLine(from, to) {
                for (var i = 0; i < this.data.lineList.length; i++) {
                    var line = this.data.lineList[i]
                    if (line.from === from && line.to === to) {
                        return true
                    }
                }
                return false
            },
            // 是否含有相反的线
            hashOppositeLine(from, to) {
                return this.hasLine(to, from)
            },
            nodeRightMenu(nodeId, evt) {
                this.menu.show = true
                this.menu.curNodeId = nodeId
                this.menu.left = evt.x + 'px'
                this.menu.top = evt.y + 'px'
            },
            deleteNode(nodeId) {

                this.$confirm('确定要删除节点' + nodeId + '?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    closeOnClickModal: false
                }).then(() => {
                    var i=0;
                    this.data.nodeList = this.data.nodeList.filter(function (node) {

                        // return node.id !== nodeId
						i=i+1
                        if (node.id === nodeId) {                          					
                            node.show = false					   
							
                        }
                        return true
                    })
					this.data.nodeList.splice(i-1, 1);	
                    this.$nextTick(function () {
                        console.log('删除' + nodeId)
                        this.jsPlumb.removeAllEndpoints(nodeId);
                    })
                }).catch(() => {
                })
                return true
            },
            editNode(nodeId) {
                console.log('编辑节点', nodeId)				
                this.nodeFormVisible = true
				var nodeData=null
				this.data.nodeList.forEach(v=>{  
                      if(v.id==nodeId){
					       nodeData=v
						   return
					  }   
                })
                 console.log('编辑节点', nodeData)		 
                this.$nextTick(function () {				   	
                    this.$refs.nodeForm.init(nodeData, nodeId)
                })
            },
		
            // 流程数据信息
            dataInfo() {
                this.flowInfoVisible = true
                this.$nextTick(function () {
                    this.$refs.flowInfo.init()
                })
            },
            dataReload(data) {
                this.easyFlowVisible = false
                this.data.nodeList = []
                this.data.lineList = []
                this.$nextTick(() => {
                    // 这里模拟后台获取数据、然后加载
					if(data instanceof Array){					
                    data = lodash.cloneDeep(data[0])
					}
					else{
					data=lodash.cloneDeep(data)
					}
                    this.easyFlowVisible = true
                    this.data = data
                    this.$nextTick(() => {
                        this.jsPlumb = jsPlumb.getInstance()
                        this.$nextTick(() => {
                            this.jsPlumbInit()
                        })
                    })
                })
            },
            addNewDataFlow(){
			      console.log("创建新流程")
				   this.flowAddFormVisible = true
				   this.$nextTick(function () {
                    this.$refs.addFlowForm.init()
                })
			},
            initFlowPanel(data){
			    console.log("。。。。。")
			    console.log("初始化新流程")
			},
            saveDataFlow(){
			   console.log("save flow:",this.data)
			   this.post('/api/v1/flow/relation',this.data).then((response) => {                     
                  this.$message('保存成功!');					  
                });
			
			},			
            selectFlow(vid){    
			   
				if('undfined'===vid){
				    return
				}
				this.$nextTick(() => {
                     this.get('/api/v1/flow/'+vid,{}).then((data) => {
					 console.log("选择flow",data.data)
                     this.dataReload(data.data)                    			  
                  });
               })				
			},
            createNewCommand(){
			    console.log("创建新命令")
				this.newCmdFormVisible=true
				 this.$nextTick(function () {
                   this.$refs.newCmdForm.init()
                })
				
			},
            createNewCommandInstance(){
			 console.log("创建新命令实例")
				this.newCmdInstanceFormVisible=true
				 this.$nextTick(function () {
                   this.$refs.newCmdInstanceForm.init()
                })
			
			},
            newCmdInstanceForm(){},			
            dataReloadA() {
                this.dataReload(getDataA())
            },
            dataReloadB() {
                this.dataReload(getDataB())
            },
            dataReloadC() {
                this.dataReload(getDataC())
            },
            changeLabel() {
                var lines = this.jsPlumb.getConnections({
                    source: 'nodeA',
                    target: 'nodeB'
                })
                lines[0].setLabel({
                    label: 'admin',
                    cssClass: 'labelClass a b'
                })

            }
        }
    }
</script>

<style>
    #flowContainer {
        background-image: linear-gradient(90deg, rgba(0, 0, 0, 0.15) 10%, rgba(0, 0, 0, 0) 10%), linear-gradient(rgba(0, 0, 0, 0.15) 10%, rgba(0, 0, 0, 0) 10%);
        background-size: 10px 10px;
        height: 500px;
        background-color: rgb(251, 251, 251);
        /*background-color: #42b983;*/
        position: relative;
    }

    .labelClass {
        background-color: white;
        padding: 5px;
        opacity: 0.7;
        border: 1px solid #346789;
        /*border-radius: 10px;*/
        cursor: pointer;
        -webkit-user-select: none;
        -moz-user-select: none;
        -ms-user-select: none;
        user-select: none;
    }
</style>
