/** 上传头像 */
	$(function(){
		var avatar = $("#avatarUpload");
		if(0 === avatar.size()){
			return true;
		}
		
	
		var preview = $("#avatarPreview");
		var form = $("#avatarForm");
	
		var hiddenInput = form.find("input[type=hidden]");
		var avatarFile1 = $("#avatarFile1");
	
		var uploadBtn = $("#uploadBtn");

		avatar.click(function(){
			avatarFile.trigger("click");
		});

		uploadBtn.click(function(){
			avatarFile.trigger('click');
		});

		avatarFile1.change(function(){
			$.ajax({
				url: ROOT_URL + "/admin/borrow/companyUpLoad.html",
				data: new FormData(form[0]),
				type: "post",
				dataType: "json",
				cache: false,
				contentType: false,
    			processData: false,
    			success: function(path){
    				if(path.indexOf("2M") >= 0 ){    alert(path);  return false; }
    				var remoteFile = path.replace(/\\/g, "/");	// I Hate Windows
    				var fileName = remoteFile.substr(0, remoteFile.lastIndexOf("."));
    				var images = $(".preview-avatar img.img-polaroid");
    				fileName = ROOT_URL + fileName;
    				avatar.attr("src", fileName + ".jpg");
    				avatar.data("path", remoteFile);
    				avatar.prop("uploaded", true);
    				$("#imgUpload").val(remoteFile);
    			},
    			error: function(){
    				alert("上传出错，可能是网络问题");
    			}
			});
		});
		

//		$("#saveAvatar").click(function(){
//			var isUploaded = avatar.prop("uploaded");
//			if(!isUploaded){
//				alert("请先选择一张图片再上传保存");
//				return false;
//			}
//			$.post(ROOT_URL + "/myhome/upUserPic.html", { imgFile: avatar.data("path") }, function(res){
//				alert("上传成功");
////				window.location.reload();
//			});
//		});
	});
/**
 * 头像非空校验
 */
//function checkImg(){
//	var avatar = $("#avatarUpload");
//	var isUploaded = avatar.prop("uploaded");
//	if(!isUploaded){
//		alert("请先选择一张图片再上传保存");
//		return false; 
//	}
//	$.post(ROOT_URL + "/myhome/upUserPic.html", { imgFile: avatar.data("path") }, function(res){
////		alert("上传成功");
//		return true;
////		window.location.reload();
//	});
//}