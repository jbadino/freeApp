(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('WeekMenuDialogController', WeekMenuDialogController);

    WeekMenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'WeekMenu', 'DayMenu'];

    function WeekMenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, WeekMenu, DayMenu) {
        var vm = this;

        vm.weekMenu = entity;
        vm.clear = clear;
        vm.save = save;
        vm.daymenus = DayMenu.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.weekMenu.id !== null) {
                WeekMenu.update(vm.weekMenu, onSaveSuccess, onSaveError);
            } else {
                WeekMenu.save(vm.weekMenu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('freeFoodApp:weekMenuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
