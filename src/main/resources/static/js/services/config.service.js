(function(){
    'use strict';

    angular
        .module('app')
        .service('ConfigService', Service)

    /** @ngInject */
    function Service(){

        this.URL = '/';
    }

}());