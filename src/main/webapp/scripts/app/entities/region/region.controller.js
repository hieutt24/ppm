'use strict';

angular.module('ppmApp')
    .controller('RegionController', function ($scope, Region, RegionSearch) {
        $scope.regions = [];
        $scope.loadAll = function() {
            Region.query(function(result) {
               $scope.regions = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Region.get({id: id}, function(result) {
                $scope.region = result;
                $('#saveRegionModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.region.id != null) {
                Region.update($scope.region,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Region.save($scope.region,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Region.get({id: id}, function(result) {
                $scope.region = result;
                $('#deleteRegionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {        	
            Region.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteRegionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
            RegionSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.regions = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveRegionModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.region = {name: null, active: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
