package com.sinotopia.fundamental.codegenerator.test;

import com.sinotopia.fundamental.codegenerator.basis.data.Field;
import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;

public class ClientClassDefination {
	public static Clazz[] classes = new Clazz[] {
		
		new Clazz("AndroidDictionary", "客户端通用数据字典定义", new Field[]{
				
		}),
		new Clazz("ActivityParameter", "界面传递参数", new Field[] {
		}),

		new Clazz("ActivityResult", "界面返回结果参数", new Field[] {
		}),

		new Clazz("SharedFile", "界面传递参数", new Field[] {
			new Field("sharedFileType", "Integer", "文件类型").setRelatedEnum(TypeEnumDefination.sharedFileType),
			new Field("name", "String", "名称"),
			new Field("path", "String", "文件路径"),
			new Field("size", "Long", "大小"),
			new Field("packageName", "String", "包名"),
			
			new Field("selected", "java.lang.Boolean", "选中状态"),
			new Field("percent", "Integer", "文件传输进度"),
			new Field("icon", "java.lang.Math", "图标Drawable"),
			
		}),

		new Clazz("SendFileActivityParameter", "ActivityParameter", "发送文件页面参数", new Field[] {
			new Field("sharedFileList", "SharedFile[]", "文件列表"),
		}),

		new Clazz("DownloadServiceParameter", "ActivityParameter", "下载文件服务参数", new Field[] {
			new Field("name", "String", "资源名"),
			new Field("url", "String", "下载地址"),
			new Field("fileDirectory", "String", "存储目录"),
			new Field("fileName", "String", "文件名"),
			new Field("innerStorage", "Boolean", "是否内部存储"),
			new Field("originType", "Integer", "下载来源（网络，快传）").setRelatedEnum(TypeEnumDefination.originType),
			new Field("sharedFileType", "Integer", "文件类型").setRelatedEnum(TypeEnumDefination.sharedFileType),
		}),
		
		new Clazz("AppHomeActivityParameter", "ActivityParameter", "应用详情页面参数", new Field[] {
			new Field("resource", "com.brucezee.codegenerator.auto.Resource", "应用"),
		}),

		new Clazz("GalleryActivityParameter", "ActivityParameter", "查看图片页面参数", new Field[] {
			new Field("currentImageUrl", "String", "当前显示的图片链接地址"),
			new Field("imageUrls", "String[]", "图片链接地址列表"),
		}),

		new Clazz("WebviewActivityParameter", "ActivityParameter", "打开网页页面参数", new Field[] {
			new Field("title", "String", "标题"),
			new Field("url", "String", "链接地址"),
		}),

		new Clazz("UserHomeActivityParameter", "ActivityParameter", "用户详情页面参数", new Field[] {
				new Field("user", "com.brucezee.codegenerator.auto.User", "用户"),
		}),
		
		new Clazz("UserSetPasswordActivityParameter", "ActivityParameter", "用户注册设置密码页面参数", new Field[] {
			new Field("phoneNumber", "String", "手机号码"),
		}),

		new Clazz("ModifyUserInfoActivityParameter", "ActivityParameter", "修改用户头像昵称页面参数", new Field[] {
			new Field("accountAuthorization", "com.brucezee.codegenerator.auto.AccountAuthorization", "第三方授权"),
		}),

		new Clazz("RemoteClientInfo", "互传文件时终端对象", new Field[] {
			new Field("name", "String", "名称"),
			new Field("avatar", "String", "头像"),
			new Field("ip", "String", "ip地址"),
			new Field("port", "Integer", "ip端口"),
		}),

		new Clazz("TransferConnectResult", "互传文件时连接成功返回对象", new Field[] {
			new Field("serverInfo", "RemoteClientInfo", "服务端连接信息"),
		}),

		new Clazz("TransferGetFileListResult", "互传文件时查询发送文件列表返回对象", new Field[] {
			new Field("sharedFileList", "SharedFile[]", "文件信息列表"),
		}),

		new Clazz("TransferDownloadParameter", "互传文件时下载文件传递的参数对象", new Field[] {
			new Field("sharedFile", "SharedFile", "文件信息"),
		}),
	};


}
