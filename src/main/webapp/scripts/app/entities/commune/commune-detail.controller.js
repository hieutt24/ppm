'use strict';

angular.module('ppmApp')
    .controller('CommuneDetailController', function ($scope, $stateParams, Commune) {
        $scope.commune = {};
        $scope.load = function (id) {
            Commune.get({id: id}, function(result) {
              $scope.commune = result;
            });
        };
        $scope.load($stateParams.id);
    });
