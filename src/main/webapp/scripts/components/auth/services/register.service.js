'use strict';

angular.module('ppmApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


