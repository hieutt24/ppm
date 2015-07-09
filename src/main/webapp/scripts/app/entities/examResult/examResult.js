'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('examResult', {
                parent: 'entity',
                url: '/examResult',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.examResult.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/examResult/examResults.html',
                        controller: 'ExamResultController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('examResult');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('examResultDetail', {
                parent: 'entity',
                url: '/examResult/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.examResult.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/examResult/examResult-detail.html',
                        controller: 'ExamResultDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('examResult');
                        return $translate.refresh();
                    }]
                }
            });
    });
