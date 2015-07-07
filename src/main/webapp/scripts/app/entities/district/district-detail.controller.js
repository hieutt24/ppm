'use strict';

angular.module('ppmApp')
    .controller('DistrictDetailController', function ($scope, $stateParams, District, Province) {
        $scope.district = {};
        $scope.load = function (id) {
            District.get({id: id}, function(result) {
              $scope.district = result;
            });
        };
        $scope.load($stateParams.id);
    });
