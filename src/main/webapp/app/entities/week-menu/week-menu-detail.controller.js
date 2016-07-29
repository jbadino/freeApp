(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('WeekMenuDetailController', WeekMenuDetailController);

    WeekMenuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'WeekMenu', 'DayMenu'];

    function WeekMenuDetailController($scope, $rootScope, $stateParams, previousState, entity, WeekMenu, DayMenu) {
        var vm = this;

        vm.weekMenu = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:weekMenuUpdate', function(event, result) {
            vm.weekMenu = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
