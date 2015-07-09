'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('patient', {
                parent: 'entity',
                url: '/patient',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.patient.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patients.html',
                        controller: 'PatientController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('patientDetail', {
                parent: 'entity',
                url: '/patient/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.patient.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/patient/patient-detail.html',
                        controller: 'PatientDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('patient');
                        return $translate.refresh();
                    }]
                }
            });
    });
