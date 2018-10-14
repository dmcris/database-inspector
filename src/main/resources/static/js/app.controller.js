angular.module('app').controller('appController', function ($scope, ModelService, UtilService) {
    $scope.ejemplo = 'Hola mundo';
    $scope.modelo = null;
    $scope.databases = null;
    $scope.selectedDatabase;
    $scope.filtro = {
        schema: "",
        table: ""
    }

    // Obtiene la lista de bases de datos
    ModelService.getDatabases().then(
        (response) => {
            $scope.databases = response.data;
        });

    // Ajusta la clase CSS para determinar si un esquema es o no visible
    $scope.collapseSchema = function (schema) {
        schema.show = !schema.show;
    }

    // Obtiene el modelo de la base de datos seleccionada
    $scope.selectDatabase = function () {
        if ($scope.selectedDatabase != null) {
            ModelService.getModel($scope.selectedDatabase).then(
                (response) => {
                    $scope.modelo = response.data;
                    console.log(response);
                    $scope.modelo.schemas.forEach((schema) => {
                        schema.show = true;
                    });
                    iniciarBootstrap();
                },
                (error) => { console.log(error) },
            );
        }
    }
});