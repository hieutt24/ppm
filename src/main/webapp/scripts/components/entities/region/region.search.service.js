'use strict';

angular.module('ppmApp')
    .factory('RegionSearch', function ($resource) {
        return $resource('api/_search/regions/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
