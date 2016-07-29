(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuOrderDetailController', MenuOrderDetailController);

    MenuOrderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MenuOrder', 'WeekMenu', 'User'];

    function MenuOrderDetailController($scope, $rootScope, $stateParams, previousState, entity, MenuOrder, WeekMenu, User) {
        var vm = this;

        vm.menuOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:menuOrderUpdate', function(event, result) {
            vm.menuOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
