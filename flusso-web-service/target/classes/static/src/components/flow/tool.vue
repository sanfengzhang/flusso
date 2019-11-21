<template>
    <div style="background-color: #66a6e0;" ref="tool">
	
        <el-menu :default-openeds="defaultOpeneds" expand-on-click-node="false">
		  <el-submenu v-if="menuList.length > 0" v-for="(menu,index) in  menuList"  :index=String(menu.type+index) :key="menu.type+index">
		        <template slot="title">				    
                  <i :class="menu.ico"></i>
                  <span>{{menu.name}}</span>                 
                </template>	
               <el-submenu  v-if="menuList.length > 0" v-for="(menu,index) in  menu.children" :index=String(menu.type+index)  :key="menu.type+index" >               
                  <template slot="title">
                    <i :class="menu.ico"></i>
                    <span>{{menu.name}}</span>
					<!--<span> <el-button type="text" size="mini" @click="() => append(data)"> Append</el-button></span> -->
                  </template>               
                  <el-menu-item-group>
                     <draggable @end="addNode" @choose="move" v-model="menu.children" :options="draggableOptions">
                        <el-menu-item v-for="(son,i) in menu.children" :key="son.id" :index="son.id" :type=JSON.stringify(son) :aaa="son.type" :bbb="son.type">
                           {{son.name}}
                        </el-menu-item>
                      </draggable>
                   </el-menu-item-group>
              </el-submenu>
		   </el-submenu>
        </el-menu>
		
		 
    </div>
</template>
<script>
    import draggable from 'vuedraggable'
    var mousePosition = {
        left: -1,
        top: -1
    }
    export default {
        data() {
            return {
                draggableOptions: {
                    preventOnFilter: false
                },
                defaultOpeneds: ['group00', 'group11'],
                menuList: [
                    
                ],
                nodeMenu: {}
            }
        },
      mounted() {
	      this.$nextTick(() => {
	         this.get('/api/v1/main/menu',{}).then((data) => {                    
                   this.menuList = data.data
				   console.log("menun:", this.menuList)		 
                 })
		 });
      },
	  
      
        components: {
            draggable
        },
        created() {
            /**
             * 以下是为了解决在火狐浏览器上推拽时弹出tab页到搜索问题
             * @param event
             */
            if (this.isFirefox()) {
                document.body.ondrop = function (event) {
                    // 解决火狐浏览器无法获取鼠标拖拽结束的坐标问题
                    mousePosition.left = event.layerX
                    mousePosition.top = event.clientY - 50
                    event.preventDefault();
                    event.stopPropagation();
                }
            }
        },
        methods: {
            // 根据类型获取左侧菜单对象
            getMenu(type) {
                for (var i = 0; i < this.menuList.length; i++) {
                    let children = this.menuList[i].children;
                    for (var j = 0; j < children.length; j++) {
                        let son = children[j]
                        if (son.type === type) {
                            return son
                        }
                    }
                }
            },
            move(evt) {
                var attrs = evt.item.attributes
				//console.info("select",attrs.type.nodeValue)
                this.nodeMenu = JSON.parse(attrs.type.nodeValue)
				
				
            },
            // 添加节点
            addNode(evt) {			  
                this.$emit('addNode', evt, this.nodeMenu, mousePosition)
            },
            // 是否是火狐浏览器
            isFirefox() {
                var userAgent = navigator.userAgent
                if (userAgent.indexOf("Firefox") > -1) {
                    return true
                }
                return false
            }
        }
    }
</script>
<style>
    .flow-tool-menu {
        background-color: #eeeeee;
        cursor: pointer;
        padding-left: 5px;
        height: 50px;
        line-height: 50px;
        border-bottom: 1px solid #979797
    }

    .flow-tool-submenu {
        background-color: white;
        padding-left: 20px;
        cursor: pointer;
        height: 50px;
        line-height: 50px;
        vertical-align: middle;
        border-bottom: 1px solid #d3d3d3
    }
</style>
