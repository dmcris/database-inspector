package com.babel.databaseinspector.database;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.babel.databaseinspector.model.ForeignKey;
import com.babel.databaseinspector.model.ModelMetadata;
import com.babel.databaseinspector.model.ObjectComment;
import com.babel.databaseinspector.model.PrimaryKey;

@Component
public class ModelDao {
	@Resource
	private JdbcTemplate jdbc;
	private String currentDatabase = "";
	
	private static final String SQL_TABLES = "select tables.TABLE_SCHEMA, " + 
						 "tables.TABLE_NAME," + 
						 "columns.COLUMN_NAME, " + 
						 "columns.ORDINAL_POSITION," + 
						 "concat(isnull(columns.DATA_TYPE, ''), case when columns.CHARACTER_MAXIMUM_LENGTH = -1 then '(MAX)'" + 
						 "										     when columns.CHARACTER_MAXIMUM_LENGTH is null then ''" + 
						 "											 else concat('(', columns.CHARACTER_MAXIMUM_LENGTH, ')') end) \"DATA_TYPE\", " + 
						 "columnproperty(object_id(concat(tables.TABLE_SCHEMA, '.', tables.TABLE_NAME)),column_name ,'IsIdentity') \"ISIDENTITY\"," + 
						 "columns.COLUMN_DEFAULT, " + 
						 "case isnull(columns.IS_NULLABLE, '') when 'YES' then 1 else 0 end \"IS_NULLABLE\"" + 
						 "from INFORMATION_SCHEMA.TABLES tables" + 
						 "	   inner join INFORMATION_SCHEMA.COLUMNS columns on (tables.TABLE_SCHEMA = columns.TABLE_SCHEMA and tables.TABLE_NAME = columns.TABLE_NAME)" + 
						 "order by tables.table_schema, tables.table_type, tables.table_name, " + 
						 "		columns.ORDINAL_POSITION";
	private static final String SQL_COMMENTS = "SELECT s.name \"schema\", t.name \"table\", '' \"column\", cast(td.value AS NVARCHAR(512)) \"comment\"\r\n" + 
			"FROM sys.schemas s\r\n" + 
			"JOIN sys.tables t ON s.schema_id = t.schema_id\r\n" + 
			"LEFT OUTER JOIN sys.extended_properties td ON td.major_id = t.object_id AND td.minor_id = 0 AND td.name = 'MS_Description'\r\n" + 
			"WHERE t.type = 'u'\r\n" + 
			"union\r\n" + 
			"SELECT s.name \"schema\", t.name \"table\", c.name \"column\", cast(td.value AS NVARCHAR(512)) \"comment\"\r\n" + 
			"FROM sys.schemas s\r\n" + 
			"JOIN sys.tables t ON s.schema_id = t.schema_id\r\n" + 
			"JOIN sys.columns c ON c.object_id = t.object_id\r\n" + 
			"LEFT OUTER JOIN sys.extended_properties td ON td.major_id = t.object_id AND td.minor_id = c.column_id AND td.name = 'MS_Description'\r\n" + 
			"WHERE t.type = 'u'";
	
	private static final String SQL_PRIMARY_KEYS = "select constraints.TABLE_SCHEMA \"schema\", \r\n" + 
			"	   constraints.TABLE_NAME \"table\", \r\n" + 
			"	   constraints.CONSTRAINT_NAME \"pk_name\",\r\n" + 
			"	   keyColumn.COLUMN_NAME \"column\"\r\n" + 
			"from INFORMATION_SCHEMA.TABLE_CONSTRAINTS constraints\r\n" + 
			"join INFORMATION_SCHEMA.KEY_COLUMN_USAGE keyColumn on \r\n" + 
			"	 keyColumn.TABLE_SCHEMA = constraints.CONSTRAINT_SCHEMA and keyColumn.TABLE_NAME = constraints.TABLE_NAME and keyColumn.CONSTRAINT_NAME = constraints.CONSTRAINT_NAME\r\n" + 
			"where constraints.constraint_type = 'PRIMARY KEY'\r\n" + 
			"order by constraints.table_schema, constraints.table_name, constraints.CONSTRAINT_NAME";
	private static final String SQL_FOREIGN_KEYS = "select  s.name \"schema\",\r\n" + 
			"    t.name \"table\", \r\n" + 
			"	c.name \"column\", \r\n" + 
			"	rt.name \"referenced_table\", \r\n" + 
			"	rc.name \"referenced_column\",\r\n" + 
			"    fk.constraint_column_id as \"fk_part\"\r\n" + 
			"from sys.foreign_key_columns as fk\r\n" + 
			"join sys.tables as t on fk.parent_object_id = t.object_id\r\n" + 
			"join sys.schemas as s on t.schema_id = s.schema_id\r\n" + 
			"join sys.columns as c on fk.parent_object_id = c.object_id and fk.parent_column_id = c.column_id\r\n" + 
			"left join sys.tables as rt on fk.referenced_object_id = rt.object_id\r\n" + 
			"left join sys.columns as rc on fk.referenced_object_id = rc.object_id and fk.referenced_column_id = rc.column_id\r\n" + 
			"order by t.name, c.column_id";
	
	private static final String  ADD_TABLE_COMMENT_SQL = "exec sys.sp_addextendedproperty 'MS_Description', ?, 'SCHEMA', ?, 'table', ? ";
	private static final String  UPDATE_TABLE_COMMENT_SQL = "exec sys.sp_updateextendedproperty 'MS_Description', ?, 'SCHEMA', ?, 'table', ? ";
	private static final String  EXISTS_TABLE_COMMENT_SQL = "select count(0) from sys.schemas s join sys.tables t on s.schema_id = t.schema_id join sys.extended_properties ep on t.object_id = ep.major_id AND ep.minor_id = 0 and ep.name = 'MS_Description' where s.name = ? and t.name= ?";
	private static final String  EXISTS_COLUMN_COMMENT_SQL = "select count(0) from sys.schemas s join sys.tables t on s.schema_id = t.schema_id join sys.columns c on t.object_id = c.object_id join sys.extended_properties ep on t.object_id = ep.major_id AND ep.minor_id = c.column_id and ep.name = 'MS_Description' where s.name = ? and t.name = ? and c.name = ? ";
	private static final String  ADD_COLUMN_COMMENT_SQL = ADD_TABLE_COMMENT_SQL + ", 'column', ? ";
	private static final String  UPDATE_COLUMN_COMMENT_SQL = UPDATE_TABLE_COMMENT_SQL + ", 'column', ? ";
	
	public List<ModelMetadata> searchModelMetadata(String database) {
		useDatabase(database);
		List<ModelMetadata> rows = jdbc.query(SQL_TABLES, BeanPropertyRowMapper.newInstance(ModelMetadata.class));
		return rows;
	}
	
	public List<ObjectComment> getComents(String database){
		useDatabase(database);
		return jdbc.query(SQL_COMMENTS, BeanPropertyRowMapper.newInstance(ObjectComment.class));		
	}

	public List<PrimaryKey> getPrimaryKeys(String database){
		useDatabase(database);
		return jdbc.query(SQL_PRIMARY_KEYS, BeanPropertyRowMapper.newInstance(PrimaryKey.class));		
	}
	
	public List<ForeignKey> getForeignKeys(String database){
		useDatabase(database);
		return jdbc.query(SQL_FOREIGN_KEYS, BeanPropertyRowMapper.newInstance(ForeignKey.class));		
	}
	
	
	public void updateTableComment(String database, String schema, String table, String comment) {
		useDatabase(database);
		boolean exists = jdbc.queryForObject(EXISTS_TABLE_COMMENT_SQL, Integer.class, schema, table) > 0;
		if(exists) {
			jdbc.update(UPDATE_TABLE_COMMENT_SQL, comment, schema, table);
		}else {
			jdbc.update(ADD_TABLE_COMMENT_SQL, comment, schema, table);
		}
	}
	
	public void updateColumnComment(String database, String schema, String table, String column, String comment) {
		useDatabase(database);
		boolean exists = jdbc.queryForObject(EXISTS_COLUMN_COMMENT_SQL, Integer.class, schema, table, column) > 0;
		if(exists) {
			jdbc.update(UPDATE_COLUMN_COMMENT_SQL, comment, schema, table, column);
		}else {
			jdbc.update(ADD_COLUMN_COMMENT_SQL, comment, schema, table, column);
		}
	}
	
	private void useDatabase(String database) {
		if(!database.equals(currentDatabase)) {
			jdbc.execute("USE " + database);
			this.currentDatabase = database;
		}
	}
	
}
