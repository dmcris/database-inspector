(function () {
    'use strict';

    angular
        .module ('app')
        .component ('modelTable', {
            bindings: {
                table: '<'
            },
            templateUrl: 'js/components/modelTable.html',
            // template: '{{table.name}}',
            controller: ($scope) => {},
            controllerAs: '$controller'
        });

} ());