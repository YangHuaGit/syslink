<template>
    <section>
    	<!-- <ul style="list-style-type:none">
	    	<li v-for="(item,index) in this.detailPages" v-if="item.status == 3">
	    	    <img src="../../assets/arrow.jpg" style="margin:0 auto;display:block;"><br><br>
	    	    <el-button type="primary" style="margin:0 auto;display:block;" >{{item.user.userName}}（同意）</el-button><br><br>
	    	</li>
	    	<li v-for="(item,index) in this.detailPages" v-if="item.status == 4">
	    	    <img src="../../assets/arrow.jpg" style="margin:0 auto;display:block;"><br><br>
	    	    <el-button type="primary"  style="background-color:red;margin:0 auto;display:block;">{{item.user.userName}}（未同意）</el-button><br><br>
	    	</li>
	    	<li v-for="(item,index) in this.detailPages" v-if="item.status == 2">
	    	    <img style="margin:0 auto;display:block;" src="../../assets/arrow.jpg" ><br><br>
	    	    <el-button type="primary"  style="background-color:#F7BA2A;margin:0 auto;display:block;" align="center">{{item.user.userName}}（正在审核）</el-button><br><br>
	    	</li>
	    	<li v-for="(item,index) in this.detailPages" v-if="item.status == 1">
	    	    <img src="../../assets/arrow.jpg" style="margin:0 auto;display:block;"><br><br>
	    	    <el-button type="primary"  style="background-color:#8492A6;margin:0 auto;display:block;">{{item.user.userName}}（未到）</el-button><br><br>
	    	</li>
        </ul> -->
    

    <!--步骤条的审签状态-->
    <el-steps :space="200" :active="1" finish-status="success">
    <el-step v-for="(item,index) in this.detailPages" v-if="items.status == 3||items.status == 4" title="审核完成"></el-step>
    <el-step v-for="(item,index) in this.detailPages" v-if="items.status == 2" title="正在审核"></el-step>
    <el-step title="未进行" "(item,index) in this.detailPages" v-if="items.status ==1"></el-step>
    </el-steps>
    </section>
    
</template>

<script>
	export default{
		data(){
			return {
				instanceId: 0,
				detailPages:[],
			}
		},
		methods: {
            redirect() {
				sessionStorage.removeItem('instanceId');
				this.$router.push({path:'/mySubmit'});
			},
			getDetail() {
				let params={
					instanceId: this.instanceId,
				}
				let url='api/nodeInstance/details';
				this.func.ajaxPost(url,params,res=>{
					this.detailPages = res.data.records;
					//console.log(this.detailPages[0].node.sequence);
				})
			}
		},
		mounted() {
			this.instanceId = sessionStorage.getItem('instanceId');
			this.getDetail();
		},
	}
</script>

<style scoped>
.agree{
	background-color: #FF4949;
}
</style>