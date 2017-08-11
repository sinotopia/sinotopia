package com.hkfs.fundamental.codegenerator.test;

import com.hkfs.fundamental.codegenerator.basis.data.Annotation;
import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.basis.data.Interface;
import com.hkfs.fundamental.codegenerator.basis.data.db.*;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.translator.BaseColumnNameTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.BaseColumnTypeTranslator;
import com.hkfs.fundamental.codegenerator.basis.translator.TableToClassTranslator;
import com.hkfs.fundamental.codegenerator.io.CodeInputer;
import com.hkfs.fundamental.codegenerator.output.MultiCodeOutputer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhoubing on 2016/4/11.
 */
public class DjdCodeGenerator {
    static {
        Consts.TAB = "    ";
        Config.addFullClass("org.springframework.stereotype.Repository");
    }

    static Connection connection = new Connection("192.168.7.55", 3306, "djd", "root", "123456");

//    private static final String ROOT = "F:\\works\\dingjidai\\djd-v2-git\\djd-fundamental\\djd-fundamental-codegenerator\\src\\out\\";
    private static final String ROOT = "djd-business/";

    public static void main(String[] args) throws Exception {
//        generateSms();
//        generatorCreditloan();
//        generateAdmin();
//        generateCustomer();
//        generatePayment();
//        generateMisc();
//        generateActivity();
        generateDefaultMapper();
    }

    private static void generatorCreditloan() throws Exception {
        generate("creditloan", new String[]{
                "mcc_credit_card",
                "mcc_creditreport_user",
                "mcc_amount_change_pool",
                "mcc_customer_consume",
                "mcc_consume_repayment",
                "mcc_customer_credit_report",
                "mcc_customer_grant",
                "mcc_customer_grant_attribute_value",
                "mcc_customer_grant_data",
                "mcc_customer_investor",
                "mcc_customer_manual_audit",
                "mcc_customer_card",
                "mcc_daily_amount",
                "mcc_grant_auth",
                "mcc_identity_auth",
                "mcc_identity_auth_record",
                "mcc_consume_status",
                "mcc_consume_status_transform",
                "mcc_consume_supply",
                "mcc_fight_type",
                "mcc_investor",
                "mcc_investor_attribute",
                "mcc_investor_bank",
                "mcc_investor_daikou_bank",
                "mcc_investor_grant",
                "mcc_promote_record",
                "mcc_modify_log",
                "mcc_repayment_change_amount",
                "mcc_repayment_record",
                "mcc_repayment_schedule",
                "mcc_customer_blacklist_record",
                "mcc_customer_tongdun_relation",
                "mcc_txpwd_check",
        });
    }

    private static void generateCustomer() throws Exception {
        generate("customer", new String[]{
                "mcc_customer",
                "mcc_customer_info",
                "mcc_customer_contacts",
                "mcc_location",
                "mcc_pic",
                "mcc_recommend_relation",
                "mcc_customer_login_log",
        });
    }

    private static void generatePayment() throws Exception {
        generate("payment", new String[]{
                "mcc_customer_bankcard",
                "mcc_bank_config",
                "mcc_bank_pay",
                "mcc_bestpay_overdue_history",
                "mcc_bestpay_repayment_request",
                "mcc_bestpay_repayment_smspay_request",
                "mcc_bestpay_sign_account",
                "mcc_chinapay_repayment_request",
                "mcc_extract_batch",
                "mcc_extract_cash",
                "mcc_extract_order",
                "mcc_lianlian_payment",
                "mcc_shanxin_repayment_request",
                "mcc_shanxinpay_sign_account",
                "mcc_union_payment",
                "mcc_threshold_repayment_schedule",
        });
    }

    private static void generateAdmin() throws Exception {
        generate("admin", new String[]{
                "mcc_role",
                "mcc_role_menu",
                "mcc_role_power",
                "mcc_user",
                "mcc_menu",
                "mcc_power",
                "mcc_position",
        });
    }

    private static void generateActivity() throws Exception {
        generate("activity", new String[]{
                "mcc_activity",
                "mcc_activity_flow",
                "mcc_activity_prize",
                "mcc_weixin_auth_redirect",
                "mcc_weixin_menu",
                "mcc_weixin_payment",
                "mcc_weixin_reply",
                "mcc_weixin_scene",
                "mcc_weixin_user",
                "mcc_coupon",
                "mcc_coupon_batch",
                "mcc_coupon_type",
                "mcc_customer_coupon",
        });
    }

    private static void generateSms() throws Exception {
        generate("sms", new String[]{
                "mcc_tel_voice_checkcode",
        });
    }

    private static void generateMisc() throws Exception {
        generate("misc", new String[]{
                "mcc_app_version_info",
                "mcc_feed_back",
                "mcc_index_config",
                "mcc_wallet",
                "mcc_wescore_record",
                "mcc_taobao_seller_info",
                "mcc_connection",
                "mcc_whitelist_item",
                "mcc_apply_upload_file",
                "mcc_common_config",
                "mcc_device_info",
        });
    }

    //需要自定义的枚举配置，默认枚举按照（表名+字段名称）作为枚举的名称
//    static Map<String, String> enumsConfigMap = new HashMap<String, String>();
//    static {
//        //key=表名/字段名，value=枚举名称
//        enumsConfigMap.put("mcc_coupon_type/type", "CouponTypeEnum");
//        enumsConfigMap.put("mcc_weixin_auth_redirect/userAgentType", "UserAgentType");
//        enumsConfigMap.put("mcc_customer_info/contactType", "ContactType");
//        enumsConfigMap.put("mcc_coupon_batch/createUserType", "CreateUserType");
//        enumsConfigMap.put("mcc_modify_log/modifyType", "ModifyType");
//        enumsConfigMap.put("mcc_weixin_auth_redirect/status", "AuthRedirectStatus");
//    }

//    private static void generate(String module) throws Exception {
//        generate(module, new String[]{});
//    }

    private static void generateDefaultMapper() throws Exception {
        String root = "F:\\temp2\\";
        String mapperRoot = root+"/src/main/resources/mappers";
        String pojoPackageName = "com.djd.business.bean";
        String daoPackageName = "com.djd.business.dao";

        TableToClassTranslator tableToClassTranslator = new TableToClassTranslator(pojoPackageName,
                new BaseColumnTypeTranslator().setReflection("DECIMAL", "Double"), new BaseColumnNameTranslator());
        tableToClassTranslator.setParentClassName("com.djd.fundamental.api.data.PojoDataObjectBase");//基类
        tableToClassTranslator.setIsSerializable(true);
        tableToClassTranslator.setTablePrefix("mcc_");
        //查询数据库获取table
        Table[] tables = MySql.newInstance(connection).include("mcc_tel_voice_checkcode").getTables();

        //输出mapper.xml
        MultiCodeOutputer mapperOutputer = MultiCodeOutputer.newInstance(mapperRoot, "").setClearFolderBeforeOutput(false).setGiveupOutputIfExists(false);
        Mapper[] mappers = translate2(mapperOutputer, daoPackageName, tables, tableToClassTranslator);
        mapperOutputer.output(mappers);
    }

    private static void generate(String module, String[] tableNames) throws Exception {
        String root = ROOT+"/djd-business-"+module+"/";
        String apiRoot = root+"djd-business-"+module+"-api/src/main/java";
        String implRoot = root+"djd-business-"+module+"-impl/src/main/java";
        String mapperRoot = root+"djd-business-"+module+"-impl/src/main/resources/mappers";
        String pojoPackageName = "com.djd.business."+module+".bean";
        String pojoEnumDefultPackageName = "com.djd.business."+module+".enums";
        String daoPackageName = "com.djd.business."+module+".dao";

        TableToClassTranslator tableToClassTranslator = new TableToClassTranslator(pojoPackageName,
                new BaseColumnTypeTranslator().setReflection("DECIMAL", "Double"), new BaseColumnNameTranslator());
        tableToClassTranslator.setParentClassName("com.djd.fundamental.api.data.PojoDataObjectBase");//基类
        tableToClassTranslator.setIsSerializable(true);
        tableToClassTranslator.setTablePrefix("mcc_");

        //注释掉后不自动生成枚举
//        tableToClassTranslator.setColumnToEnumTranslator(new ColumnToEnumTranslator(pojoEnumDefultPackageName, NameRender.Constant){
//            @Override
//            protected String processEnumClassName(Table table, Column column) {
//                String key = table.name+"/"+column.name;
//                String enumName = enumsConfigMap.get(key);
//                if (enumName != null) {
//                    return enumName;
//                }
//                return super.processEnumClassName(table, column);
//            }
//        });


        //查询数据库获取table
        Table[] tables = MySql.newInstance(connection).include(tableNames).getTables();
        //将table转换成class
        Clazz[] pojoClasses = tableToClassTranslator.translate(tables);

        //pojo输出
        MultiCodeOutputer.newInstance(apiRoot, pojoPackageName).setClearFolderBeforeOutput(false).setGiveupOutputIfExists(false).output(pojoClasses);
        //枚举输出
        MultiCodeOutputer.newInstance(apiRoot, pojoEnumDefultPackageName).setClearFolderBeforeOutput(false).setGiveupOutputIfExists(false).outputEnum(pojoClasses);


        //输出mapper.xml
        MultiCodeOutputer mapperOutputer = MultiCodeOutputer.newInstance(mapperRoot, "").setClearFolderBeforeOutput(false).setGiveupOutputIfExists(false);
        ExMapper[] mappers = translate(mapperOutputer, daoPackageName, tables, tableToClassTranslator);
        mapperOutputer.output(mappers);

        //输出dao
        Interface[] interfaces = translate(mappers);
        MultiCodeOutputer.newInstance(implRoot, daoPackageName).setClearFolderBeforeOutput(false).setGiveupOutputIfExists(true).output(interfaces);
    }

    //将table转换成扩展mapper
    private static ExMapper[] translate(MultiCodeOutputer mapperOutputer, String packageName, Table[] tables, TableToClassTranslator tableToClassTranslator) {
        List<ExMapper> list = new ArrayList<ExMapper>();
        for (Table table : tables) {
            ExMapper mapper = new ExMapper(packageName, table, tableToClassTranslator);
            String mapperPath = mapperOutputer.getOutputablePathBuilder(mapper).build();
            CodeInputer codeInputer = new CodeInputer(mapperPath);
            mapper.setExistingMapper(codeInputer.read());
            mapper.setPrimaryKeyColumnName("id");
            list.add(mapper);
        }
        return list.toArray(new ExMapper[tables.length]);
    }
    //将table转换成扩展mapper
    private static Mapper[] translate2(MultiCodeOutputer mapperOutputer, String packageName, Table[] tables, TableToClassTranslator tableToClassTranslator) {
        List<Mapper> list = new ArrayList<Mapper>();
        for (Table table : tables) {
            Mapper mapper = new Mapper(packageName, table, tableToClassTranslator);
            String mapperPath = mapperOutputer.getOutputablePathBuilder(mapper).build();
            CodeInputer codeInputer = new CodeInputer(mapperPath);
            mapper.setExistingMapper(codeInputer.read());
            mapper.setPrimaryKeyColumnName("id");
            list.add(mapper);
        }
        return list.toArray(new Mapper[tables.length]);
    }

    //扩展的mapper
    static class ExMapper extends Mapper {
        private TableToClassTranslator tableToClassTranslator;

        public ExMapper(String packageName, Table table, TableToClassTranslator tableToClassTranslator) {
            super(packageName, table, tableToClassTranslator);
            this.tableToClassTranslator = tableToClassTranslator;
        }

        @Override
        public String processNamespace() {
            return packageName+"."+processPojoClassName()+"Dao";
        }

        @Override
        public String getPackageName() {
            return "";
        }

        @Override
        protected String processParameterType() {
            return pojoClass.fullClassName;
        }
        @Override
        protected String processResultType() {
            return pojoClass.fullClassName;
        }
        @Override
        protected String processRequestAllFieldsSQLPrefix() {
            return "<sql id=\""+processRequestAllFieldsSQLId()+"\">"+line()+tab(2)+"<![CDATA[";
        }
        protected String processSelectItemSQL(Column column, boolean isLastItem) {
            StringBuilder sb = new StringBuilder();
            sb.append(tab(3));
            String parameterName = columnNameTranslator.translate(column.name);
            if (parameterName.equals(column.name)) {
                sb.append(parameterName);
            }
            else {
                sb.append(column.name).append(" AS ").append(parameterName);
            }
            if (!isLastItem) {
                sb.append(",");
            }
            sb.append(line());
            return sb.toString();
        }

        @Override
        protected String processRequestAllFieldsSQLSuffix() {
            return tab()+"]]>"+line()+tab()+"</sql>";
        }

        @Override
        protected String processWhereClauseItem(Column column) {
            String columnType = column.type;
            //日期不使用where =
            if (columnType.contains("DATE") || columnType.contains("TIMESTAMP")) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            String parameterName = columnNameTranslator.translate(column.name);
            sb.append(tab(3) + "<if test=\"null!=" + parameterName);
            if (isStringParameter(column)) {
                sb.append(" and ''!=" + parameterName);
            }
            sb.append("\">AND " + column.name + " = #{" + parameterName + "}</if>" + line());
            return sb.toString();
        }

        protected String processInsertDefineItem(Column column) {
            if (column.name.equals(primaryKeyColumnName)) {
                return "";
            }
            String parameterName = columnNameTranslator.translate(column.name);
            if (isStringParameter(column)) {
                return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">," + column.name + "</if>" + line();
            }
            return tab(3) + "<if test=\"null!=" + parameterName + "\">," + column.name + "</if>" + line();
        }

        protected String processInsertValueItem(Column column) {
            if (column.name.equals(primaryKeyColumnName)) {
                return "";
            }
            String parameterName = columnNameTranslator.translate(column.name);
            if (isStringParameter(column)) {
                return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">,#{" + parameterName + "}</if>" + line();
            }
            return tab(3) + "<if test=\"null!=" + parameterName + "\">,#{" + parameterName + "}</if>" + line();
        }

        private boolean isStringParameter(Column column) {
            String type = tableToClassTranslator.getColumnTypeTranslator().translate(column);
            return type.equalsIgnoreCase("String");
        }

        @Override
        protected String processUpdateItem(Column column) {
            if (column.name.equals(primaryKeyColumnName)) {
                return "";
            }
            String parameterName = columnNameTranslator.translate(column.name);
            if (isStringParameter(column)) {
                return tab(3) + "<if test=\"null!=" + parameterName + " and ''!=" + parameterName + "\">," + column.name + " = #{" + parameterName + "}</if>" + line();
            }
            return tab(3) + "<if test=\"null!=" + parameterName + "\">," + column.name + " = #{" + parameterName + "}</if>" + line();
        }
    }

    //将mapper转换成dao的接口
    private static Interface[] translate(Mapper[] mappers) {
        List<Interface> list = new ArrayList<Interface>();
        String parentBaseMapperName = "com.djd.fundamental.database.DaoBase<T, Long>";
        for (Mapper mapper : mappers) {
            String parentMapperName = parentBaseMapperName.replace("<T,", "<"+mapper.pojoClass.fullClassName+",");
            Interface it = new Interface(mapper.processNamespace()).setParentInterface(new Interface(parentMapperName));
            it.setAnnotation(new Annotation("@Repository"));
            list.add(it);
        }
        return list.toArray(new Interface[mappers.length]);
    }
}
