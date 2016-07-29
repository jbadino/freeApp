(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('DayMenuController', DayMenuController);

    DayMenuController.$inject = ['$scope', '$state', 'DayMenu'];

    function DayMenuController ($scope, $state, DayMenu) {
        var vm = this;
        
        vm.dayMenus = [];

        loadAll();

        function loadAll() {
            DayMenu.query(function(result) {
                vm.dayMenus = result;
            });
        }
    }
})();
