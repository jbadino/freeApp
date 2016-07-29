(function() {
    'use strict';

    angular
        .module('freeFoodApp')
        .controller('ProviderDetailController', ProviderDetailController);

    ProviderDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Provider'];

    function ProviderDetailController($scope, $rootScope, $stateParams, previousState, entity, Provider) {
        var vm = this;

        vm.provider = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('freeFoodApp:providerUpdate', function(event, result) {
            vm.provider = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
