package com.hkfs.fundamental.codegenerator.template;

import com.hkfs.fundamental.codegenerator.utils.IOUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.*;
import java.util.Locale;

/**
 * Created by zhoubing on 2016/5/4.
 */
public class TemplateProcessor {
    private String templateFilePath;
    public TemplateProcessor(String templateFilePath) {
        this.templateFilePath = templateFilePath;
    }

    public static TemplateProcessor newInstance(String templateFilePath) {
        return new TemplateProcessor(templateFilePath);
    }

    public Configuration newConfiguration() {
        Configuration configuration = new Configuration();
        try {
            configuration.setDirectoryForTemplateLoading(getTemplateLoadingFolder());
        } catch (IOException e) {
            e.printStackTrace();
        }
        configuration.setDefaultEncoding(StrUtils.UTF_8);
        // setEncoding这个方法一定要设置国家及其编码，不然在flt中的中文在生成html后会变成乱码
        configuration.setEncoding(Locale.getDefault(), StrUtils.UTF_8);
        // 设置对象的包装器
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        // 设置异常处理器//这样的话就可以${a.b.c.d}即使没有属性也不会出错
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.IGNORE_HANDLER);
        return configuration;
    }

    public String toString(Object object) {
        Configuration configuration = newConfiguration();
        ByteArrayOutputStream baos = null;
        Writer writer = null;
        try {
            Template template = configuration.getTemplate(getTemplateFileName(), StrUtils.UTF_8);
            baos = new ByteArrayOutputStream();
            writer = new BufferedWriter(new OutputStreamWriter(baos, StrUtils.UTF_8));
            template.process(object, writer);
            writer.flush();
            return baos.toString(StrUtils.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.close(baos);
            IOUtils.close(writer);
        }
        return "";
    }

    private File getTemplateLoadingFolder() {
        return new File(templateFilePath).getParentFile();
    }
    private String getTemplateFileName() {
        return new File(templateFilePath).getName();
    }
}
