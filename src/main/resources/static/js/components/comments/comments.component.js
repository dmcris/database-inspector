(function () {
    'use strict';

    angular
        .module('app')
        .component('comments', {
            bindings: {
                element: '<',
                onSave: '&'
            },
            templateUrl: 'js/components/comments/comments.html',
            controller: ($scope) => {
                var ctrl = $scope;
                // Alterna entre los modos de edición y visualización de los comentarios
                $scope.toogleComment = function (element, saveFunction) {
                    if (element.editComment == null) element.editComment = true;
                    else element.editComment = !element.editComment;
                    if(saveFunction)saveFunction(element);
                }
            },
            controllerAs: '$controller'
        });

}());