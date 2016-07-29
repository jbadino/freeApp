(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('ProviderController', ProviderController);

    ProviderController.$inject = ['$scope', '$state', 'Provider'];

    function ProviderController ($scope, $state, Provider) {
        var vm = this;
        
        vm.providers = [];

        loadAll();

        function loadAll() {
            Provider.query(function(result) {
                vm.providers = result;
            });
        }
    }
})();
