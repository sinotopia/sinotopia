package com.hkfs.fundamental.test;

import com.hkfs.fundamental.codegenerator.basis.data.Annotation;
import com.hkfs.fundamental.codegenerator.basis.data.Class;
import com.hkfs.fundamental.codegenerator.basis.data.Interface;
import com.hkfs.fundamental.codegenerator.basis.data.db.Connection;
import com.hkfs.fundamental.codegenerator.basis.data.db.MySql;
import com.hkfs.fundamental.codegenerator.basis.data.db.Table;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.template.TemplateMapper;
import com.hkfs.fundamental.codegenerator.basis.translator.BaseColumnNameTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.BaseColumnTypeTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.ClassToDaoTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.TableToClassTranslator;
import com.hkfs.fundamental.codegenerator.output.MultiCodeOutputer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubing on 2016/4/11.
 */
public class TemplateTest {
    static {
        Consts.TAB = "    ";
        Config.addFullClass("org.springframework.stereotype.Repository");
    }

    static Connection connection = new Connection("192.168.7.55", 3306, "djd", "root", "123456");

    private static final String ROOT = "F:\\temp1";
    private static final String TEMPLATE_ROOT = "F:\\works\\hkfs-fundamental\\hkfs-fundamental\\hkfs-fundamental-codegenerator\\src\\test\\resources";

    public static void main(String[] args) throws Exception {
        generateSms();
    }

    private static void generateSms() throws Exception {
        generate("sms", new String[]{
                "mcc_tel_voice_checkcode",
        });
    }

    private static void generate(String module, String[] tableNames) throws Exception {
        String root = ROOT+"/djd-business-"+module+"/";
        String mapperRoot = root+"djd-business-"+module+"-impl/src/main/resources/mappers";
        String pojoPackageName = "com.djd.business."+module+".bean";
        String daoPackageName = "com.djd.business."+module+".dao";
        String templateFilePath = TEMPLATE_ROOT+"/mapper.ftl";

        TableToClassTranslator tableToClassTranslator = new TableToClassTranslator(pojoPackageName,
                new BaseColumnTypeTranslator().setReflection("DECIMAL", "Double"), new BaseColumnNameTranslator());
        tableToClassTranslator.setParentClassName("com.djd.fundamental.api.data.PojoDataObjectBase");//基类
        tableToClassTranslator.setIsSerializable(true);
        tableToClassTranslator.setTablePrefix("mcc_");

        //查询数据库获取table
        Table[] tables = MySql.newInstance(connection).include(tableNames).getTables();
        //将table转换成class
        Class[] pojoClasses = tableToClassTranslator.translate(tables);

        ClassToDaoTranslatorEx classToDaoTranslator = new ClassToDaoTranslatorEx(daoPackageName);

        //输出mapper.xml
        MultiCodeOutputer mapperOutputer = MultiCodeOutputer.newInstance(mapperRoot, "").setClearFolderBeforeOutput(false).setGiveupOutputIfExists(false);
        TemplateMapper[] mappers = translate(tables, templateFilePath, tableToClassTranslator, classToDaoTranslator);
        mapperOutputer.output(mappers);
    }

    //将table转换成扩展mapper
    private static TemplateMapper[] translate(Table[] tables, String templateFilePath, TableToClassTranslator tableToClassTranslator, ClassToDaoTranslatorEx classToDaoTranslatorEx) {
        List<TemplateMapper> list = new ArrayList<TemplateMapper>();
        for (Table table : tables) {
            Class pojoClass = tableToClassTranslator.translate(table);
            Interface daoInterface = classToDaoTranslatorEx.translate(pojoClass);
            list.add(new TemplateMapper(table, daoInterface, templateFilePath, tableToClassTranslator));
        }
        return list.toArray(new TemplateMapper[list.size()]);
    }

    //将mapper转换成dao的接口
    private static Interface[] translate(Class[] pojoClasses, ClassToDaoTranslatorEx classToDaoTranslatorEx) {
        List<Interface> list = new ArrayList<Interface>();
        for (Class cls : pojoClasses) {
            list.add(classToDaoTranslatorEx.translate(cls));
        }
        return list.toArray(new Interface[list.size()]);
    }


    static class ClassToDaoTranslatorEx extends ClassToDaoTranslator {
        static String parentBaseMapperName = "com.djd.fundamental.database.DaoBase<T, Long>";

        public ClassToDaoTranslatorEx(String packageName) {
            super(packageName);
        }

        @Override
        public Interface translate(Class pojoClass) {
            String daoClassName = processDaoFullClassName(pojoClass);
            String parentDaoClassName = parentBaseMapperName.replace("<T,", "<"+pojoClass.fullClassName+",");
            Interface it = new Interface(daoClassName).setParentInterface(new Interface(parentDaoClassName));
            it.setAnnotation(new Annotation("@Repository"));
            return it;
        }
    }
}
