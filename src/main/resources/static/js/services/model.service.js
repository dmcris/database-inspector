(function(){
    'use strict';

    angular
        .module('app')
        .service('ModelService', Service)

    /** @ngInject */
    function Service(HttpService){

        // Obtiene el modelo completo de una base de datos
        this.getModel = function(database){
            return HttpService.get('model/' + database + '/metadata');
        }

        // Obtiene el listado de bases de datos
        this.getDatabases = function(){
            return HttpService.get('model/databases');
        }

        // Guarda los comentarios del elemento indicado
        this.saveComments = function(comment, database, schema, table, column){
            let url = 'model/' + database;
            if(schema)url += '/metadata/' + schema;
            if(table)url += '/' + table;
            if(column)url += '/' + column;
            url += '/comments';
            return HttpService.post(url, {comment: comment});
        }

        // Obtiene la información de la aplicación
        this.getInfo = function(){
            return HttpService.get('model/info');
        }
    }

}());