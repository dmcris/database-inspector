package com.babel.databaseinspector.services;

import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.babel.databaseinspector.database.ModelDao;
import com.babel.databaseinspector.model.ForeignKey;
import com.babel.databaseinspector.model.Model;
import com.babel.databaseinspector.model.ModelMetadata;
import com.babel.databaseinspector.model.ObjectComment;
import com.babel.databaseinspector.model.PrimaryKey;

@Service
public class ModelService {

	@Resource
	private ModelDao dao;
	
	@Value("${model.scannedDatabases}")
	private String scannedDatabases;
	
	private List<String> databases;
	
	@PostConstruct
	public void init() {
		String[] aDatabases = scannedDatabases.split(",");
		databases = Arrays.asList(aDatabases);
	}
	
	/**
	 * Obtiene el modelo de la base de datos indicada
	 * @param database
	 * @return
	 */
	public Model getModelMetadata(String database) {
		Model model = new Model();
		List<ModelMetadata> modelMetadata = dao.searchModelMetadata(database);
		for (ModelMetadata metadata : modelMetadata) {
			model.addItem(metadata);
		}
		List<ObjectComment> comments = dao.getComents(database);
		for (ObjectComment comment : comments) {
			model.addColumnData(comment);
		}
		List<PrimaryKey> pks = dao.getPrimaryKeys(database);
		for (PrimaryKey pk : pks) {
			model.addColumnData(pk);
		}
		List<ForeignKey> fks = dao.getForeignKeys(database);
		for (ForeignKey fk : fks) {
			model.addColumnData(fk);
		}
		return model;	
	}
	
	/**
	 * Actualiza el comentario de la tabla indicada
	 * @param schema
	 * @param table
	 * @param comment
	 */
	public void updateTableComment(String database, String schema, String table, String comment) {
		dao.updateTableComment(database, schema, table, comment);
	}
	
	/**
	 * Actualiza el comentario de la columna indicada
	 * @param database
	 * @param schema
	 * @param table
	 * @param column
	 * @param comment
	 */
	public void updateColumnComment(String database, String schema, String table, String column, String comment) {
		dao.updateColumnComment(database, schema, table, column, comment);
	}
	
	/**
	 * Devuelve la lista de bases de datos escaneadas
	 * @return
	 */
	public List<String> getDatabases(){
		return databases;
	}
	
	
}
