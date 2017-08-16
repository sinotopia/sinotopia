//$("#myFiles").click(function () {
				var uploader = new plupload.Uploader(
						{
							runtimes : 'html5,flash,silverlight,html4',
							browse_button : 'myFiles', // you can pass an id...
							container : document.getElementById('container'), // ... or DOM Element itself
							url : 'http://192.168.8.31:8850/upload',
						    multipart:true,
						    multi_selection:false,//ֻ��ѡȡһ��
							filters : {
								max_file_size : '10mb',
								mime_types : [ {
									title : "Image files",
									extensions : "jpg,gif,png"
								}, {
									title : "Zip files",
									extensions : "zip"
								} ]
							},

							init : {
								PostInit : function() {
//									$("#upload").click(function(){
//										alert("123")
//										uploader.start();
//										return false;
//									})
								},

								FilesAdded : function(up, files) {
									plupload
											.each(
													files,
													function(file) {
														document
																.getElementById('filelist').innerHTML += '<div id="' + file.id + '">'
																+ file.name
																+ ' ('
																+ plupload
																		.formatSize(file.size)
																+ ') <b></b></div>';
													});
								},

							}
						});
				uploader.init();
				
			    uploader.bind('FileUploaded',function(uploader,files,responseObject){
			        uploader.destroy();
					alert("done")
			    });
//		})
$("#upload").click(function(){
	alert("123")
	uploader.start();
	return false;
})