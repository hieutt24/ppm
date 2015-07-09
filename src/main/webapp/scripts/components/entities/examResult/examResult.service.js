'use strict';

angular.module('ppmApp')
    .factory('ExamResult', function ($resource, DateUtils) {
        return $resource('api/examResults/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
