'use strict';

angular.module('ppmApp')
    .factory('DistrictSearch', function ($resource) {
        return $resource('api/_search/districts/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
