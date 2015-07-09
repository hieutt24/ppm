'use strict';

angular.module('ppmApp')
    .controller('PatientDetailController', function ($scope, $stateParams, Patient) {
        $scope.patient = {};
        $scope.load = function (id) {
            Patient.get({id: id}, function(result) {
              $scope.patient = result;
            });
        };
        $scope.load($stateParams.id);
    });
