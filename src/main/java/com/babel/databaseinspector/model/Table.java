package com.babel.databaseinspector.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class Table {

	@JsonIgnore
	private Map<String, Column> mColumns;
	private String name;
	private String comment;
	private String pkName;

	public Table(String name) {
		this.name = name;
		this.mColumns = new LinkedHashMap<String, Column>();
	}

	public Collection<Column> getColumns() {
		return mColumns.values();
	}

	public void addItem(ModelMetadata metadata) {
		String key = metadata.getColumnName();
		if (!mColumns.containsKey(key)) {
			mColumns.put(key, new Column());
		}
		mColumns.get(key).addItem(metadata);
	}

	public <X extends ColumnData>void addColumnData(X data) {
		if (StringUtils.isEmpty(data.getColumn())) {
			if(data instanceof ObjectComment)
				this.comment = ((ObjectComment)data).getComment();
		} else {
			mColumns.get(data.getColumn()).addColumnData(data);
			if(data instanceof PrimaryKey) {
				PrimaryKey pk = (PrimaryKey)data;
				this.pkName = pk.getPkName();
			}
		}
	}
}
