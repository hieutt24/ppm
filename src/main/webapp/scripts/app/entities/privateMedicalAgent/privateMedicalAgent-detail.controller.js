'use strict';

angular.module('ppmApp')
    .controller('PrivateMedicalAgentDetailController', function ($scope, $stateParams, PrivateMedicalAgent) {
        $scope.privateMedicalAgent = {};
        $scope.load = function (id) {
            PrivateMedicalAgent.get({id: id}, function(result) {
              $scope.privateMedicalAgent = result;
            });
        };
        $scope.load($stateParams.id);
    });
