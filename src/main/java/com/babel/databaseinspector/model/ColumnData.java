package com.babel.databaseinspector.model;

import lombok.Data;

@Data
public class ColumnData {
	private String schema;
	private String table;
	private String column;
}
