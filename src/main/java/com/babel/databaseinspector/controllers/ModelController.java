package com.babel.databaseinspector.controllers;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.babel.databaseinspector.model.Comment;
import com.babel.databaseinspector.model.Info;
import com.babel.databaseinspector.model.Model;
import com.babel.databaseinspector.services.ModelService;

@RestController
@RequestMapping(value="/model")
public class ModelController {

	@Resource
	private ModelService modelService;
	
	@Value("${version}")
	private String version;
	
	@Value("${host}")
	private String host;
	
	@RequestMapping(value="/{database}/metadata", method=RequestMethod.GET)
	public Model getModelMetadata(@PathVariable String database) {
		return modelService.getModelMetadata(database);
	}
	
	@RequestMapping(value="/{database}/metadata/comments", method=RequestMethod.POST)
	public void updateDatabaseComment(@RequestBody Comment comment, @PathVariable String database) {
		modelService.updateDatabaseComment(database, comment.getComment());
	}
	
	@RequestMapping(value="/{database}/metadata/{schema}/comments", method=RequestMethod.POST)
	public void updateSchemaComment(@RequestBody Comment comment, @PathVariable String database, @PathVariable String schema) {
		modelService.updateSchemaComment(database, schema, comment.getComment());
	}
	
	@RequestMapping(value="/{database}/metadata/{schema}/{table}/comments", method=RequestMethod.POST)
	public void updateTableComment(@RequestBody Comment comment, @PathVariable String database, @PathVariable String schema, @PathVariable String table) {
		modelService.updateTableComment(database, schema, table, comment.getComment());
	}
	

	@RequestMapping(value="/{database}/metadata/{schema}/{table}/{column}/comments", method=RequestMethod.POST)
	public void updateColumnComment(@RequestBody Comment comment, @PathVariable String database, @PathVariable String schema, @PathVariable String table, @PathVariable String column) {
		modelService.updateColumnComment(database, schema, table, column, comment.getComment());
	}
	
	@RequestMapping(value="/databases", method=RequestMethod.GET)
	public List<String> getDatabases(){
		return modelService.getDatabases();
	}
	
	@RequestMapping(value="/info", method=RequestMethod.GET)
	public Info getInfo() {
		return new Info(version, host);
	}
	
	
}
