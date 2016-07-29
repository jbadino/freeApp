(function() {
    'use strict';
    angular
        .module('freeFoodApp')
        .factory('Menu', Menu);

    Menu.$inject = ['$resource', 'DateUtils'];

    function Menu ($resource, DateUtils) {
        var resourceUrl =  'api/menus/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.weekDate = DateUtils.convertLocalDateFromServer(data.weekDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.weekDate = DateUtils.convertLocalDateToServer(data.weekDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.weekDate = DateUtils.convertLocalDateToServer(data.weekDate);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
