'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('privateMedicalAgent', {
                parent: 'entity',
                url: '/privateMedicalAgent',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.privateMedicalAgent.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/privateMedicalAgent/privateMedicalAgents.html',
                        controller: 'PrivateMedicalAgentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('privateMedicalAgent');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('privateMedicalAgentDetail', {
                parent: 'entity',
                url: '/privateMedicalAgent/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.privateMedicalAgent.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/privateMedicalAgent/privateMedicalAgent-detail.html',
                        controller: 'PrivateMedicalAgentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('privateMedicalAgent');
                        return $translate.refresh();
                    }]
                }
            });
    });
