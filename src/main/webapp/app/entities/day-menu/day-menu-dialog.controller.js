(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('DayMenuDialogController', DayMenuDialogController);

    DayMenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DayMenu', 'WeekMenu', 'MenuItem'];

    function DayMenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DayMenu, WeekMenu, MenuItem) {
        var vm = this;

        vm.dayMenu = entity;
        vm.clear = clear;
        vm.save = save;
        vm.weekmenus = WeekMenu.query();
        vm.menuitems = MenuItem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dayMenu.id !== null) {
                DayMenu.update(vm.dayMenu, onSaveSuccess, onSaveError);
            } else {
                DayMenu.save(vm.dayMenu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('freeFoodApp:dayMenuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
