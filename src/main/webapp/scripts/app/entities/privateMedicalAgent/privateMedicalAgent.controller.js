'use strict';

angular.module('ppmApp')
    .controller('PrivateMedicalAgentController', function ($scope, $http, $filter,PrivateMedicalAgent, Commune, District, Province, PrivateMedicalAgentSearch) {
        $scope.privateMedicalAgents = [];        
        $scope.provinces = Province.query();
        $scope.districts = [];$scope.communes = [];
        var contextPath = "/ppm/api/";
        var $translate = $filter('translate');
        $scope.agentTypes = [
                      		{id: 1, name: $translate('ppmApp.privateMedicalAgent.private_clinic')},
                              {id: 2, name: $translate('ppmApp.privateMedicalAgent.private_hospital')}                
                      	];
        
        $scope.getDistrictByProvince = function() {
        	$http.get(contextPath + 'districts/p/' + $scope.filterByProvince).success(function(result){$scope.districts = result;});
        }
        
        $scope.getCommuneByDistrict = function(districtId) {
        	$http.get(contextPath + 'communes/d/' + districtId).success(function(result){$scope.communes = result;});
        }        

        $scope.showUpdate = function (id) {
            PrivateMedicalAgent.get({id: id}, function(result) {
                $scope.privateMedicalAgent = result;
                $('#savePrivateMedicalAgentModal').modal('show');
                $scope.getCommuneByDistrict($scope.privateMedicalAgent.districtId);
            });
        };

        $scope.save = function () {
            if ($scope.privateMedicalAgent.id != null) {
                PrivateMedicalAgent.update($scope.privateMedicalAgent,
                    function () {
                        $scope.refresh();
                    });
            } else {
                PrivateMedicalAgent.save($scope.privateMedicalAgent,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            PrivateMedicalAgent.get({id: id}, function(result) {
                $scope.privateMedicalAgent = result;
                $('#deletePrivateMedicalAgentConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PrivateMedicalAgent.delete({id: id},
                function () {
                    $scope.search();
                    $('#deletePrivateMedicalAgentConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
        	var inputDistrict = $scope.filterByDistrict ? $scope.filterByDistrict : 0;
        	$http.get(contextPath + 'privateMedicalAgents/p/d/c/' + $scope.filterByProvince + '/' + inputDistrict + '/0')
        	.success(function(result){$scope.privateMedicalAgents = result;});
        };

        $scope.refresh = function () {
            $scope.search();
            $('#savePrivateMedicalAgentModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.privateMedicalAgent = {name: null, type: null, communeId: null, districtId: null, provinceId: null, active: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
            $scope.privateMedicalAgent.provinceId = $scope.filterByProvince;
        };
    });
