(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuOrderDeleteController',MenuOrderDeleteController);

    MenuOrderDeleteController.$inject = ['$uibModalInstance', 'entity', 'MenuOrder'];

    function MenuOrderDeleteController($uibModalInstance, entity, MenuOrder) {
        var vm = this;

        vm.menuOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MenuOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
