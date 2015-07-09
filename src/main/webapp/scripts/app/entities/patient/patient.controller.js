'use strict';

angular.module('ppmApp')
    .controller('PatientController', function ($scope, $filter, $http, Patient, Commune, District, Province, ExamResult, PatientSearch, ParseLinks) {
        $scope.patients = [];
        $scope.provinces = Province.query();
        $scope.districts = [];$scope.pdistricts = [];
        $scope.privateMedicalAgents = [];$scope.examResults = ExamResult.query();
        $scope.pcommunes = [];
        $scope.page = 1;
        
        var $translate = $filter('translate');
        $scope.labelYes = $translate('ppmApp.patient.yes');
        $scope.labelNo = $translate('ppmApp.patient.no');
        $scope.sexs = [
                     		{id: 1, name: $translate('ppmApp.patient.sex.female')},
                            {id: 2, name: $translate('ppmApp.patient.sex.male')}                
                     	];
        $scope.agentTypes = [
                     		{id: 1, name: $translate('ppmApp.patient.private_clinic')},
                            {id: 2, name: $translate('ppmApp.patient.private_clinic')}                
                     	];
        
        $scope.getDistrictByProvince = function() {
        	$http.get('/api/districts/p/' + $scope.filterByProvince).success(function(result){$scope.districts = result;});        	
        }
        
        $scope.getPatientDistrict = function() {
        	$http.get('/api/districts/p/' + $scope.patient.provinceId).success(function(result){$scope.pdistricts = result;});
        }
        
        $scope.getPatientCommune = function() {
        	$http.get('/api/communes/d/' + $scope.patient.districtId).success(function(result){$scope.pcommunes = result;});
        }
        
        $scope.getPrivateMedicalAgents = function(provinceId) {
        	$http.get('/api/privateMedicalAgents/p/d/c/' + provinceId + '/0/0')
        	.success(function(result){$scope.privateMedicalAgents = result;});
        }
        
        $scope.loadAll = function() {
            Patient.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.patients = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        //$scope.loadAll();

        $scope.showUpdate = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $('#savePatientModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.patient.id != null) {
                Patient.update($scope.patient,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Patient.save($scope.patient,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $('#deletePatientConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Patient.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePatientConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.search = function () {
//            PatientSearch.query({query: $scope.searchQuery}, function(result) {
//                $scope.patients = result;
//            }, function(response) {
//                if(response.status === 404) {
//                    $scope.loadAll();
//                }
//            });
        	
        	var inputDistrict = $scope.filterByDistrict ? $scope.filterByDistrict : 0;
        	$http.get('/api/patients/p/d/c/' + $scope.filterByProvince + '/' + inputDistrict + '/0')
        	.success(function(result){$scope.patients = result;});
        };

        $scope.refresh = function () {
            $scope.search();
            $('#savePatientModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.patient = {fullname: null, age: null, sex: null, communeId: null, districtId: null, provinceId: null, address: null, examDate: null, referredFrom: null, referredBy: null, phlegmTest: null, examResult: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
            
        };
        
        
       
    });
