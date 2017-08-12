package com.hkfs.fundamental.codegenerator.lazy;

import com.hkfs.fundamental.codegenerator.basis.data.Annotation;
import com.hkfs.fundamental.codegenerator.basis.data.Clazz;
import com.hkfs.fundamental.codegenerator.basis.data.Interface;
import com.hkfs.fundamental.codegenerator.basis.data.db.*;
import com.hkfs.fundamental.codegenerator.basis.global.Config;
import com.hkfs.fundamental.codegenerator.basis.global.Consts;
import com.hkfs.fundamental.codegenerator.basis.render.NameRender;
import com.hkfs.fundamental.codegenerator.basis.translator.*;
import com.hkfs.fundamental.codegenerator.io.CodeInputer;
import com.hkfs.fundamental.codegenerator.output.MultiCodeOutputer;
import com.hkfs.fundamental.codegenerator.utils.StrUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by brucezee on 2017/1/17.
 */
public class LazyCodeGenerator {
    public static final String DAO_BASE_CLASS_NAME      = "com.hkfs.fundamental.database.DaoBase<T, Long>";
    public static final String PAGE_DAO_BASE_CLASS_NAME = "com.hkfs.fundamental.database.PageDaoBase<T, Long>";

    private String root;
    private String pojoRoot;
    private String pojoEnumRoot;
    private String daoRoot;
    private String mapperRoot;

    private String pojoPackageName;
    private String daoPackageName;
    private String pojoEnumPackageName;

    private Connection connection;

    private String[] includeTables;
    private String[] excludeTables;

    private String primaryKeyColumnName = "id";
    private String pojoParentClassFullName = "com.hkfs.fundamental.api.data.PojoDataObjectBase";
    private String[] tablePrefix = new String[] {"tb_", "t_"};
    private boolean serializable = true;
    private String parentBaseMapperName = DAO_BASE_CLASS_NAME;

    private Map<String, String> columnTypeReflection;


    private TableToClassTranslator tableToClassTranslator;
    private ColumnTypeTranslator columnTypeTranslator;
    private ColumnNameTranslator columnNameTranslator;
    private ColumnToEnumTranslator columnToEnumTranslator;
    private Map<String, String> enumsConfigMap = new HashMap<String, String>();
    private Map<String, String> tableNameConfigMap = new HashMap<String, String>();

    public static LazyCodeGenerator create() {
        return new LazyCodeGenerator();
    }

    public void generate() {
        check();
        init();
        output();
    }

    protected void output() {
        //查询数据库获取table
        Table[] tables = MySql.newInstance(connection).include(includeTables).exclude(excludeTables).getTables();
        //将table转换成class
        Clazz[] pojoClasses = tableToClassTranslator.translate(tables);

        //pojo输出
        if (StrUtils.notEmpty(pojoRoot) && StrUtils.notEmpty(pojoPackageName)) {
            MultiCodeOutputer.newInstance(pojoRoot, pojoPackageName)
                    .setClearFolderBeforeOutput(false)
                    .setGiveupOutputIfExists(false)
                    .output(pojoClasses);
        }

        //枚举输出
        if (StrUtils.notEmpty(pojoEnumRoot) && StrUtils.notEmpty(pojoEnumPackageName)) {
            MultiCodeOutputer.newInstance(pojoEnumRoot, pojoEnumPackageName)
                    .setClearFolderBeforeOutput(false)
                    .setGiveupOutputIfExists(false)
                    .outputEnum(pojoClasses);
        }

        //输出mapper.xml
        if (StrUtils.notEmpty(mapperRoot)) {
            MultiCodeOutputer mapperOutputer = MultiCodeOutputer.newInstance(mapperRoot, "")
                    .setClearFolderBeforeOutput(false)
                    .setGiveupOutputIfExists(false);
            Mapper[] mappers = translate(mapperOutputer, tables, tableToClassTranslator);
            mapperOutputer.output(mappers);


            //输出dao
            Interface[] interfaces = translate(mappers);
            MultiCodeOutputer.newInstance(daoRoot, daoPackageName)
                    .setClearFolderBeforeOutput(false)
                    .setGiveupOutputIfExists(true)
                    .output(interfaces);
        }
    }

    protected void init() {
        if (columnTypeTranslator == null) {
            columnTypeTranslator = new BaseColumnTypeTranslator();
            if (columnTypeReflection != null) {
                ((BaseColumnTypeTranslator)columnTypeTranslator).setReflection(columnTypeReflection);
            }
        }
        if (columnNameTranslator == null) {
            columnNameTranslator = new BaseColumnNameTranslator();
        }
        if (columnToEnumTranslator == null) {
            if (StrUtils.notEmpty(pojoEnumPackageName)) {
                columnToEnumTranslator = new ColumnToEnumTranslator(pojoEnumPackageName, NameRender.Constant) {
                    @Override
                    protected String processEnumClassName(Table table, Column column) {
                        if (enumsConfigMap != null && enumsConfigMap.size() > 0) {
                            String key = table.name + "/" + column.name;
                            String enumName = enumsConfigMap.get(key);
                            if (enumName != null) {
                                if (enumName.endsWith("Enum")) {
                                    return enumName;
                                }
                                return enumName + "Enum";
                            }
                        }
                        return super.processEnumClassName(table, column)+"Enum";
                    }
                };
            }
        }

        if (tableToClassTranslator == null) {
            tableToClassTranslator = new TableToClassTranslator(pojoPackageName, columnTypeTranslator, columnNameTranslator) {
                @Override
                protected String tableNameToClassName(String tableName) {
                    if (tableNameConfigMap.containsKey(tableName)) {
                        return tableNameConfigMap.get(tableName);
                    }
                    return super.tableNameToClassName(tableName);
                }
            };
            tableToClassTranslator.setParentClassName(pojoParentClassFullName);
            tableToClassTranslator.setIsSerializable(serializable);
            tableToClassTranslator.setTablePrefix(tablePrefix);
            tableToClassTranslator.setColumnToEnumTranslator(columnToEnumTranslator);//自动生成枚举
        }

        Consts.TAB = "    ";
        Config.addFullClass("org.springframework.stereotype.Repository");
    }

    protected void check() {
        if (StrUtils.isEmpty(pojoRoot)) {
            throw new IllegalArgumentException("参数pojoRoot不能为空");
        }
        if (StrUtils.isEmpty(daoRoot)) {
            throw new IllegalArgumentException("参数daoRoot不能为空");
        }
        if (StrUtils.isEmpty(mapperRoot)) {
            throw new IllegalArgumentException("参数mapperRoot不能为空");
        }
        if (StrUtils.isEmpty(pojoPackageName)) {
            throw new IllegalArgumentException("参数pojoPackageName不能为空");
        }
        if (StrUtils.isEmpty(daoPackageName)) {
            throw new IllegalArgumentException("参数daoPackageName不能为空");
        }
        if (connection == null) {
            throw new IllegalArgumentException("参数connection不能为空");
        }

        pojoRoot = appendRoot(pojoRoot);
        daoRoot = appendRoot(daoRoot);
        mapperRoot = appendRoot(mapperRoot);
        pojoEnumRoot = appendRoot(pojoEnumRoot);
    }

    protected String appendRoot(String path) {
        if (StrUtils.notEmpty(root) && StrUtils.notEmpty(path)
                && !path.toLowerCase().startsWith(root.toLowerCase())) {
            return root + "/" + path;
        }
        return path;
    }

    //将table转换成扩展mapper
    protected Mapper[] translate(MultiCodeOutputer mapperOutputer, Table[] tables,
                                 TableToClassTranslator tableToClassTranslator) {
        List<Mapper> list = new ArrayList<Mapper>();
        for (Table table : tables) {
            Mapper mapper = new Mapper(daoPackageName, table, tableToClassTranslator) {
                @Override
                protected String processGetListSQL() {
                    if (!PAGE_DAO_BASE_CLASS_NAME.equals(parentBaseMapperName)) {
                        return super.processGetListSQL();
                    }
                    return super.processGetListSQL() + processGetPageListSQL();
                }
                protected String processGetPageListSQL() {
                    StringBuilder sb = new StringBuilder();
                    sb.append(tab()+"<select id=\"pageQuery\" parameterType=\""+processParameterType()+"\" resultType=\""+processResultType()+"\">"+line());
                    sb.append(tab(2)+"SELECT <include refid=\""+processRequestAllFieldsSQLId()+"\"/> FROM "+table.name+""+line());
                    sb.append(tab(2)+"<include refid=\""+processWhereClauseSQLId()+"\" />"+line());
                    sb.append(tab(2)+"<include refid=\""+processExtendedOrderByClauseSQLId()+"\" />"+line());
                    sb.append(tab()+"</select>"+line());
                    sb.append(tab()+line());
                    return sb.toString();
                }
            };
            String mapperPath = mapperOutputer.getOutputablePathBuilder(mapper).build();

            CodeInputer codeInputer = new CodeInputer(mapperPath);

            mapper.setExistingMapper(codeInputer.read());
            mapper.setPrimaryKeyColumnName(primaryKeyColumnName);

            list.add(mapper);
        }
        return list.toArray(new Mapper[tables.length]);
    }

    //将mapper转换成dao的接口
    protected Interface[] translate(Mapper[] mappers) {
        List<Interface> list = new ArrayList<Interface>();
        for (Mapper mapper : mappers) {
            String parentMapperName = parentBaseMapperName.replace("<T,", "<"+mapper.pojoClass.fullClassName+",");
            Interface it = new Interface(mapper.processNamespace()).setParentInterface(new Interface(parentMapperName));
            it.setAnnotation(new Annotation("@Repository"));
            list.add(it);
        }
        return list.toArray(new Interface[mappers.length]);
    }


    public LazyCodeGenerator setRoot(String root) {
        this.root = root;
        return this;
    }

    public LazyCodeGenerator setPojoRoot(String pojoRoot) {
        this.pojoRoot = pojoRoot;
        return this;
    }

    public LazyCodeGenerator setPojoEnumRoot(String pojoEnumRoot) {
        this.pojoEnumRoot = pojoEnumRoot;
        return this;
    }

    public LazyCodeGenerator setDaoRoot(String daoRoot) {
        this.daoRoot = daoRoot;
        return this;
    }

    public LazyCodeGenerator setMapperRoot(String mapperRoot) {
        this.mapperRoot = mapperRoot;
        return this;
    }

    public LazyCodeGenerator setPojoPackageName(String pojoPackageName) {
        this.pojoPackageName = pojoPackageName;
        return this;
    }

    public LazyCodeGenerator setDaoPackageName(String daoPackageName) {
        this.daoPackageName = daoPackageName;
        return this;
    }

    public LazyCodeGenerator setPojoEnumPackageName(String pojoEnumPackageName) {
        this.pojoEnumPackageName = pojoEnumPackageName;
        return this;
    }

    public LazyCodeGenerator setPojoParentClassFullName(String pojoParentClassFullName) {
        this.pojoParentClassFullName = pojoParentClassFullName;
        return this;
    }

    public LazyCodeGenerator setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    public LazyCodeGenerator setConnection(String ip, int port, String databaseName, String username, String password) {
        this.connection = new Connection(ip, port, databaseName, username, password);
        return this;
    }

    public LazyCodeGenerator setIncludeTables(String... includeTables) {
        this.includeTables = includeTables;
        return this;
    }

    public LazyCodeGenerator setExcludeTables(String... excludeTables) {
        this.excludeTables = excludeTables;
        return this;
    }

    public LazyCodeGenerator setPrimaryKeyColumnName(String primaryKeyColumnName) {
        this.primaryKeyColumnName = primaryKeyColumnName;
        return this;
    }

    public LazyCodeGenerator setTablePrefix(String... tablePrefix) {
        this.tablePrefix = tablePrefix;
        return this;
    }

    public LazyCodeGenerator setSerializable(boolean serializable) {
        this.serializable = serializable;
        return this;
    }

    public LazyCodeGenerator setParentBaseMapperName(String parentBaseMapperName) {
        this.parentBaseMapperName = parentBaseMapperName;
        return this;
    }

    public LazyCodeGenerator setTableToClassTranslator(TableToClassTranslator tableToClassTranslator) {
        this.tableToClassTranslator = tableToClassTranslator;
        return this;
    }

    public LazyCodeGenerator setColumnTypeTranslator(ColumnTypeTranslator columnTypeTranslator) {
        this.columnTypeTranslator = columnTypeTranslator;
        return this;
    }

    public LazyCodeGenerator setColumnNameTranslator(ColumnNameTranslator columnNameTranslator) {
        this.columnNameTranslator = columnNameTranslator;
        return this;
    }

    public LazyCodeGenerator setColumnToEnumTranslator(ColumnToEnumTranslator columnToEnumTranslator) {
        this.columnToEnumTranslator = columnToEnumTranslator;
        return this;
    }

    public LazyCodeGenerator setColumnTypeReflection(String columnType, String javaType) {
        if (columnTypeReflection == null) {
            columnTypeReflection = new HashMap<String, String>();
        }
        columnTypeReflection.put(columnType, javaType);
        return this;
    }

    public LazyCodeGenerator usePageDaoBase() {
        return setParentBaseMapperName(PAGE_DAO_BASE_CLASS_NAME);
    }

    public LazyCodeGenerator putEnumConfig(String tableFieldKey, String enumClassName) {
        enumsConfigMap.put(tableFieldKey, enumClassName);
        return this;
    }

    public LazyCodeGenerator putTableNameConfig(String tableName, String className) {
        tableNameConfigMap.put(tableName, className);
        return this;
    }
}
