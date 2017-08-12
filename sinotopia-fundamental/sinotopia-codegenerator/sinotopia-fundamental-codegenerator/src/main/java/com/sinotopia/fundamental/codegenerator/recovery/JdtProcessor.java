package com.hkfs.fundamental.codegenerator.recovery;

import com.hkfs.fundamental.codegenerator.basis.data.Annotation;
import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.basis.data.*;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.utils.CodeUtils;
import com.hkfs.fundamental.codegenerator.utils.FileUtils;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;
import org.eclipse.jdt.core.dom.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * java文件解析成Class
 * Created by zhoubing on 2016/5/3.
 */
public class JdtProcessor {
    public static JdtProcessor newInstance() {
        return new JdtProcessor();
    }

    //注释处理器
    private List<CommentProcessor> commentProcessorList = new ArrayList<CommentProcessor>();

    public void addCommentProcessor(CommentProcessor commentProcessor) {
        this.commentProcessorList.add(commentProcessor);
    }

    public Clazz process(String filePath) {
        //初始化注释器
        initCommentProcessor();

        String content = FileUtils.getStringFromFile(filePath); //java源文件
        //创建解析器
        ASTParser parser = ASTParser.newParser(AST.JLS3);
        //设定解析器的源代码字符
        parser.setSource(content.toCharArray());
        //使用解析器进行解析并返回AST上下文结果(CompilationUnit为根节点)
        CompilationUnit result = (CompilationUnit) parser.createAST(null);

        //获取类型
        List types = result.types();
        //取得类型声明
        TypeDeclaration typeDec = (TypeDeclaration) types.get(0);

        //取得包名
        String packageName = result.getPackage().getName().toString();
        //引用import
        List importList = result.imports();
        //内部类
        TypeDeclaration[] innerTypeDec = typeDec.getTypes();

        Clazz cls = processClass(typeDec, packageName, importList);
        for (TypeDeclaration eachType : innerTypeDec) {
            Clazz innerCls = processClass(eachType, "", null);
            cls.addInnerClass(innerCls);
        }

        //额外注释
        if (commentProcessorList != null && commentProcessorList.size() > 0) {
            for (CommentProcessor processor : commentProcessorList) {
                processor.process(cls, filePath);
            }
        }
        return cls;
    }

    protected void initCommentProcessor() {
        this.commentProcessorList.add(new FieldCommentProcessor());
    }

    protected Clazz processClass(TypeDeclaration typeDec, String packageName, List importList) {
        //##############获取源代码结构信息#################
        //类注解
        Javadoc classJavadoc = typeDec.getJavadoc();
        //父类
        Type superClassType = typeDec.getSuperclassType();
        //取得类名
        String className = typeDec.getName().toString();
        //取得函数(Method)声明列表
        MethodDeclaration[] methodDec = typeDec.getMethods();
        //取得函数(Field)声明列表
        FieldDeclaration[] fieldDec = typeDec.getFields();

        String fullClassName = null;
        if (StrUtils.notEmpty(packageName)) {
            fullClassName = packageName+"."+className;
        }
        else {
            fullClassName = className;
        }
        Clazz cls = new Clazz(fullClassName);
        cls.setComments(getCommentText(classJavadoc));
        cls.setHasGetterSetterMethod(false);
        cls.setIsInterface(typeDec.isInterface());

        Annotation[] classAnnotations = getAnnotations(typeDec.modifiers());
        if (classAnnotations != null) {
            cls.setAnnotations(classAnnotations);
        }
        String classModifier = getModifiers(typeDec.modifiers());
        cls.setModifier(classModifier);

        if (importList != null) {
            for (Object obj : importList) {
                ImportDeclaration importDec = (ImportDeclaration) obj;
                String name = importDec.getName().toString();
                String actualName = StrUtils.getMiddleText(name, ".", null, false, false);
                if (actualName != null && !StrUtils.isUpperCase(actualName.charAt(0))) {
                    //import的不是类 而是包名+*
                    name = name + ".*";
                } else {
                    Config.addFullClass(name);
                }
                cls.addImport(name);
            }
        }
        Clazz parentCls = null;
        if (superClassType != null) {
            parentCls = new Clazz(superClassType.toString());
        }

        List superInterfaceTypes = typeDec.superInterfaceTypes();
        if (superInterfaceTypes != null && superInterfaceTypes.size() > 0) {
            for (Object obj : superInterfaceTypes) {
                Interface in = null;
                if (obj instanceof SimpleType) {
                    SimpleType type = (SimpleType) obj;
                    in = new Interface(type.getName().toString());
                }
                else {
                    in = new Interface(obj.toString());
                }

                cls.addInterface(in);
            }
        }


        cls.setParentClass(parentCls);

        for (FieldDeclaration eachField : fieldDec) {
            String modifier = getModifiers(eachField.modifiers());
            String type = eachField.getType().toString();
            String name = getFieldName(eachField);
            Javadoc javadoc = eachField.getJavadoc();
            Field field = new Field(name, type);
            field.setModifier(modifier);
            if (javadoc != null) {
                field.setComments(getCommentText(javadoc));
            }
            Annotation[] annotations = getAnnotations(eachField.modifiers());
            if (annotations != null) {
                field.setAnnotations(annotations);
            }
            cls.addField(field);
        }

        for (MethodDeclaration eachMethod : methodDec) {
            String type = "";
            if (eachMethod.getReturnType2() != null) {
                type = eachMethod.getReturnType2().toString();
            }
            String name = eachMethod.getName().toString();
            String body = "";
            if (eachMethod.getBody() != null) {
                body = eachMethod.getBody().toString();
            }

            Method method = new Method(name, type);
            String newBody = getMethodBody(body);
            method.setBody(newBody);
            String modifier = getModifiers(eachMethod.modifiers());
            method.setModifier(modifier);
            Annotation[] annotations = getAnnotations(eachMethod.modifiers());
            if (annotations != null) {
                method.setAnnotations(annotations);
            }
            Param[] params = getParams(eachMethod.parameters());
            if (params != null) {
                method.setParams(params);
            }
            Javadoc javadoc = eachMethod.getJavadoc();
            if (javadoc != null) {
                method.setComments(getCommentText(javadoc));
            }
            cls.addMethod(method);
        }
        return cls;
    }

    protected Param[] getParams(List parameters) {
        List<Param> list = new ArrayList<Param>();
        if (parameters != null) {
            for (Object obj : parameters) {
                SingleVariableDeclaration declaration = (SingleVariableDeclaration)obj;
                String type = declaration.getType().toString();
                String name = declaration.getName().toString();
                Param param = new Param(type, name);

                List modifiers = declaration.modifiers();
                if (modifiers != null && modifiers.size() > 0) {
                    param.annotations = getAnnotations(modifiers);
                }

                list.add(param);
            }
        }
        return list.toArray(new Param[list.size()]);
    }

    protected String getMethodBody(String body) {
        String text = StrUtils.getMiddleText(body, "{", "}", true, false);
        if (StrUtils.notEmpty(text)) {
            return CodeUtils.formatJavaCode(text, 2);
        }
        return null;
    }

    protected String[] getCommentText(String comment) {
        String text = StrUtils.getMiddleText(comment, "/**", "*/");
        if (text != null) {
            String[] arr = text.split("\n");
            List<String> comments = new ArrayList<String>(arr.length);
            for (int i = 0; i < arr.length; i++) {
                arr[i] = arr[i].replace("*", "").trim();
                if (arr[i].length() > 0) {
                    comments.add(arr[i]);
                }
            }
            return comments.toArray(new String[comments.size()]);
        }
        return null;
    }
    protected String[] getCommentText(Javadoc javadoc) {
        if (javadoc != null) {
            return getCommentText(javadoc.toString());
        }
        return new String[]{};
    }

    protected String getFieldName(FieldDeclaration fieldDecEle) {
        StringBuilder sb = new StringBuilder();
        List fragments = fieldDecEle.fragments();
        if (fragments != null) {
            int size = fragments.size();
            for (int i = 0; i < size - 1; i++) {
                sb.append(fragments.get(i)).append(" ");
            }
            if (size > 0) {
                sb.append(fragments.get(size - 1));
            }
        }
        return sb.toString();
    }

    protected String getModifiers(List modifiers) {
        StringBuilder sb = new StringBuilder();
        if (modifiers != null) {
            int size = modifiers.size();
            for (int i = 0; i < size-1; i++) {
                Object obj = modifiers.get(i);
                if (obj instanceof org.eclipse.jdt.core.dom.Annotation) {
                    continue;
                }
                sb.append(obj).append(" ");
            }
            if (size > 0) {
                Object obj = modifiers.get(size - 1);
                if (!(obj instanceof org.eclipse.jdt.core.dom.Annotation)) {
                    sb.append(obj);
                }
            }
        }
        return sb.toString();
    }

    protected Annotation[] getAnnotations(List modifiers) {
        List<Annotation> list = new ArrayList<Annotation>();
        if (modifiers != null) {
            for (Object obj : modifiers) {
                if (obj instanceof NormalAnnotation) {
                    NormalAnnotation normalAnnotation = (NormalAnnotation) obj;
                    Annotation annotation = new Annotation(StrUtils.trim(obj.toString()),
                            normalAnnotation.getTypeName().toString());
                    List values = normalAnnotation.values();
                    if (values != null) {
                        annotation.keyValues = getKeyValues(values);
                    }
                    list.add(annotation);
                }
                else if (obj instanceof org.eclipse.jdt.core.dom.Annotation) {
                    Annotation an = new Annotation(obj.toString(), obj.toString());
                    list.add(an);
                }
                else {
                    break;
                }
            }
        }
        return list.toArray(new Annotation[list.size()]);
    }

    protected Map<String, String> getKeyValues(List list) {
        if (list != null && list.size() > 0) {
            Map<String, String> map = new HashMap<String, String>(list.size());
            for (Object obj : list) {
                if (obj instanceof MemberValuePair) {
                    MemberValuePair pair = (MemberValuePair) obj;
                    map.put(pair.getName().toString(), pair.getValue().toString());
                }
            }
            return map;
        }
        return null;
    }
}
