'use strict';

angular.module('ppmApp')
    .controller('ExamResultDetailController', function ($scope, $stateParams, ExamResult) {
        $scope.examResult = {};
        $scope.load = function (id) {
            ExamResult.get({id: id}, function(result) {
              $scope.examResult = result;
            });
        };
        $scope.load($stateParams.id);
    });
