<template>
  <section>
    <!--工具条-->
    <!-- <el-col :span="24" class="toolbar" style="padding-bottom: 0px;"> -->
      <el-form :inline="true" >
        <el-form-item>
          <el-button size="small" type="primary" @click="handleAdd"  >新建</el-button>
          <el-button size="small"  @click="handleEdit">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel">删除</el-button>
        </el-form-item>
      </el-form>
       
    <!-- </el-col> -->
    <hr/>

    <!--列表-->
    <el-table :data="roles" highlight-current-row       style="width: 100%;">
      <el-table-column type="selection" width="55">
      </el-table-column>

      <el-table-column type="index" width="60">
      </el-table-column>
      <el-table-column prop="name" label="名称" min-width="120" sortable>
      </el-table-column>
      <el-table-column prop="createDate" label="创建时间" min-width="160" sortable  >
       </el-table-column>
      <el-table-column prop="description" label="描述" min-width="160" sortable  >
       </el-table-column>
      <el-table-column label="操作" width="200">
        <template slot-scope="scope">
         <el-button size="small" type="primary"  @click="handlePermission(scope.$index, scope.row)">分配权限</el-button>
        </template>
      </el-table-column>
    </el-table>

   

    <!--编辑角色界面-->
    <el-dialog title="编辑角色" :visible="editFormVisible" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="80px" ref="editForm"    >
        <el-form-item label="名称" prop="name"  :rules="[{required:true,message:'请输入角色名称',trigger:'blur'}]"  >
          <el-input v-model="editForm.name" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="描述" prop="description"   >
          <el-input   type="textarea"   :autosize="{ minRows: 4, maxRows: 10}"  placeholder="请添加相关描述" v-model="editForm.description" auto-complete="off"></el-input>
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="editSubmit" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>
  

    <!--新增角色界面-->
    <el-dialog title="新建角色" :visible="addFormVisible" :close-on-click-modal="false"  >
      <el-form :model="addForm" label-width="80px"  ref="addForm"    >
        <el-form-item label="名称" prop="name"  :rules="[{required:true,message:'请输入角色名称',trigger:'blur'}]"  >
          <el-input v-model="addForm.name" auto-complete="off"></el-input>
        </el-form-item>
         <el-form-item label="描述" prop="description" >
          <el-input v-model="addForm.description" type="textarea"  :autosize="{ minRows: 4, maxRows: 10}"  placeholder="请添加相关描述"  auto-complete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false">取消</el-button>
        <el-button type="primary" @click.native="addSubmit" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>




     <!--分配权限界面-->
   <el-dialog title="分配权限" :visible="permissionVisible" ref="permissionDialog" :close-on-click-modal="false" :show-close="false" >
   <el-form :model="permission" label-width="80px"  ref="permissionForm"    >
 <!--    <div slot="title">
      <span>分配权限</span> -->
       <el-tree :data="data2" show-checkbox  node-key="authId"  ref="tree"  highlight-current :props="defaultProps">
    </el-tree>

<!--     </div> -->
   
      </el-form>
    <div slot="footer" class="dialog-footer">
        <el-button @click.native="permissionVisible=false">取消</el-button>
        <el-button type="primary" @click.native="permissionSubmit" :loading="permissionLoading">提交</el-button>
    </div>
    </el-dialog>
   
  </section>
</template>

<script>
  import util from '../../common/js/util'
  const   cityOptions=[9,10];
  //import NProgress from 'nprogress'
  export default {
    data() {
       
      return {

        data2:[],
           defaultProps: {
          children: 'children',
          label: 'authName'
        },

       auths:[],    
        ids:{
        authIds:[],
        roleId:''
        },
        
        addItem:{
          authName:'',
          authCode:'',
          description:''
        },
        editItem:{
          authName:'',
          authCode:'',
          description:''
        },
        groups:[],
     
        roles:[],
        listLoading: false,
        sels: [],//列表选中列
        perCheck:[],//权限列表选中列

        editFormVisible: false,//编辑界面是否显示
        editLoading: false,
        addItemLoading:false,
        addItemVisible:false,
        dialogVisible:false,
        editItemLoading:false,
        editItemVisible:false,

        permissionVisible:false,
        permissionLoading:false,
       
        //编辑界面数据
        editForm: {
     //     id: 0,
          name: '',
          description: '',
          
        },
        permission:{},
      
        addFormVisible: false,//新增界面是否显示
        addLoading: false,
       
        //新增界面数据
        addForm: {
         name:'',
         description:''

        }
        ,   
      }
    },
    methods: {

      getCheckedNodes() {
        return this.$refs.tree.getCheckedNodes();
      },
      setCheckedNodes(nodes) {
         this.$refs.tree.setCheckedNodes(nodes);
      },
 
      getRoles() {
         var _this = this;
          _this.$http.post('/api/role/list')
              .then(function (response) {
                  _this.roles = response.data.role;
                 
              
          })
          .catch(function (error) {
     //         console.log(error);
          });
      },
      getGroups() {
         var _this = this;
          _this.$http.post('/api/auth/list')
              .then(function (response) {
                  _this.groups = response.data.group;
                  _this.data2  = response.data.group;
               


          })
          .catch(function (error) {
              console.log(error);
          });
      },

 
      //删除
      handleDel: function () {
        this.$confirm('确认删除该角色吗?', '提示', {
          type: 'warning'
        }).then(() => {
          this.listLoading = true;
          //NProgress.start();
          let  a = this.sels;
          let b = a.length;
      //    console.log(b);
          let para = { id: a[b-1].id};
          this.$http({
            url:'/api/role/delete',
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
            this.getRoles();
          });
        }).catch(() => {

        });
      },
     
      //显示编辑界面
      handleEdit: function () {
        this.editFormVisible = true;
        let a = this.sels;
        let b = a.length;

        this.editForm = Object.assign({}, this.sels[this.sels.length-1]);
     //   console.log(this.sels);
       // console.log(this.editForm);
      
      },
    
      //显示新增界面
      handleAdd: function () {
        this.addFormVisible = true;
        this.addForm = {
         name:'',
         description:''
          
        };
      },

      handlePermission(index,row)
      {

         this.ids.roleId=row.id;
         this.permissionVisible=true;
          console.log( this.$refs.tree);
          this.$refs.tree.setCheckedNodes(row.permissions);
      //   this.setCheckedNodes(row.permissions);
       
      },
      //编辑
      editSubmit: function () {
        this.$refs.editForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.editLoading = true;
              //NProgress.start();
              let para = Object.assign({}, this.editForm);
          
              this.$http({
                url:'/api/role/edit',
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
                this.getRoles();
              });
            });
          }
        });
      },
       permissionSubmit: function () {


        this.$refs.permissionForm.validate((valid) => {
          if (valid) {
        
              this.permissionLoading = true;
  
              this.ids.authIds=this.getCheckedNodes();
              let para = Object.assign({}, this.ids);
   
              this.$http({
                url:'/api/role/assign',
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
                this.$refs['permissionForm'].resetFields();
                this.permissionVisible = false;
                this.getGroups();
                this.getRoles();
              });
           
          }
        });
      },
      //新增
      addSubmit: function () {
        this.$refs.addForm.validate((valid) => {
          if (valid) {
            this.$confirm('确认提交吗？', '提示', {}).then(() => {
              this.addLoading = true;
              //NProgress.start();
              let para = Object.assign({}, this.addForm);
              
              this.$http({method:'post',
                url:'api/role/add',
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
                this.getRoles();
              });
            });
          }
        });
      }
    
    
    },
    mounted() {
      this.getRoles();
   //   this.getCheckBox();
      this.getGroups();
      console.log(this.$refs.permissionDialog);
  
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
