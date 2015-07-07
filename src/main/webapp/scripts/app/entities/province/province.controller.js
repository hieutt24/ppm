'use strict';

angular.module('ppmApp')
    .controller('ProvinceController', function ($scope, Province, Region, ProvinceSearch) {
        $scope.provinces = [];
        $scope.regions = Region.query();
        $scope.loadAll = function() {
            Province.query(function(result) {
               $scope.provinces = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Province.get({id: id}, function(result) {
                $scope.province = result;
                $('#saveProvinceModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.province.id != null) {
                Province.update($scope.province,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Province.save($scope.province,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Province.get({id: id}, function(result) {
                $scope.province = result;
                $('#deleteProvinceConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Province.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteProvinceConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            ProvinceSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.provinces = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveProvinceModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.province = {name: null, active: null, id_region: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
