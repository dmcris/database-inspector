(function () {
    'use strict';

    angular
        .module ('app')
        .component ('modelTable', {
            bindings: {
                table: '<',
                filtro: '<',
                onSaveComments: '&'
            },
            templateUrl: 'js/components/modelTable/modelTable.html',
            controller: ($scope) => {
                
            },
            controllerAs: '$controller'
        });

} ());