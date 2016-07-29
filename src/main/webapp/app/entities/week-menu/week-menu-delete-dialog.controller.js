(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('WeekMenuDeleteController',WeekMenuDeleteController);

    WeekMenuDeleteController.$inject = ['$uibModalInstance', 'entity', 'WeekMenu'];

    function WeekMenuDeleteController($uibModalInstance, entity, WeekMenu) {
        var vm = this;

        vm.weekMenu = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            WeekMenu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
