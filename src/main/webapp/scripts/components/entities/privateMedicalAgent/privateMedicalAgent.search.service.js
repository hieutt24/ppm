'use strict';

angular.module('ppmApp')
    .factory('PrivateMedicalAgentSearch', function ($resource) {
        return $resource('api/_search/privateMedicalAgents/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
