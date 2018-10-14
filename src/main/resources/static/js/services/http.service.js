(function(){
    'use strict';

    angular
        .module('app')
        .service('HttpService', Service)

    /** @ngInject */
    function Service($http, ConfigService, UtilService){

        // Get con comportamiento genérico
        this.get = function(url, errorFunction, finallyFunction){
            return catchFinally($http.get(ConfigService.URL + url), errorFunction, finallyFunction);
        }

        // Post con comportamiento genérico
        this.post = function(url, body, errorFunction, finallyFunction){
            return catchFinally($http.post(ConfigService.URL + url, body), errorFunction, finallyFunction);
        }

        // Añade el comportamiento genérico de cualquier petición AJAX
        function catchFinally(promise, errorFunction, finallyFunction){
            UtilService.showMask();
            return promise.catch((error) => {
                console.log(error);
                if(errorFunction != null){
                    errorFunction(error);
                }
            }).finally(() => {
                UtilService.hideMask();
                if(finallyFunction != null){
                    finallyFunction();
                }
            });
        }
    }

}());