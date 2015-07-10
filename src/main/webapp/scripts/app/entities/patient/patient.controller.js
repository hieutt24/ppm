'use strict';

angular.module('ppmApp')
    .controller('PatientController', function ($scope, $filter, $http, Patient, Commune, District, Province, ExamResult, PatientSearch, ParseLinks) {
        $scope.patients = [];
        $scope.provinces = Province.query();
        $scope.districts = [];$scope.pdistricts = [];
        $scope.privateMedicalAgents = [];$scope.examResults = ExamResult.query();
        $scope.pcommunes = [];
        $scope.page = 1;
        $scope.patientTotal = 0;
        $scope.patientparams = {};
        var $translate = $filter('translate');
        $scope.labelYes = $translate('ppmApp.patient.yes');
        $scope.labelNo = $translate('ppmApp.patient.no');
        kendo.culture("vi-VN");	
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
        
        $scope.getPatientDistrict = function(provinceId) {
        	$http.get('/api/districts/p/' + provinceId).success(function(result){$scope.pdistricts = result;});
        }
        
        $scope.getPatientCommune = function(districtId) {
        	$http.get('/api/communes/d/' + districtId).success(function(result){$scope.pcommunes = result;});
        }
        
        $scope.getPrivateMedicalAgents = function(provinceId) {
        	$http.get('/api/privateMedicalAgents/p/d/c/' + provinceId + '/0/0')
        	.success(function(result){$scope.privateMedicalAgents = result;});
        }
        
        $scope.loadAll = function() {
            Patient.query({page: $scope.page, per_page: 3}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.patients = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.search();
        };
        //$scope.loadAll();

        $scope.showUpdate = function (id) {
            Patient.get({id: id}, function(result) {
                $scope.patient = result;
                $scope.getPatientDistrict($scope.patient.provinceId);
                $scope.getPatientCommune($scope.patient.districtId);
                $scope.getPrivateMedicalAgents($scope.patient.provinceId);
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
        	
//        	var inputDistrict = $scope.filterByDistrict ? $scope.filterByDistrict : 0;
//        	$http.get('/api/patients/p/d/c/' + $scope.filterByProvince + '/' + inputDistrict + '/0')
//        	.success(function(result){$scope.patients = result;});
        	
        	$scope.patientparams.provinceId = $scope.filterByProvince ? $scope.filterByProvince : 0;
        	$scope.patientparams.districtId = $scope.filterByDistrict ? $scope.filterByDistrict : 0;
        	$scope.patientparams.communeId = 0;
        	$scope.patientparams.offset = $scope.page;
        	$scope.patientparams.limit = 20;
        	$scope.patientparams.examResultId = $scope.filterByExamResult ? $scope.filterByExamResult : 0;
        	$scope.patientparams.exportExcel = 0;
        	$http({
        	      url: '/api/patients/search',
        	      method: "POST",
        	      data: $scope.patientparams,
        	      headers: {
        	        'Content-Type': 'application/json'
        	      }
        	    })
        	    .success(function(data, status, headers, config) {
        	    	$scope.patients = data;
                    $scope.links = ParseLinks.parse(headers('link'));
                    $scope.patientTotal = headers('GRIDTOTAL');
                })
                .error(function (data, status) {
        	      console.log('getResult error.');
        	    });
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
        
        $scope.setbgcolor = function (patient) {
        	if (patient.examResult == 2) {
                return {
                    background: "#FFC7CE"
                }
            } else if (patient.examResult == 3) {
            	return {
            		background: "#FFEB9C"
            	}
            }
        } 
        
        $scope.exportExcel = function() {
        	if ($scope.patientTotal > 0) {
        		$scope.patientparams.exportExcel = 1;
        		$http({
          	      url: '/api/patients/excel',
          	      method: "POST",
          	      data: $scope.patientparams,
          	      headers: {
          	        'Content-Type': 'application/json'
          	      }
          	    })
          	    .success(function(data, status) {
          	    	window.open("/assets/ppm_.xls",'_blank');
                  })
                  .error(function (data, status) {
          	      console.log('getResult error.');
          	    });
        	}
        }
       
    });
