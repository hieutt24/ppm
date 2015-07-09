'use strict';

angular.module('ppmApp')
    .controller('ExamResultController', function ($scope, ExamResult, ExamResultSearch) {
        $scope.examResults = [];
        $scope.loadAll = function() {
            ExamResult.query(function(result) {
               $scope.examResults = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            ExamResult.get({id: id}, function(result) {
                $scope.examResult = result;
                $('#saveExamResultModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.examResult.id != null) {
                ExamResult.update($scope.examResult,
                    function () {
                        $scope.refresh();
                    });
            } else {
                ExamResult.save($scope.examResult,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            ExamResult.get({id: id}, function(result) {
                $scope.examResult = result;
                $('#deleteExamResultConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ExamResult.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteExamResultConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ExamResultSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.examResults = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveExamResultModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.examResult = {name: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
