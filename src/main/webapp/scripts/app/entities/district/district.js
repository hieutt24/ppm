'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('district', {
                parent: 'entity',
                url: '/district',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'ppmApp.district.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/district/districts.html',
                        controller: 'DistrictController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('district');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('districtDetail', {
                parent: 'entity',
                url: '/district/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'ppmApp.district.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/district/district-detail.html',
                        controller: 'DistrictDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('district');
                        return $translate.refresh();
                    }]
                }
            });
    });
