(function(){
    'use strict';

    angular
        .module('app')
        .service('UtilService', UtilService)

    /** @ngInject */
    function UtilService(){

        this.showMask = function(){
            $(".loading-mask").show();
        }

        this.hideMask = function(){
            $(".loading-mask").hide();
        }
    }

}());