package com.sinotopia.fundamental.codegenerator.template;

import com.sinotopia.fundamental.codegenerator.basis.data.Clazz;
import com.sinotopia.fundamental.codegenerator.basis.data.Interface;
import com.sinotopia.fundamental.codegenerator.basis.data.db.Column;
import com.sinotopia.fundamental.codegenerator.basis.data.db.Mapper;
import com.sinotopia.fundamental.codegenerator.basis.data.db.Table;
import com.sinotopia.fundamental.codegenerator.basis.translator.ColumnNameTranslator;
import com.sinotopia.fundamental.codegenerator.basis.translator.ColumnTypeTranslator;
import com.sinotopia.fundamental.codegenerator.basis.translator.TableToClassTranslator;
import com.sinotopia.fundamental.codegenerator.io.Outputable;

import java.util.ArrayList;
import java.util.List;

/**
 * 使用模板生成的mapper对象
 */
public class TemplateMapper extends Mapper implements Outputable {

    public Interface daoInterface;
    public ColumnField[] columnFields;
    public String templateFilePath;

    public String tableName;
    public String namespace;
    public String fieldClause;
    public String whereClause;
    public String insertParams;
    public String insertValues;
    public String updateClause;
    public String parameterType;
    public String resultType;

    public TemplateMapper(Table table, Interface daoInterface, String templateFilePath, TableToClassTranslator tableToClassTranslator) {
        super(daoInterface.fullClassName, table, tableToClassTranslator);
        this.daoInterface = daoInterface;
        this.templateFilePath = templateFilePath;
        this.columnFields = processColumnFields(table.columns, tableToClassTranslator);

        this.tableName = table.name;
        this.namespace = daoInterface.fullClassName;
        this.parameterType = pojoClass.fullClassName;
        this.resultType = pojoClass.fullClassName;
        this.fieldClause = processRequestAllFieldsItemsSQL();
        this.whereClause = processWhereClauseItemsSQL();
        this.insertParams = processInsertParamsItemsSQL();
        this.insertValues = processInsertValuesItemsSQL();
        this.updateClause = processUpdateSetterItemsSQL();
    }

    private ColumnField[] processColumnFields(Column[] columns, TableToClassTranslator tableToClassTranslator) {
        ColumnNameTranslator columnNameTranslator = tableToClassTranslator.getColumnNameTranslator();
        ColumnTypeTranslator columnTypeTranslator = tableToClassTranslator.getColumnTypeTranslator();

        List<ColumnField> columnFieldList = new ArrayList<ColumnField>();
        for (Column column : columns) {
            String fieldName = columnNameTranslator.translate(column);
            String fieldType = columnTypeTranslator.translate(column);
            columnFieldList.add(new ColumnField(column.type, column.name, fieldType, fieldName));
        }
        return columnFieldList.toArray(new ColumnField[columnFieldList.size()]);
    }

    @Override
    public String toString() {
        return TemplateProcessor.newInstance(templateFilePath).toString(this);
    }

    public Table getTable() {
        return table;
    }

    public Interface getDaoInterface() {
        return daoInterface;
    }

    public Clazz getPojoClass() {
        return pojoClass;
    }

    public ColumnField[] getColumnFields() {
        return columnFields;
    }

    public String getTemplateFilePath() {
        return templateFilePath;
    }

    public String getFieldClause() {
        return fieldClause;
    }

    public String getWhereClause() {
        return whereClause;
    }

    public String getInsertParams() {
        return insertParams;
    }

    public String getInsertValues() {
        return insertValues;
    }

    public String getUpdateClause() {
        return updateClause;
    }

    public String getTableName() {
        return tableName;
    }

    public String getNamespace() {
        return namespace;
    }

    public String getParameterType() {
        return parameterType;
    }

    public String getResultType() {
        return resultType;
    }
}
