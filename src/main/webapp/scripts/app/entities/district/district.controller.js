'use strict';

angular.module('ppmApp')
    .controller('DistrictController', function ($scope, $http, District, Province, DistrictSearch) {
        $scope.districts = [];
        $scope.provinces = Province.query();

        $scope.showUpdate = function (id) {
            District.get({id: id}, function(result) {
                $scope.district = result;
                $('#saveDistrictModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.district.id != null) {
                District.update($scope.district,
                    function () {
                        $scope.refresh();
                    });
            } else {
                District.save($scope.district,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            District.get({id: id}, function(result) {
                $scope.district = result;
                $('#deleteDistrictConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            District.delete({id: id},
                function () {
                    $scope.search();
                    $('#deleteDistrictConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
        	$http.get('/api/districts/p/' + $scope.filterByProvince).success(function(result){$scope.districts = result;});        	
        };

        $scope.refresh = function () {
            $scope.search();
            $('#saveDistrictModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.district = {name: null, active: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
