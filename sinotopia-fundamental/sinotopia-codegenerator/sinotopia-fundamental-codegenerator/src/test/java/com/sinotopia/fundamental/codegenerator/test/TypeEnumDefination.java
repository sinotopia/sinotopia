package com.sinotopia.fundamental.codegenerator.test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.sinotopia.fundamental.codegenerator.basis.data.custom.CDict;
import com.sinotopia.fundamental.codegenerator.basis.data.custom.CEnum;

public class TypeEnumDefination {
//	new JsonClass("AndroidDictionary", "客户端通用数据字典定义", new JsonField[] {
//	new JsonField("sharedFileType", "Integer", "分享文件类型", "k", new JsonDict[]{
//		new JsonDict(1, "Apk", "应用"),
//		new JsonDict(2, "Picture", "图片"),
//		new JsonDict(3, "Music", "音乐"),
//	}),
//	new JsonField("originType", "Integer", "下载文件来源类型", "o", new JsonDict[]{
//		new JsonDict(0, "Download", "下载"),
//		new JsonDict(1, "Received", "快传"),
//	}),
//}),
	
	public static CEnum sharedFileType = new CEnum("sharedFileType", "分享文件类型", new CDict[]{
			new CDict(1, "Apk", "应用"),
			new CDict(2, "Picture", "图片"),
			new CDict(3, "Music", "音乐"),
	});
	public static CEnum originType = new CEnum("originType", "下载文件来源类型", new CDict[]{
			new CDict(0, "Download", "下载"),
			new CDict(1, "Received", "快传"),
	});
	public static CEnum osType = new CEnum("osType", "OS类型", new CDict[]{
			new CDict(1, "Android", "安卓"),
			new CDict(2, "Ios", "iphone"),
	});
	public static CEnum downloadState = new CEnum("downloadState", "下载状态", new CDict[]{
			new CDict(1, "Downloading", "正在下载"),
			new CDict(2, "DownloadPaused", "下载暂停"),
			new CDict(3, "DownloadFinished", "下载成功"),
			new CDict(4, "DownloadFailed", "下载失败"),
			new CDict(5, "Installed", "已安装"),
	});
	public static CEnum error = new CEnum("error", "错误码", new CDict[]{
			new CDict(0, "Success", "成功"),
			new CDict(1, "Failed", "失败"),
			new CDict(2, "Duplicated", "记录重复"),
			new CDict(3, "NotSupported", "不支持"),
			new CDict(4, "FormatError", "格式错误"),
			new CDict(5, "DatabaseError", "数据库错误"),
			new CDict(6, "InternalError", "内部错误"),
			new CDict(7, "NotFound", "未找到"),
			new CDict(8, "InvalidParameter", "无效参数"),
			
			new CDict(20, "UserAlreadyExists", "用户已存在"),
			new CDict(21, "UsernameOrPasswordError", "用户名或密码错误"),
			new CDict(22, "UserNotLoggedin", "用户未登录"),
			new CDict(23, "AccountAlreadyBound", "账户已绑定"),
			new CDict(24, "VerificationVodeError", "验证码错误"),
			new CDict(25, "OriginPasswordError", "原密码错误"),
			new CDict(26, "PhoneNumberNotRegistered", "手机号码未注册"),
			
			
			new CDict(30, "InvalidSessionToken", "会话无效"),
			new CDict(31, "IllegalMethodInvoking", "非法方法调用"),
			new CDict(32, "IackingResource", "缺少资源"),
	});
	public static CEnum needDetail = new CEnum("needDetail", "是否需要上传应用的详细信息", new CDict[]{
			new CDict(1, "Yes", "需要"),
			new CDict(2, "No", "不需要"),
			new CDict(3, "Failed", "失败"),
			new CDict(4, "Exeption", "异常"),
	});
	public static CEnum changeType = new CEnum("changeType", "改变类型", new CDict[]{
			new CDict(1, "Add", "添加"),
			new CDict(2, "Remove", "删除"),
			new CDict(3, "Update", "更新"),
	});
	public static CEnum operatingType = new CEnum("operatingType", "操作类型", new CDict[]{
			new CDict(1, "Adding", "添加"),
			new CDict(2, "Removing", "卸载"),
			new CDict(3, "Updating", "更新"),
	});
	public static CEnum detailType = new CEnum("detailType", "详情类型", new CDict[]{
			new CDict(1, "Owned", "已安装的应用"),
			new CDict(2, "Added", "新安装的应用"),
			new CDict(3, "Updated", "更新的应用"),
	});
	public static CEnum recommendedType = new CEnum("recommendedType", "推荐类型", new CDict[]{
			new CDict(1, "Activity", "活动"),
			new CDict(2, "Resource", "资源"),
	});
	public static CEnum checkStatus = new CEnum("checkStatus", "校验结果", new CDict[]{
			new CDict(1, "Success", "成功"),
			new CDict(2, "Failed", "失败"),
	});
	public static CEnum codeType = new CEnum("codeType", "验证码类型", new CDict[]{
			new CDict(1, "Register", "注册账号"),
			new CDict(2, "Modify", "找回密码"),
	});
	public static CEnum exchangeStatus = new CEnum("exchangeStatus", "兑换状态", new CDict[]{
			new CDict(1, "processing", "正在操作"),
			new CDict(2, "finished", "已完成"),
			new CDict(3, "failed", "兑换失败"),
	});
	public static CEnum pushType = new CEnum("pushType", "推送内容的类型", new CDict[]{
			new CDict(1, "message", "消息"),
	});
	
	public static List<CEnum> getEnums() {
		Field[] fields = TypeEnumDefination.class.getDeclaredFields();
		List<CEnum> list = new ArrayList<CEnum>();
		for (Field field : fields) {
			try {
				Object value = field.get(TypeEnumDefination.class);
				if (value != null && value instanceof CEnum) {
					CEnum em = (CEnum)value;
//					em.setDictNameRender(NameRender.Constant);
					list.add(em);
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}

	static {
		List<CEnum> list = getEnums();
		for (CEnum em : list) {
			em.setFullClassName("com.djd.business.creditloan.enums.type."+em.fullClassName);
		}
	}
}
