(function() {
    'use strict';
    angular
        .module('freeFoodApp')
        .factory('WeekMenu', WeekMenu);

    WeekMenu.$inject = ['$resource'];

    function WeekMenu ($resource) {
        var resourceUrl =  'api/week-menus/:id';

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
