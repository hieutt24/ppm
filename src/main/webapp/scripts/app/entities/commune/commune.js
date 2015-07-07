'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('commune', {
                parent: 'entity',
                url: '/commune',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.commune.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commune/communes.html',
                        controller: 'CommuneController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commune');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('communeDetail', {
                parent: 'entity',
                url: '/commune/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.commune.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/commune/commune-detail.html',
                        controller: 'CommuneDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('commune');
                        return $translate.refresh();
                    }]
                }
            });
    });
