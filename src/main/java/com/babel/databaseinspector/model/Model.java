package com.babel.databaseinspector.model;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Model {

	@JsonIgnore
	private Map<String, Schema> mSchemas;
	
	private String comment;
	
	public Model(){
		this.mSchemas = new LinkedHashMap<String, Schema>();
	}
	
	public void addItem(ModelMetadata metadata) {
		String key = metadata.getTableSchema();
		if (!mSchemas.containsKey(key)) {
			mSchemas.put(key, new Schema(key));
		}
		mSchemas.get(key).addItem(metadata);
	}
	
	public <X extends ColumnData> void addColumnData(X data) {
		mSchemas.get(data.getSchema()).addColulmnData(data);
	}
	
	public Collection<Schema> getSchemas(){
		return mSchemas.values();
	}

	public Map<String, Schema> getmSchemas() {
		return mSchemas;
	}

	public void setmSchemas(Map<String, Schema> mSchemas) {
		this.mSchemas = mSchemas;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
	
}
