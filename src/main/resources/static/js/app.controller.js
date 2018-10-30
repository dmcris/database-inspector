angular.module('app').controller('appController', function ($scope, ModelService) {
    $scope.modelo = null;
    $scope.databases = null;
    $scope.selectedDatabase;
    $scope.filtro = {
        schema: "",
        table: "",
        column: ""
    }
    $scope.numSchemas = 0;
    $scope.numTables = 0;
    $scope.porSchemaComments = 0;
    $scope.porTableComments = 0;
    $scope.porColumnComments = 0;
    $scope.info = null;


    // Obtiene la lista de bases de datos
    ModelService.getDatabases().then(
        (response) => {
            $scope.databases = response.data;
        });
    ModelService.getInfo().then(
        (response) => {
            $scope.info = response.data;
        }
    );

    // Ajusta la clase CSS para determinar si un esquema es o no visible
    $scope.collapseSchema = function (schema, event) {
        schema.show = !schema.show;
        let icon = event.target;
        if(schema.show){
            icon.innerHTML = 'expand_less';
        }else{
            icon.innerHTML = 'expand_more';
        }
    }

    // Hace scroll progresivo al elemento indicado
    $scope.scrollToElement = function (id){
        $('html, body').animate({
            scrollTop: ($('#'+id).offset().top - 165)
        },500);
    }

    // Muestra u oculta un elmento
    $scope.toogleElement = function(event){
        element = event.target;
        let collapsible = $(element).parent().find('.colapsable');
        if(collapsible.hasClass('hide')){
            collapsible.removeClass('hide');
            element.innerHTML = 'expand_less';
        }else{
            collapsible.addClass('hide');
            element.innerHTML = 'expand_more';
        } 
    }

    // Guarda el comentario del elemento
    $scope.saveComments = function(element){
        let database = element.database;
        let schema = element.schema;
        let table = element.table;
        let column = null;
        if(!element.schema){
            schema = element.name;
        }else if(!element.table){
            table = element.name;
        }else{
            column = element.name;
        }
        ModelService.saveComments(element.comment, database, schema, table, column);
    }

    // Obtiene el modelo de la base de datos seleccionada
    $scope.selectDatabase = function () {
        if ($scope.selectedDatabase != null) {
            ModelService.getModel($scope.selectedDatabase).then(
                (response) => {
                    let numSchemaComments = 0;
                    let numTableComments = 0;
                    let numColumnComments = 0;
                    let numColumns = 0;
                    $scope.modelo = response.data;
                    console.log(response);
                    $scope.numSchemas = $scope.modelo.schemas.length;
                    $scope.modelo.schemas.forEach((schema) => {
                        if(schema.comment != null)numSchemaComments++;
                        schema.show = true;
                        schema.database = $scope.selectedDatabase;
                        $scope.numTables += schema.tables.length;
                        schema.tables.forEach((table) => {
                            if(table.comment != null)numTableComments++;
                            table.schema = schema.name;
                            table.database = schema.database;
                            numColumns += table.columns.length;
                            table.columns.forEach((column) => {
                                if(column.comment != null)numColumnComments++;
                                column.database = table.database;
                                column.schema = table.schema;
                                column.table = table.name;
                            });
                        });
                    });
                    $scope.porSchemaComments = numSchemaComments/$scope.numSchemas;
                    $scope.porTableComments = numTableComments/$scope.numTables;
                    $scope.porColumnComments = numColumnComments/numColumns;
                    iniciarBootstrap();
                }
            );
        }
    }
});