(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('MenuDetailController', MenuDetailController);

    MenuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Menu', 'WeekMenu', 'Provider'];

    function MenuDetailController($scope, $rootScope, $stateParams, previousState, entity, Menu, WeekMenu, Provider) {
        var vm = this;

        vm.menu = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:menuUpdate', function(event, result) {
            vm.menu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
