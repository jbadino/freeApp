(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuItemDialogController', MenuItemDialogController);

    MenuItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MenuItem', 'DayMenu'];

    function MenuItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MenuItem, DayMenu) {
        var vm = this;

        vm.menuItem = entity;
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
            if (vm.menuItem.id !== null) {
                MenuItem.update(vm.menuItem, onSaveSuccess, onSaveError);
            } else {
                MenuItem.save(vm.menuItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('freeFoodApp:menuItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
