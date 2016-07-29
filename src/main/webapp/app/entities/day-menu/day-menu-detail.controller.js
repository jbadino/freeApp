(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('DayMenuDetailController', DayMenuDetailController);

    DayMenuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DayMenu', 'WeekMenu', 'MenuItem'];

    function DayMenuDetailController($scope, $rootScope, $stateParams, previousState, entity, DayMenu, WeekMenu, MenuItem) {
        var vm = this;

        vm.dayMenu = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:dayMenuUpdate', function(event, result) {
            vm.dayMenu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
