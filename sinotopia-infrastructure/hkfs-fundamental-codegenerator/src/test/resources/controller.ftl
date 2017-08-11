package ${controllerPackageName};

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import ${servicePackageName}.${controller.serviceName};
import com.djd.fundamental.api.data.*;

<#list controller.methods as eachMethod>
<#if (eachMethod.parameterType)??>
import ${parameterPackageName}.${eachMethod.parameterType};
</#if>
<#if (eachMethod.itemType)??>
import ${itemPackageName}.${eachMethod.itemType};
</#if>
</#list>


/**
 *
 * @author caochong
 * ${controller.comment}
 */
@RestController
@RequestMapping("${controller.url}")
public class ${controller.name} {
    @Autowired
    private ${controller.serviceName} ${controller.serviceParameterName};


<#list controller.methods as eachMethod>
	/**
	 * ${eachMethod.comment}
	 * @param param
	 * @return
	 */
	@RequestMapping("${eachMethod.url}")
	@ResponseBody
	public ${eachMethod.returnType} ${eachMethod.name}(${eachMethod.parameterType} param) {
		return ${controller.serviceParameterName}.${eachMethod.name}(param);
	}
</#list>
}
