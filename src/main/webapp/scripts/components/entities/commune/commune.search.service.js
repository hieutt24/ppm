'use strict';

angular.module('ppmApp')
    .factory('CommuneSearch', function ($resource) {
        return $resource('api/_search/communes/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
