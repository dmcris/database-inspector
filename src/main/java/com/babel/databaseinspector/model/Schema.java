package com.babel.databaseinspector.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Schema {

	@JsonIgnore
	private Map<String, Table> mTables;
	private String name;
	private String comment;
	
	Schema (String name){
		this.name = name;
		this.mTables = new LinkedHashMap<String, Table>();
	}
	
	public void addItem(ModelMetadata metadata) {
		this.name = metadata.getTableSchema();
		String key = metadata.getTableName();
		if (!mTables.containsKey(metadata.getTableName())) {
			mTables.put(key, new Table(key));
		}
		mTables.get(key).addItem(metadata);
	}
	
	public <X extends ColumnData> void addColulmnData(X data) {
		if(data instanceof ObjectComment && StringUtils.isEmpty(data.getTable())) {
			setComment(((ObjectComment)data).getComment());
		}else{
			mTables.get(data.getTable()).addColumnData(data);
		}
	}
	
	public Collection<Table> getTables(){
		return mTables.values();
	}

	public Map<String, Table> getmTables() {
		return mTables;
	}

	public void setmTables(Map<String, Table> mTables) {
		this.mTables = mTables;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
