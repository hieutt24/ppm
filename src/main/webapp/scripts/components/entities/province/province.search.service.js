'use strict';

angular.module('ppmApp')
    .factory('ProvinceSearch', function ($resource) {
        return $resource('api/_search/provinces/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
