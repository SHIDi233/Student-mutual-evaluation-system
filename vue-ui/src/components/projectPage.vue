<style src="./css/project.css" scoped></style>
<template>
    <div>
        <div class='project-data'>
			
		<!-- <p> -->
		<el-container style="height: 500px; border: 1px solid #eee">
		<el-aside width="200px">
			<div class='living'>
				<div class='living_table_name'>
					当前进行的会议
				</div>
			</div>
			<div class='living_table_body'>
				<!-- <div class='item_left'>
					<div class='face'>
					<div class='bimg'>
						<div class='img-inner'>
								<img src='./header/avator.jpg' width='48px' height='48px'/>
							</div>
						</div>
					</div>
					<div class='showing'>进行中
					</div>
				</div> -->
				<!-- <div class='item_right'>
					<div class='living_name'>央视新闻直播间1</div>
					<div class='living_des'>首艘国产大型邮轮出坞准备进行时 看万吨邮轮如何起浮</div>
				</div> -->
			</div>
		</el-aside>
		<el-container width="2000">
		<main>
			<!-- <div class='project-header'>
				<div class='header_list'> -->
					<!-- <div href="#" class="Home" onclick="leave()" style="color: #00aeec; font-weight: 600;">Home</div>
					<div href="#" class="Msg" onclick="Msg()" style="color: #00aeec; font-weight: 600;">Msg</div>
					<div href="#" class="Code" onclick="Code()" style="color: #00aeec; font-weight: 600;">Code</div>
					<div href="#" class="Setting" onclick="Settings()" style="color: #00aeec; font-weight: 600;">Settings</div> -->
					<projectHeader width="2000"></projectHeader>
					<router-view/>
				<!-- </div>
			</div> -->
		</main>
	</el-container>
		<!-- </p> -->
	</el-container>
	</div>
	<!-- <el-table
    :data="id"
    style="width: 100%"
    max-height="8 50">
	</el-table> -->
    </div>
</template>

<script>
	import ProjectHeader from "../components/ProjectHeader.vue";

	
	export default{
		created() {
			// alert(this.$route.params.id);
		},
		computed(){
			
		},
		methods:{
			// a() {
			// 	alert(this.$route.params.num);
			// } 
		},
		components:{
			ProjectHeader
		},
		data(){
			
			return {
				// id:'1',
			};
			
		}
		
	}
	
</script>