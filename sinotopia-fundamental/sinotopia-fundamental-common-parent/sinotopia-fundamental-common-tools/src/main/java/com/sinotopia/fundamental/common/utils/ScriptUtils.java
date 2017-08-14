package com.sinotopia.fundamental.common.utils;

import com.sinotopia.fundamental.common.assist.CodeMocker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

/**
 * JS脚本工具类
 */
public class ScriptUtils {
    private static Logger logger = LoggerFactory.getLogger(CodeMocker.class);

    /**
     * 调用js中的方法
     *
     * @param path         js文件路径
     * @param functionName 要调用的js函数
     * @param args         参数数组
     * @return
     */
    public static Object invokeFunction(String path, String functionName, Object... args) {
        try {
            return invokeFunction(new FileReader(path), functionName, args);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 调用js中的方法
     *
     * @param reader       js文件读取器
     * @param functionName 要调用的js函数
     * @param args         参数数组
     * @return
     */
    public static Object invokeFunction(Reader reader, String functionName, Object... args) {
        try {
            //1.java中执行js方法,返回加密后的密码
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");
            scriptEngine.eval(reader);
            Invocable invocable = (Invocable) scriptEngine;
            return invocable.invokeFunction(functionName, args);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return null;
    }


    /**
     * 调用js文本中某个方法
     *
     * @param jsCode       JS的代码
     * @param functionName 要调用的js函数
     * @param args         参数数组
     * @return
     */
    public static String invokeFunctionByJsCode(String jsCode, String functionName, Object... args) {
        try {
            ScriptEngineManager scriptEngineManager = new ScriptEngineManager();
            ScriptEngine scriptEngine = scriptEngineManager.getEngineByExtension("js");
            scriptEngine.eval(jsCode);
            Invocable invocable = (Invocable) scriptEngine;
            Object result = invocable.invokeFunction(functionName, args);
            return result != null ? result.toString() : null;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(invokeFunctionByJsCode("function method(a,b){return a+b+' Hello';}", "method", "pA", "pB"));
    }
}
