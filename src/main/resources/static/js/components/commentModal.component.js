(function () {
    'use strict';

    angular
        .module ('app')
        .component ('commentModal', {
            bindings: {
                modelComponent: '<'
            },
            templateUrl: 'js/components/commentModal.html',
            controller: ($scope) => {},
            controllerAs: '$controller'
        });

} ());