import Login from './views/Login.vue'
import NotFound from './views/404.vue'
import Home from './views/Home.vue'
import Gogs from './views/Gogs.vue'
import Main from './views/Main.vue'
import Table from './views/nav1/Table.vue'
import Form from './views/nav1/Form.vue'
import user from './views/nav1/user.vue'
import Page4 from './views/nav2/Page4.vue'
import Page5 from './views/nav2/Page5.vue'
import Page6 from './views/nav3/Page6.vue'
import FileContent from './views/nav3/index.vue'
import echarts from './views/charts/echarts.vue'



import Repository from './views/repository/index2.vue'
import Model from './views/model/index.vue'
import modelVariable from './views/model/modelVariable.vue' ;
import packageDiagram from './views/model/packageDiagram.vue'
import packageDiagramModel from './views/model/packageDiagramModel.vue'
import packageDiagramVariable from './views/model/packageDiagramVariable.vue'
import modelComponent from  './views/model/ModelComponent.vue'
import modelCont from './views/model/modelCont.vue'
import packageTransfModel from  './views/model/packageTransfModel.vue'





import template from './views/review/template.vue'
import setCheckors from './views/review/setCheckors.vue'
import mySubmit from './views/review/mySubmit.vue'
import mySubmitDetail from './views/review/mySubmitDetail.vue'
import checkor from './views/review/checkor.vue'
import Myorg from './views/nav4/Myorg.vue'
import OrgEdit from './views/nav4/OrgEdit.vue'
import GUser from './views/nav4/GUser.vue'
import TreeGrid from './views/nav4/TreeGrid.vue'
import test from './views/nav4/model.vue'
import tree from './views/nav4/tree.vue'
import log from   './views/nav4/log.vue'
import permission from './views/nav4/permission.vue'
import role from './views/nav4/role.vue'
import test2 from './views/nav4/test2.vue'
import team from './views/nav4/team.vue'
import OrgMange from './views/nav4/OrgMange.vue'
import error401 from './views/401.vue'
import error404 from './views/404.vue'

import Vue from 'vue'
import Router from 'vue-router'

/**
Vue.use(Router)



export let  commonRoutes = [
    {
        path: '',
        component: Home,
        name: '审签管理',
        iconCls: 'el-icon-setting',//图标样式class
        children: [
            // { path: '/main', component: Main, name: '项目', hidden: true },
            { path: 'setCheckors', component: setCheckors, name: '审核人员配置',hidden: true },
            { path: 'mySubmitDetail', component: mySubmitDetail, name: '我的提交详情',hidden: true },
        ]
    },
    {
        path: '',
        component: Home,
        name: '我的审签',
        iconCls: 'el-icon-star-on',//图标样式class
        children: [
            // { path: '/main', component: Main, name: '项目', hidden: true },
            {path:'template',component:template,name:'我的模板'},
            {path:'checkor',component:checkor,name:'我的待办'},
            {path:'mySubmit',component:mySubmit,name:'我的提交'},
        ]
    },
    {
        path: '/login',
        component: Login,
        name: '',
        hidden: true
    },
    {
        path: '/404',
        component: NotFound,
        name: '',
        hidden: true
    },
    //{ path: '/main', component: Main },
    {
        path: '',
        component: Home,
        name: '我的项目',
        iconCls: 'el-icon-menu',
        leaf: true,//只有一个节点
        noDropdown: true,
        children: [
            { path: 'repository/index', component: Repository, name: '我的项目' }
        ]
    },
    {
        path: '',
        component: Home,
        name: '模型',
        iconCls: 'el-icon-document',
     //   leaf: true,//只有一个节点
        noDropdown: true,
        children: [
            { path: 'model/index', component: Model, name: '我的模型' },
            { path: 'model/packageTransfModel', component: packageTransfModel, name: '包转模型'},
            { path: 'model/modelCont', component: modelCont, name: '模型信息'},
            // { path: '/model/modelVariable', component: modelVariable, name: '模型参数' },
            { path: 'model/packageDiagram', component: packageDiagram, name: '包视图'},
            { path: 'model/packageDiagramModel', component: packageDiagramModel, name: '包视图组件信息' },
            { path: 'model/packageDiagramVariable', component: packageDiagramVariable, name: '包视图组件参数信息',
                iconCls: 'el-icon-document',
                leaf: true,//只有一个节点
                children: [
                    { path: 'model/ModelComponent', component: modelComponent, name: '模型组件树信息' },
                    ]
            },
            // { path: '/model/ModelComponent', component: modelComponent, name: '模型组件树信息' },
        ]
    },
    {
        path: '',
        component: Home,
        name: '仿真',
        iconCls: 'fa fa-address-card',
        leaf: true,//只有一个节点
        noDropdown: true,
        children: [
            // { path: '/page6', component: Page6, name: '我的仿真' }
            { path: 'nav3/index', component: FileContent, name: '我的仿真' }
        ]
    },
    {
        path: '',
        component: Home,
        name: '分享',
        noDropdown: true,
        iconCls: 'el-icon-share',
        leaf: true,//只有一个节点
        children: [
            { path: 'page4', component: Page4, name: '我的分享' }
        ]
    },
    {
        path: '收藏',
        component: Home,
        name: '我的收藏',
        noDropdown: true,
        iconCls: 'el-icon-star-on',//图标样式class
        children: [
            // { path: '/main', component: Main, name: '项目', hidden: true },
            { path: 'table', component: Table, name: '项目' },
            { path: 'form', component: Form, name: '模型' },
            { path: 'user', component: user, name: '仿真' },
        ]
    },
    {
        path: '',
        component: Home,
        name: '回收站',
        iconCls: 'el-icon-delete',
        leaf: true,//只有一个节点
        noDropdown: true,
        children: [
            { path: 'page6', component: Page6, name: '回收站' }
        ]
    },
      {
        path: '',
        component: Home,
        name: '组织',
        iconCls: 'el-icon-delete',
        leaf: true,//只有一个节点
        noDropdown: true,
        children: [
            { path: 'Myorg', component: Myorg, name: '我的组织'},
            { path: 'Myorg/:orgName', component: OrgMange, name: '组织管理',hidden:true},
            { path: 'Myorg/:orgName/:teamName', component: team, name: '团队管理',hidden:true}
        ]
    },
    { path: '/404', component: error404, hidden: true },
    { path: '/401', component: error401, hidden: true }
   
   
]


export let limitRoutes=[
 {
        path: '',
        component: Home,
        name: '系统管理',
        iconCls: 'el-icon-setting',//图标样式class
        children: [
            // { path: '/main', component: Main, name: '项目', hidden: true },
         //   { path: 'test2', component: test2, name: '项目管理' },
            { path: 'tree', component: tree, name: '组织管理' ,meta:{role:['management_org_list']}},
          //  { path: 'TreeGrid', component: TreeGrid, name: '用户管理' ,meta:{role:['management_user_list']}},
            { path: 'GUser', component: GUser, name: '用户管理' ,meta:{role:['management_user_list']}},
            { path: 'role', component: role, name: '角色管理' ,meta:{role:['management_role_list']}},
            { path: 'permission', component: permission, name: '权限管理' ,meta:{role:['management_auth_list']} },
         //   { path: 'user', component: user, name: '应用监控' },
            { path: 'log', component: log, name: '日志管理' ,meta:{role:['management_log_list']}},
            { path: 'tree/:orgName', component: OrgMange, name: '组织管理',hidden:true},
            { path: 'tree/:orgName/:teamName', component: team, name: '团队管理',hidden:true}
        ]
    },
    {
        path: '*',
        hidden: true,
        redirect: { path: '/404' }
    }
]



 export default new Router({
  // mode: 'history', //后端支持可开
  scrollBehavior: () => ({ y: 0 }),
  routes: commonRoutes
})

**/
//export default commonRoutes;