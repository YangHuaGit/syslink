<template>
  <section>

<!--工具条-->




   <!--  <el-col :span="24" class="toolbar" style="padding-bottom:0px;"> -->
  
      <el-form :inline="true" :model="filters" >
      
        <el-form-item>
          <el-input v-model="filters.name" placeholder="用户名/全名/邮箱"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" v-on:click="getUsers">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="small" v-on:click="reset">重置</el-button>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" size="small" @click="handleAdd"  >新增</el-button>
        </el-form-item>

      </el-form>
      <hr/>
     
  <!--   </el-col> -->
  <el-row :gutter="20">
  <el-col :span="6">
   


   <!-- <zk-table
      ref="table"
      sum-text="sum"
      index-text="#"
      :data="departs"
      :columns="columns"
      :stripe="false"
      :border="false"
      :show-header="true"
      :show-summary="false"
      :show-row-hover="true"
      :show-index="false"
      :tree-type="true"
      :is-fold="false"
      :expand-type="false"
      :selection-type="false"
      @row-click="handleDepart"
      >
     </zk-table> -->
      <el-tree :data="data4" :props="defaultProps1"   @node-click="queryByDepart" ref="tree2"></el-tree>



  <div class="grid-content bg-purple"></div></el-col>
  <el-col :span="18"><div class="grid-content bg-purple">


    <!--列表-->
    <el-table :data="users" highlight-current-row  center  @selection-change="selsChange" style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>
          
      <el-table-column type="expand">
    
      <template slot-scope="props">
      
        <el-form label-position="left" inline class="demo-table-expand">

          <el-form-item label="用户名">
            <span>{{ props.row.userName }}</span>
          </el-form-item>
          <el-form-item label="姓名">
            <span>{{ props.row.realName }}</span>
          </el-form-item>
          <el-form-item label="性别"  >
            <span>{{ props.row.gender }}</span>
          </el-form-item>
          <el-form-item label="创建人">
            <span>{{ props.row.createUser }}</span>
          </el-form-item>
          <el-form-item label="所属部门">
            <span>{{ props.row.departmentName }}</span>
          </el-form-item>
          <el-form-item label="创建时间">
            <span>{{ props.row.createDate }}</span>
          </el-form-item>
          <el-form-item label="最后修改时间">
            <span>{{ props.row.updateDate }}</span>
          </el-form-item>
          <el-form-item label="状态">
            <span>{{ props.row.status }}</span>
          </el-form-item>
           <el-form-item label="手机">
            <span>{{ props.row.mobile }}</span>
          </el-form-item>
           <el-form-item label="邮箱">
            <span>{{ props.row.email }}</span>
          </el-form-item>
        </el-form>
      
      </template>

    </el-table-column>
      
      <el-table-column prop="id" label="ID" width="80" sortable>
      </el-table-column>
      <el-table-column prop="name" label="用户名" min-width="120" sortable>
      </el-table-column>
       <el-table-column prop="departName" label="部门" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="email" label="邮箱" min-width="120" sortable>
     
       </el-table-column>
   <!--   <el-table-column prop="isActive"  label="已激活" min-width="120" sortable>
       <template  slot-scope="props">
         <i v-if=props.row.isActive class="el-icon-check"></i>
         <i v-else=props.row.isActive class="el-icon-close"></i>
      </template>
       </el-table-column> -->
   <!--     <el-table-column prop="isAdmin"  label="管理员" min-width="120" sortable>
        <template  slot-scope="props">
         <i v-if="props.row.isAdmin" class="el-icon-check"></i>
         <i v-else class="el-icon-close"></i>
      </template> -->

     
        <el-table-column prop="prohibitLogin"  label="允许登录" min-width="120" sortable>
       <template  slot-scope="props">
         <i v-if=!props.row.prohibitLogin class="el-icon-check"></i>
         <i v-else class="el-icon-close"></i>
      </template>


       </el-table-column>
     <!--  <el-table-column prop="numRepos" label="仓库数" min-width="160" sortable  >
       </el-table-column> -->
     


      <el-table-column label="操作" width="300">
        <template slot-scope="scope">

        <el-dropdown>
  <el-button type="primary" size="small">
    选项<i class="el-icon-caret-bottom el-icon--right"></i>
  </el-button>
  <el-dropdown-menu slot="dropdown">
    <el-dropdown-item >  <el-button  type="text"  @click="handleEdit(scope.$index, scope.row)">编辑</el-button></el-dropdown-item>
    <el-dropdown-item >  <el-button  type="text" size="small" @click="handleDel(scope.$index, scope.row)" >删除</el-button></el-dropdown-item>
    <el-dropdown-item >  <el-button  type="text" size="small" @click="handleRole(scope.$index, scope.row)">分配角色</el-button></el-dropdown-item>
    <el-dropdown-item >  <el-button  type="text" size="small" @click="handleAuth(scope.$index, scope.row)">分配权限</el-button></el-dropdown-item>
     <el-dropdown-item >  <el-button  type="text" size="small" @click="modelAuth(scope.$index, scope.row)">模型访问控制</el-button></el-dropdown-item>  
  </el-dropdown-menu>
  </el-dropdown>

   
         
        </template>
      </el-table-column>
    </el-table>

    <!--工具条-->
    <el-col :span="24" class="toolbar">
     
      <el-pagination  
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="pager.pageIndex"
      :page-sizes="[10, 30, 50, 100]"

      :page-size="pager.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="pager.total">
    </el-pagination>
     <el-button type="danger" size="small" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
    </el-col>

  </div></el-col>
</el-row>

    

    <!--编辑界面-->
    <el-dialog title="编辑用户信息" :visible="editFormVisible" :close-on-click-modal="false"  >
        <el-form :model="editForm" label-width="100px" :rules="addFormRules" ref="editForm"    >
        <el-form-item label="用户名" prop="name"   >
          {{editForm.name}}
        </el-form-item>

         <el-form-item label="部门" prop="departId"   >
         <el-cascader    :options="options"  :props="props"  @change="handleChange" v-model="editForm.departId"   change-on-select   :show-all-levels="false">
        </el-cascader>
        </el-form-item>
        
        <el-form-item label="自定义名称" prop="fullName"   >
            <el-input v-model="editForm.fullName" auto-complete="off"></el-input>
        </el-form-item>
          <el-form-item label="邮箱" prop="email"  :rules="[ { required:'true',type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' } ]"  >
          <el-input v-model="editForm.email" auto-complete="off" ></el-input>
        </el-form-item>
         <el-form-item label="密码" prop="fullName"   >
         <el-tooltip content="将值留空使其保持不变。" placement="top" effect="light">
            <el-input v-model="editForm.passwd" auto-complete="off"></el-input>
            </el-tooltip>
        </el-form-item>
        <el-form-item label="个人网站" prop="website"   >
            <el-input v-model="editForm.website" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label="所在地区" prop="location"   >
            <el-input v-model="editForm.location" auto-complete="off"></el-input>
        </el-form-item>
        <el-form-item label-width="150px" label="允许创建最大库数量" prop="maxRepoCreation">
             <el-input-number v-model="editForm.maxRepoCreation"  :min="-1" ></el-input-number>
             <label>  （设置为 -1 表示使用全局默认值）</label>
        </el-form-item>
        <el-form-item  label-width="20px"  prop="isActive">
         <el-checkbox v-model="editForm.isActive">该用户已被激活</el-checkbox>
        
        </el-form-item>
         <el-form-item  label-width="20px"  prop="prohibitLogin">
          <el-checkbox v-model="editForm.prohibitLogin">该账户被禁止登录</el-checkbox>
        </el-form-item>
         <el-form-item  label-width="20px" prop="isAdmin">
          <el-checkbox v-model="editForm.isAdmin">该用户具有管理员权限</el-checkbox>
        </el-form-item>
         <el-form-item label-width="20px"  prop="allowGitHook">
          <el-checkbox v-model="editForm.allowGitHook">该用户具有创建Git钩子的权限</el-checkbox>
        </el-form-item>
         

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>

    <!--新增界面-->
    <el-dialog title="新增" :visible="addFormVisible" :close-on-click-modal="false"   >
      <el-form :model="addForm" label-width="80px" :rules="addFormRules" ref="addForm"   >
        <el-form-item label="用户名" prop="name"  :rules="[{required:true,message:'请输入用户名',trigger:'blur'}]">
          <el-input v-model="addForm.name" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="密码" prop="passwd">
      <el-input type="password" v-model="addForm.passwd" auto-complete="off"  ></el-input>
     </el-form-item>
     <el-form-item label="确认密码" prop="checkPass">
     <el-input type="password" v-model="addForm.checkPass" auto-complete="off" ></el-input>
     </el-form-item>
      <el-form-item label="邮箱" prop="email"    :rules="[ { required:true, type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur,change' }]"  >
        <el-input v-model="addForm.email" auto-complete="off" ></el-input>
        </el-form-item>
       <el-form-item label="部门" prop="departId"   >
         <el-cascader    :options="options"  :props="props"  @change="handleChange" v-model="addForm.departId"   change-on-select   :show-all-levels="false">
        </el-cascader>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
    <el-dialog title="分配角色" :visible="roleVisble" :close-on-click-modal="false">


     <el-transfer :data="roles"  v-model="userRole.assigned"
      :button-texts="['移除', '分配']" :titles="['未分配', '已分配']"
     ></el-transfer>
     <div slot="footer" class="dialog-footer">
        <el-button @click.native="roleVisble = false">取消</el-button>
        <el-button type="primary" @click.native="roleSubmit" :loading="roleLoading">提交</el-button>
      </div>


    </el-dialog>

     <!--分配权限界面-->
    <el-dialog title="分配权限" :visible="permissionVisible"  :close-on-click-modal="false" :show-close="false"   >
      <!-- <div slot="title"> -->
   <el-form :model="permission" label-width="100px"  ref="permission"    >

  <el-tree :data="data2" show-checkbox  node-key="authId"  ref="tree"   highlight-current :props="defaultProps">
  </el-tree>

      </el-form>
   <!--  </div> -->
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="permissionVisible=false">取消</el-button>
        <el-button type="primary" @click.native="permissionSubmit" :loading="permissionLoading">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="模型访问控制" :visible="modelVisible" :close-on-click-modal="false" :show-close="false"   >

 <!--    <div slot="title">    -->
    <el-form :model="directory" label-width="100px"  ref="directoryForm"    >

    <el-tree :data="data3" show-checkbox  node-key="id"  
    :default-expanded-keys="[0]" ref="tree1"  highlight-current :props="defaultProps1">
    </el-tree>

    </el-form>
    <!-- </div> -->
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="modelVisible=false">取消</el-button>
        <el-button type="primary" @click.native="modelAuthSubmit" :loading="modelLoading">提交</el-button>
      </div>

    </el-dialog>




  </section>
</template>

<script>
  import util from '../../common/js/util'
  //import NProgress from 'nprogress'
  import { getUserListPage } from '../../api/api';

  export default {
    data() {
       
       var validatePass = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请输入密码'));
        } else {
          if (this.addForm.checkPass !== '') {
            this.$refs.addForm.validateField('checkPass');
          }
          callback();
        }
      };
      var validatePass2 = (rule, value, callback) => {
        if (value === '') {
          callback(new Error('请再次输入密码'));
        } else if (value !== this.addForm.passwd) {
          callback(new Error('两次输入密码不一致!'));
        } else {
          callback();
        }
      };
      return {
        props:{value:'id',label:'name',children:'children'},
        options:[],
        columns: [
          {
           label: '部门名称',
           prop: 'name',
           minWidth: '300px',
           }
         
        ],
        departs:[],
        data4:[],
       
        data2:[],
        defaultProps: {
        children: 'children',
        label: 'authName'
        },

       auths:[],    

        permission:{},
      
        props: {
          label: 'name',
          children: 'children',
          value:'id'

        },

         ids:{
          authIds:[],
          uid:'',
          roleId:'',
          directoryIds:[]
        },
        permissionVisible:false,
        permissionLoading:false,

        search:{
          label:'realName',
          value:'realName'
        },
         pager:{
          total:1,
          pageSize:10,
          pageIndex:1,
          currentPage:''
        },
        filters: {
          name: '',
          departId:''
        },
        state1:'',
       userSearch:[],
        abc:{
          a:true,
          b:true,
        },
        users: [],

        listLoading: false,
        sels: [],//列表选中列
       
        editFormVisible: false,//编辑界面是否显示
        editLoading: false,
        editFormRules: {
          name: [
            { required: true, message: '请输入姓名', trigger: 'blur' }
          ]
        },
        //编辑界面数据
        editForm: {
         fullName:'',
         email:'',
         website:'',
         location:'',
         passwd:'',
         maxRepoCreation:'',
         isActive:'',
         isAdmin:'',
         prohibitLogin:'',
         allowGitHook:''
          
        },

        addFormVisible: false,//新增界面是否显示
        addLoading: false,
      
        //新增界面数据
        addForm: {
          name: '',
          email: '',
          passwd:'',
          checkPass: '',
          departId:''
         

        },
        roleVisble:false,
        roleLoading:false,
        roles:[],
        userRole:{
           assigned:[],
           uid:''
        }
        ,
        addFormRules: {
      
          passwd: [
            { required: true, validator: validatePass, trigger: 'blur' }
          ],
          checkPass: [
            {  required: true,validator: validatePass2, trigger: 'blur' }
          ]
          
        },
        directory:{},
        data3:[],
        modelVisible:false,
        modelLoading:false,
        model:false,
        defaultProps1:{
          label:'name',
          children:'children'
        }

      }
    },
    methods: {

      handleChange(value) {
        console.log(value);
      },

      getCheckedNodes() {
        return this.$refs.tree.getCheckedNodes();
      },
      setCheckedNodes(nodes) {
         this.$refs.tree.setCheckedNodes(nodes);
      },

      getUserRoles(para){

        this.$http({
          url:'/api/user/queryUserRoles',
          method:'post',
          data:para
        }).then((res)=>{
          if(res.data.flag)
          {
           this.userRole.assigned = res.data.userRoles;
          }
        })
        .catch(error=>{
          console.log(error);
        })
      },
     // 获取角色穿梭框数据
     getRoles() {
         var _this = this;
          _this.$http.post('/api/role/list')
              .then(function (response) {
                let a = response.data.role;

                for(let i = 0;i<a.length;i++)
                {

                  _this.roles.push({
                    key:a[i].id,
                    label:a[i].name
                  })
                  // _this.roles = response.data.role;
                }

          })
          .catch(function (error) {
              console.log(error);
          });
      },
      reset(){
           this.filters={};
           this.getUsers();
      },
       handleCurrentChange(val) {
        console.log(val);
        this.pager.pageIndex = val;
        this.getUsers();
      },
      handleSizeChange(val){
          console.log(val);
        this.pager.pageSize = val;
        this.getUsers();
      },
      searchSelect(){
         this.getUsers();

      },
        querySearch(queryString, cb) {
        var userSearch = this.userSearch;
        var results = queryString ? userSearch.filter(this.createFilter(queryString)) : userSearch;
        console.log(results);
        // 调用 callback 返回建议列表的数据
        cb(results);
      },
      createFilter(queryString) {
        return (user) => {
          return (user.realName.indexOf(queryString.toLowerCase()) === 0);
        };
      },
    
      

      permissonJudge(){
            var _this = this;
        _this.$http.post('/api/user/if')
        .then(function(response){
          _this.abc.a=response.data.flag;
        }) 
        .catch(function (error) {
              console.log(error);
          });
      },
  
      //获取用户列表
      getUsers() {
         var _this = this;
         let para = Object.assign({},_this.filters,_this.pager);
          _this.$http({method:'post',
            url:'/api/user/query',
            data:para})
              .then(function (response) {
                  _this.users = response.data.users;
                  _this.pager.total=response.data.total;
              //    _this.userSearch=response.data.userName;

          })
          .catch(function (error) {
              console.log(error);
          });
      },
      getGroups() {
         var _this = this;
          _this.$http.post('/api/auth/list')
              .then(function (response) {
                  _this.data2  = response.data.group;
          })
          .catch(function (error) {
              console.log(error);
          });
      },

      //删除
      handleDel: function (index, row) {
        console.log(index);
        this.$confirm('确认删除该用户吗?', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = { id: row.id };
          this.$http({
            url:'/api/user/deleteGUser',
            method:'post',
            data:para
          }).then((res) => {

            this.listLoading = false;
            //NProgress.done();
            if(res.data.flag)
            {
               this.$message({
              message: '删除成功',
              type: 'success'
            });
            }
            else
            {
              this.$message({
              message: '删除失败',
              type: 'error'
              });
            }
           
            this.getUsers();
          });
        }).catch(() => {

        });
      },
      //显示编辑界面

      handleRole:function(index,row){
         this.userRole={
             assigned:[],
             uid:''
          }
         
      
        this.userRole.uid=row.id;
        let para = Object.assign({},this.userRole);
        this.getUserRoles(para);
        console.log(this.userRole.assigned);
        this.roleVisble=true;
       
      },
        handleAuth(index,row)
      {

         this.ids.uid=row.id;
         this.permissionVisible=true;
         this.setCheckedNodes(row.auths);
      },
      modelAuth(index,row)
      {
        console.log(row.modelAuth);
         this.ids.uid=row.id;
         this.modelVisible = true;
         this.$refs.tree1.setCheckedNodes(row.modelAuth);
      },
      //显示新增界面
      handleAdd: function () {
        this.addFormVisible = true;
        this.addForm = {
           
        };
      },
       handleEdit: function (index, row) {
        this.editFormVisible = true;
        this.editForm = Object.assign({}, row);
      },
      //编辑
      editSubmit: function () {
        this.$refs.editForm.validate((valid) => {
          if (valid) {
         
              this.editLoading = true;
              //NProgress.start();
              let para = Object.assign({}, this.editForm);
              para.departId=para.departId[para.departId.length-1];

           
              this.$http({
                url:'/api/user/updateGUser',
                method:'post',
                data:para
              })
               .then((res) => {
                this.editLoading = false;
                //NProgress.done();

                if(res.data.flag)
                {
                  this.$message({
                  message: '编辑成功',
                  type: 'success'
                });
                }
                else
                {
                   this.$message({
                  message: '编辑失败',
                  type: 'error'
                });
                }               
                this.$refs['editForm'].resetFields();
                this.editFormVisible = false;
                this.getUsers();
              });
            
          }
        });
      },
      //新增
      addSubmit: function () {
        this.$refs.addForm.validate((valid) => {
          if (valid) {
              this.addLoading = true;
              //NProgress.start();
              console.log(this.addForm);
              let para = Object.assign({}, this.addForm);      
              para.departId=para.departId[para.departId.length-1];
              console.log(para);
              this.$http({method:'post',
                url:'api/user/addGuser',
                data:para}).then((res)=>{
                this.addLoading = false;
                //NProgress.done();
                if(res.data.flag)
                {
                  this.$message({
                  message: res.data.msg,
                  type: 'success'
                });
                }
                else
                {
                  this.$message({
                  message: res.data.msg,
                  type: 'error'
                });

                }
                this.$refs['addForm'].resetFields();
                this.addFormVisible = false;
                this.getUsers();
              });        
          }
        });
      },
      roleSubmit:function(){
           
           
              this.roleLoading = true;
              //NProgress.start();
          //    let userId = thsi.sels.id;
          
              let para = Object.assign({},this.userRole);

              
              this.$http({method:'post',
                url:'api/user/assignRole',
                data:para}).then((res)=>{
                this.roleLoading = false;
                //NProgress.done();
                if(res.data.flag)
                {
                  this.$message({
                  message: res.data.msg,
                  type: 'success'
                });
                }
                else
                {
                  this.$message({
                  message: res.data.msg,
                  type: 'error'
                });

                }
           //     this.$refs['addForm'].resetFields();
                this.roleVisble = false;
                this.getUsers();
              });
           
          
      },


      permissionSubmit: function () {


        this.$refs.permission.validate((valid) => {
          if (valid) {
        
              this.permissionLoading = true;
  
              this.ids.authIds=this.getCheckedNodes();
              let para = Object.assign({}, this.ids);
   
              this.$http({
                url:'/api/user/assign',
                method:'post',
                data:para
              })
               .then((res) => {
                this.permissionLoading = false;
                //NProgress.done();
                if(res.data.flag)
                {
                  this.$message({
                  message: '编辑成功',
                  type: 'success'
                });
                }
                else
                {
                   this.$message({
                  message: '编辑失败',
                  type: 'error'
                });
                } 
                this.$refs['permission'].resetFields();
                this.permissionVisible = false;
                this.getGroups();
                this.getUsers();
              });
           
          }
        });
      },
       modelAuthSubmit: function () {


        this.$refs.directoryForm.validate((valid) => {
          if (valid) {
        
              this.modelVisible = true;
  
              this.ids.directoryIds=this.$refs.tree1.getCheckedNodes();
              console.log(this.ids.directoryIds);
              let para = Object.assign({}, this.ids);
   
              this.$http({
                url:'/api/user/modelAuth',
                method:'post',
                data:para
              })
               .then((res) => {
                this.modelLoading = false;
                //NProgress.done();
                if(res.data.flag)
                {
                  this.$message({
                  message: '编辑成功',
                  type: 'success'
                });
                }
                else
                {
                   this.$message({
                  message: '编辑失败',
                  type: 'error'
                });
                } 
                this.$refs['directoryForm'].resetFields();
                this.modelVisible = false;
                this.getUsers();
              });
           
          }
        });
      },

      selsChange: function (sels) {
        this.sels = sels;
      },
      //批量删除
      batchRemove: function () {
        var ids = this.sels.map(item => item.id);

        this.$confirm('确认删除选中记录吗？', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          //NProgress.start();
          let para = { ids: ids };
          this.$http({url:'/api/user/deleteGUsers',method:'post',data:para})
          .then((res) => {
            this.listLoading = false;
            //NProgress.done();
            if(res.data.flag)
            {
               this.$message({
              message: '删除成功',
              type: 'success'
            });
            }
            else
            {
              this.$message({
              message: '删除失败',
              type: 'error'
            });
            }
           
            this.getUsers();
          });
        }).catch(() => {

        });
      },
       getDeparts()
      {
          
          var _this = this;
          _this.$http.post('api/depart/list')
              .then(function (response) {

                _this.departs = response.data.depart;
                _this.options = response.data.depart;
                _this.data4 = response.data.depart;
            

          })
          .catch(function (error) {
              console.log(error);
          });
      },
       getDirectoryTree()
      {
          
          var _this = this;
          _this.$http.post('api/directory/getDirectoryTree')
              .then(function (response) {

                _this.data3 = response.data.directoryTree;
            
          })
          .catch(function (error) {
              console.log(error);
          });
      },
      handleDepart(row, rowIndex, $event)
      {
        console.log(row);
      //  console.log(rowIndex);
       console.log(rowIndex);
        console.log(event);
        console.log(event.target);
    

     //   this.getDeparts();
      },
      queryByDepart(node)
      {
         console.log(node);
         this.filters.departId=node.id;
      }



    },
    mounted() {
      this.getUsers();
      this.getRoles();
      this.getGroups();
      this.getDeparts();
      this.getDirectoryTree();
    //  this.func.changeDate();

  //    this.permissonJudge();
    }
  }



</script>


<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 120px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 20%;
  }
</style>
