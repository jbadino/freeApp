(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('DayMenuDeleteController',DayMenuDeleteController);

    DayMenuDeleteController.$inject = ['$uibModalInstance', 'entity', 'DayMenu'];

    function DayMenuDeleteController($uibModalInstance, entity, DayMenu) {
        var vm = this;

        vm.dayMenu = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DayMenu.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
