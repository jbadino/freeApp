(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuItemDetailController', MenuItemDetailController);

    MenuItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MenuItem', 'DayMenu'];

    function MenuItemDetailController($scope, $rootScope, $stateParams, previousState, entity, MenuItem, DayMenu) {
        var vm = this;

        vm.menuItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:menuItemUpdate', function(event, result) {
            vm.menuItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
