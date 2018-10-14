(function(){
    'use strict';

    angular
        .module('app')
        .service('ModelService', Service)

    /** @ngInject */
    function Service(HttpService){

        this.getModel = function(database){
            return HttpService.get('model/' + database + '/metadata');
        }

        this.getDatabases = function(){
            return HttpService.get('model/databases');
        }
    }

}());