package com.sinotopia.fundamental.codegenerator.recovery;

import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;
import com.sinotopia.fundamental.codegenerator.basis.data.Field;
import com.sinotopia.fundamental.codegenerator.utils.CodeUtils;
import com.sinotopia.fundamental.codegenerator.utils.FileUtils;
import com.sinotopia.fundamental.codegenerator.utils.IOUtils;
import com.sinotopia.fundamental.codegenerator.utils.StrUtils;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 基于字段的使用"//"注释的内容获取
 * Created by zhoubing on 2016/5/25.
 */
public class FieldCommentProcessor implements CommentProcessor {
    private static final String COMMENT_PREFIX = "//";
    private Matcher fieldMatcher = Pattern.compile("(private|public|protected)([\\s]+)(.*?)([\\s]+)(.*?)(;)").matcher("");

    @Override
    public void process(Clazz cls, String filePath) {
        BufferedReader br = null;

        try {
            br = FileUtils.getBufferedReader(filePath, StrUtils.UTF_8);
            String line = null;
            List<String> commentList = new ArrayList<String>();
            Map<String, Field> fieldMap = getClassFieldMap(cls);
            while ((line = br.readLine()) != null) {
                processLine(commentList, fieldMap, line);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            IOUtils.close(br);
        }
    }

    private void processLine(List<String> commentList, Map<String, Field> fieldMap, String line) {
        line = StrUtils.trim(line);

        if (isCommentLine(line)) {
            line = processCommentLine(line);
            commentList.add(line);
        }
        else {
            fieldMatcher.reset(line);
            if (fieldMatcher.find() && commentList.size() > 0) {
                String type = fieldMatcher.group(3);
                String name = fieldMatcher.group(5);

                String key = processFieldKey(type, name);
                Field field = fieldMap.get(key);
                if (field != null) {
                    field.addComments(commentList.toArray(new String[commentList.size()]));
                }
            }

            commentList.clear();
        }
    }

    private boolean isFieldLine(String line) {
        if (!line.endsWith(";")) {
            return false;
        }

        fieldMatcher.reset(line);
        return fieldMatcher.matches();
    }

    public static void main(String[] args) {
        Matcher fieldMatcher = Pattern.compile("(private|public|protected)([\\s]+)(.*?)([\\s]+)(.*?)(;)").matcher("private String hello;");
        if (fieldMatcher.find()) {
            int c = fieldMatcher.groupCount();
            for (int i = 0; i < c; i++) {
                System.out.println(i+" "+fieldMatcher.group(i));
            }
        }
    }



    private boolean isCommentLine(String line) {
        return line.startsWith(COMMENT_PREFIX);
    }

    private String processCommentLine(String line) {
        while (line.startsWith("/")) {
            line = line.substring(1, line.length());
        }
        return line;
    }

    private Map<String, Field> getClassFieldMap(Clazz cls) {
        Map<String, Field> map = new HashMap<String, Field>();
        if (cls.fields == null || cls.fields.length == 0) {
            return map;
        }
        for (Field field : cls.fields) {
            String key = processFieldKey(CodeUtils.getClassNameFromFullClassName(field.fullClassName), field.name);
            map.put(key, field);
        }
        return map;
    }

    private String processFieldKey(String type, String name) {
        return (name+"/"+type).toLowerCase();
    }
}
