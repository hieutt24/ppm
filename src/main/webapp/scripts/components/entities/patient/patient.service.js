'use strict';

angular.module('ppmApp')
    .factory('Patient', function ($resource, DateUtils) {
        return $resource('api/patients/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.examDate = DateUtils.convertLocaleDateFromServer(data.examDate);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                	data.examDate = DateUtils.convertLocaleDateToServer(data.examDate);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.examDate = DateUtils.convertLocaleDateToServer(data.examDate);   
                    return angular.toJson(data);
                }
            }
        });
    });
