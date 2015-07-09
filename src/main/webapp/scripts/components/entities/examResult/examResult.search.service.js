'use strict';

angular.module('ppmApp')
    .factory('ExamResultSearch', function ($resource) {
        return $resource('api/_search/examResults/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
