(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('WeekMenuController', WeekMenuController);

    WeekMenuController.$inject = ['$scope', '$state', 'WeekMenu'];

    function WeekMenuController ($scope, $state, WeekMenu) {
        var vm = this;
        
        vm.weekMenus = [];

        loadAll();

        function loadAll() {
            WeekMenu.query(function(result) {
                vm.weekMenus = result;
            });
        }
    }
})();
