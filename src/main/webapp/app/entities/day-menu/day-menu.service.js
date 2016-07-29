(function() {
    'use strict';
    angular
        .module('freeFoodApp')
        .factory('DayMenu', DayMenu);

    DayMenu.$inject = ['$resource'];

    function DayMenu ($resource) {
        var resourceUrl =  'api/day-menus/:id';

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
