<template >
   <section>
   

     <!--工具条-->
   <!--  <el-col :span="24" class="toolbar" style="padding-bottom: 0px;"> -->
      <el-form :inline="true" >
        <el-form-item>
          <el-button type="primary" @click="handleAdd" size="small" >新建</el-button>
        </el-form-item>
      </el-form>
      <hr />
       
    <!-- </el-col> -->
    <zk-table
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
      :selection-type="true"
      >
       <template slot="operation" slot-scope="scope">
     <el-button size="small"   @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
     <el-button size="small" type="danger"  @click="handleDel(scope.$index, scope.row)">删除</el-button>
       </template>
    </zk-table>



    <!--编辑部门界面-->
    <el-dialog title="编辑部门" v-model="editFormVisible" :close-on-click-modal="false">
        <el-form :model="editForm" label-width="80px" ref="editForm"    >
        <el-form-item label="名称" prop="name"  :rules="[{required:true,message:'请输入部门名称',trigger:'blur'}]"  >
          <el-input v-model="editForm.name" auto-complete="off"></el-input>
        </el-form-item>

         <el-form-item label="父级部门" prop="parentId"   >
         <el-cascader    :options="options"  :props="props"  @change="handleChange" v-model="editForm.parentId"   change-on-select   :show-all-levels="false">
        </el-cascader>
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
  

    <!--新增部门界面-->
    <el-dialog title="新建部门" v-model="addFormVisible" :close-on-click-modal="false"  >
      <el-form :model="addForm" label-width="80px"  ref="addForm"    >
        <el-form-item label="名称" prop="name"  :rules="[{required:true,message:'请输入部门名称',trigger:'blur'}]"  >
          <el-input v-model="addForm.name" auto-complete="off"></el-input>
        </el-form-item>
            
         <el-form-item label="父级部门" prop="parentId"   >
         <el-cascader    :options="options"  :props="props"  @change="handleChange" v-model="addForm.parentId"   change-on-select   :show-all-levels="false">
        </el-cascader>
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


</section>
</template>

<script>
  export default {
    data() {
      return {
     //   data:[],
        props:{value:'id',label:'name',children:'children'},
        departs:[],
        options:[],
        selectedOptions:[],
        addFormVisible: false,//新增界面是否显示
        addLoading: false,
        addForm:{

        },
        editFormVisible:false,
        editLoading:false,
        editForm:{

        },

        columns: [
          {
            label: '名称',
            prop: 'name',
           minWidth: '300px',
         
          },
          {
            label: '父部门',
            prop: 'parentName',
            minWidth: '100px',
          },
          {
            label: '描述',
            prop: 'description',
             minWidth: '200px'
          },
          {
            label: '创建时间',
            prop: 'createDate',
            minWidth: '150px'
          },
          {
            label:'操作',
            prop:'operation',
            minWidth:'200px',
            type:'template',
            template:'operation',
          }
        ]
      }
    },
   
    methods: {

        handleChange(value) {
        console.log(value);
      },
        //显示编辑界面
      handleEdit(index,row) {
        this.editFormVisible = true;
        console.log(row);
        this.editForm = Object.assign({}, row);
        let a = this.$refs.table.getCheckedProp('id');
        console.log(a);
     //   console.log(this.sels);
       // console.log(this.editForm);
      
      },
    
      //显示新增界面
      handleAdd: function () {
        this.addForm = {};
        this.addFormVisible = true;
        
      },
      addSubmit: function () {
        this.$refs.addForm.validate((valid) => {
          if (valid) {
  
              this.addLoading = true;
              //NProgress.start();
            
              let para = Object.assign({}, this.addForm);
              para.parentId=para.parentId[para.parentId.length-1];
              this.$http({method:'post',
                url:'api/depart/add',
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
              
              });
                this.getDeparts();
          }
        });
      },
        editSubmit: function () {
        this.$refs.editForm.validate((valid) => {
          if (valid) {
           
              this.editLoading = true;
              //NProgress.start();
              let para = Object.assign({}, this.editForm);
              para.parentId = para.parentId[para.parentId.length-1];
              this.$http({
                url:'/api/depart/update',
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
                this.getDeparts();
              });
          
          }
        });
      },
      handleDel(index,row){
            let para = {id :''};
            para.id = row.id;
            this.$http({method:'post',url:'api/depart/delete',data:para})
            .then((res)=>{
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
                this.getDeparts();
            })
       },
      getDeparts()
      {
          
          var _this = this;
          _this.$http.post('api/depart/list')
              .then(function (response) {

                _this.departs = response.data.depart;
                _this.options = response.data.depart;
            

          })
          .catch(function (error) {
              console.log(error);
          });
      }

     

     

    },
      mounted() {
      this.getDeparts(); 
    }
  }
</script>

<style scoped >
 
</style>