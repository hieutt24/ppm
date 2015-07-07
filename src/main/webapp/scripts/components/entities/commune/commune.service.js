'use strict';

angular.module('ppmApp')
    .factory('Commune', function ($resource, DateUtils) {
        return $resource('api/communes/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.created_date = DateUtils.convertDateTimeFromServer(data.created_date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
