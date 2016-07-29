(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuOrderDialogController', MenuOrderDialogController);

    MenuOrderDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'MenuOrder', 'WeekMenu', 'User'];

    function MenuOrderDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, MenuOrder, WeekMenu, User) {
        var vm = this;

        vm.menuOrder = entity;
        vm.clear = clear;
        vm.save = save;
        vm.weekmenus = WeekMenu.query({filter: 'menuorder-is-null'});
        $q.all([vm.menuOrder.$promise, vm.weekmenus.$promise]).then(function() {
            if (!vm.menuOrder.weekMenu || !vm.menuOrder.weekMenu.id) {
                return $q.reject();
            }
            return WeekMenu.get({id : vm.menuOrder.weekMenu.id}).$promise;
        }).then(function(weekMenu) {
            vm.weekmenus.push(weekMenu);
        });
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.menuOrder.id !== null) {
                MenuOrder.update(vm.menuOrder, onSaveSuccess, onSaveError);
            } else {
                MenuOrder.save(vm.menuOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('freeFoodApp:menuOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
