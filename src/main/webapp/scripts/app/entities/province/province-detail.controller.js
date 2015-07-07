'use strict';

angular.module('ppmApp')
    .controller('ProvinceDetailController', function ($scope, $stateParams, Province, Region) {
        $scope.province = {};
        $scope.load = function (id) {
            Province.get({id: id}, function(result) {
              $scope.province = result;
            });
        };
        $scope.load($stateParams.id);
    });
