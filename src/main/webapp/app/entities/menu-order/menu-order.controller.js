(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuOrderController', MenuOrderController);

    MenuOrderController.$inject = ['$scope', '$state', 'MenuOrder'];

    function MenuOrderController ($scope, $state, MenuOrder) {
        var vm = this;
        
        vm.menuOrders = [];

        loadAll();

        function loadAll() {
            MenuOrder.query(function(result) {
                vm.menuOrders = result;
            });
        }
    }
})();
