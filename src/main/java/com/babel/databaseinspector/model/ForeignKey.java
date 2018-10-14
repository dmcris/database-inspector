package com.babel.databaseinspector.model;

import lombok.Data;

@Data
public class ForeignKey extends ColumnData{

	private String referencedTable;
	private String referencedColumn;
	private int fkPart;
}
