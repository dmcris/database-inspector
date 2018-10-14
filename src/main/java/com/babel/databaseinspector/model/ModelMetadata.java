package com.babel.databaseinspector.model;

import lombok.Data;

@Data
public class ModelMetadata {
	private String tableSchema;
	private String tableName;
	private String columnName;
	private Integer ordinalPosition;
	private String dataType;
	private Boolean isIdentity;
	private String columnDefault;
	private Boolean isNullable;
}
