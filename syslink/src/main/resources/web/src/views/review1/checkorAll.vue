<template>
	<section>
		<p>待审批流程列表</p>
		<checkor></checkor>
		<p>其他审批流程记录</p>
		<!--除了正在审签之外的列表-->
		<el-table :data="detail" highlight-current-row v-loading="listLoading" @selection-change="selsChange" style="width: 100%">
			<el-table-column type="index" width="60" >
			</el-table-column>
			<el-table-column prop="model.name" label="模型名" width="100" >
			</el-table-column>
			<el-table-column prop="template.templateName" label="模板名" width="100" >
			</el-table-column>
			<el-table-column prop="instanceName" label="审签实例名" width="150" >
			</el-table-column>

			<el-table-column prop="reviewNodeInstance.status" label="审签状态" width="150" :formatter="format">
			</el-table-column>
		</el-table>

		<!--工具条-->
        <el-col :span="24" class="toolbar">
            <el-pagination layout="total,sizes,prev, pager, next" @current-change="handleCurrentChange"
            @size-change="handleSizeChange"  :total="total" :page-sizes ="pageSizes" :page-size="pageSize" style="float:right;">
            </el-pagination>
        </el-col>

	</section>
</template>

<script>
	import checkor from '../review/checkor'
	export default{
		components:{
			checkor,
		},
		data(){
			return {
				total: 0,
                pageSize: 10,
                pageSizes: [10,20,30],
                page: 1,
                pages: 0,
                total: 0,

				detail:[],
				listLoading:false,
				sels: [],

			}
		},
		methods: {
			getDetail(){
				this.listLoading = true;
				let url = "api/checkor/queryAllByReviewer";
				let params = {
					page:this.page,
					rows: this.pageSize,
				}
				this.func.ajaxPost(url,params,res=>{
					this.pages = res.data.pages;
                    this.total = res.data.total;
					this.detail = res.data.records;
					this.listLoading = false;
				})
			},
			handleCurrentChange(val){
                this.page = val;
                this.getInstance();
            },
            handleSizeChange(val) {
                this.pageSize = val;
                this.getInstance();
            },
			selsChange(sels){
				this.sels = sels;
			},
			handleAgree(index,row){
				let url="/api/checkor/agree";
				this.func.ajaxPost(url,{id:row.id},res=>{
					if(res.data.flag==true){
						this.$message({
							message:'操作成功！'
						})
						this.getDetail();
					}
				})
			},
			handleDisagree(index,row){
				let url="/api/checkor/disagree";
				this.func.ajaxPost(url,{id:row.id},res=>{
					if(res.data.flag==true){
						this.$message({
							message:'操作成功！'
						})
						this.getDetail();
					}
				})
			},
			format: function(row,column){
				let status = row.reviewNodeInstance.status;
				let msg = '';
				if(status==1){
				 msg="未进入审核状态";
				}else if(status ==2){
				 msg = "等待审核";
				}else if(status == 3){
				 msg = "已同意";
				}else if(status ==4){
				 msg ="已拒绝";
				}
				return msg;
			},

		},
		mounted() {
			this.getDetail();
		}
	}
</script>

<style scoped>
	
</style>

