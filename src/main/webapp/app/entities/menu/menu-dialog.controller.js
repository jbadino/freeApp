(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuDialogController', MenuDialogController);

    MenuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Menu', 'WeekMenu', 'Provider'];

    function MenuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Menu, WeekMenu, Provider) {
        var vm = this;

        vm.menu = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.weekmenus = WeekMenu.query({filter: 'menu-is-null'});
        $q.all([vm.menu.$promise, vm.weekmenus.$promise]).then(function() {
            if (!vm.menu.weekMenu || !vm.menu.weekMenu.id) {
                return $q.reject();
            }
            return WeekMenu.get({id : vm.menu.weekMenu.id}).$promise;
        }).then(function(weekMenu) {
            vm.weekmenus.push(weekMenu);
        });
        vm.providers = Provider.query({filter: 'menu-is-null'});
        $q.all([vm.menu.$promise, vm.providers.$promise]).then(function() {
            if (!vm.menu.provider || !vm.menu.provider.id) {
                return $q.reject();
            }
            return Provider.get({id : vm.menu.provider.id}).$promise;
        }).then(function(provider) {
            vm.providers.push(provider);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.menu.id !== null) {
                Menu.update(vm.menu, onSaveSuccess, onSaveError);
            } else {
                Menu.save(vm.menu, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('freeFoodApp:menuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.weekDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
