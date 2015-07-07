'use strict';

angular.module('ppmApp')
    .controller('CommuneController', function ($scope, $http, $resource, Commune, District, Province, CommuneSearch) {
        $scope.communes = [];
        $scope.provinces = Province.query();
        $scope.districts = [];
        
        $scope.getDistrictByProvince = function() {
        	$http.get('/api/districts/p/' + $scope.filterByProvince).success(function(result){$scope.districts = result;});
        }
        
        $scope.showUpdate = function (id) {
            Commune.get({id: id}, function(result) {
                $scope.commune = result;
                $scope.province = $scope.filterByProvince;
                $scope.district = $scope.filterByDistrict;
                $('#saveCommuneModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.commune.id != null) {
                Commune.update($scope.commune,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Commune.save($scope.commune,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Commune.get({id: id}, function(result) {
                $scope.commune = result;
                $('#deleteCommuneConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Commune.delete({id: id},
                function () {
                    $scope.search();
                    $('#deleteCommuneConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
        	$http.get('/api/communes/d/' + $scope.filterByDistrict).success(function(result){$scope.communes = result;});
        };

        $scope.refresh = function () {
            $scope.search();
            $('#saveCommuneModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.commune = {name: null, districtId: null, active: null, createdDate: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
            $scope.province = $scope.filterByProvince;
            $scope.district = $scope.filterByDistrict;
            $scope.commune.districtId = $scope.filterByDistrict;
        };
    });
