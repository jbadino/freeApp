(function() {
    'use strict';
    angular
        .module('freeFoodApp')
        .factory('MenuOrder', MenuOrder);

    MenuOrder.$inject = ['$resource'];

    function MenuOrder ($resource) {
        var resourceUrl =  'api/menu-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
