'use strict';

angular.module('ppmApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('province', {
                parent: 'entity',
                url: '/province',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.province.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/province/provinces.html',
                        controller: 'ProvinceController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('province');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('provinceDetail', {
                parent: 'entity',
                url: '/province/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ppmApp.province.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/province/province-detail.html',
                        controller: 'ProvinceDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('province');
                        return $translate.refresh();
                    }]
                }
            });
    });
