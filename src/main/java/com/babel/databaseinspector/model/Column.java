package com.babel.databaseinspector.model;

import lombok.Data;

@Data	
public class Column {
	private String columnName;
	private Integer ordinalPosition;
	private String dataType;
	private Boolean isIdentity;
	private String columnDefault;
	private Boolean isNullable;
	private Boolean isPrimaryKey;
	private String comment;
	private Boolean isForeignKey;
	private String fkReferencedTable;
	private String fkReferencedColumn;
	
	public void addItem(ModelMetadata metadata) {
		this.columnName = metadata.getColumnName();
		this.ordinalPosition = metadata.getOrdinalPosition();
		this.dataType = metadata.getDataType();
		this.isIdentity = metadata.getIsIdentity();
		this.columnDefault = metadata.getColumnDefault();
		this.isNullable = metadata.getIsNullable();
		this.isPrimaryKey = Boolean.FALSE;
		this.isForeignKey = Boolean.FALSE;
	}
	
	public <X extends ColumnData>void addColumnData(X data) {
		if(data instanceof ObjectComment)
			this.comment = ((ObjectComment)data).getComment();
		else if(data instanceof ForeignKey) {
			ForeignKey fk = (ForeignKey)data;
			this.isForeignKey = true;
			this.fkReferencedTable = fk.getReferencedTable();
			this.fkReferencedColumn = fk.getReferencedColumn();
		}else if(data instanceof PrimaryKey) {
			this.isPrimaryKey = true;
		}
			
	}
}
