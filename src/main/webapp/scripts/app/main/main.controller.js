'use strict';

angular.module('ppmApp')
    .controller('MainController', function ($scope, Principal) {
        Principal.identity().then(function(account) {
            $scope.account = account;
            $scope.isAuthenticated = Principal.isAuthenticated;
            if (!Principal.isAuthenticated()) {
            	window.location.href = 'http://210.211.124.200/ppm/#/login';
            }
        });
    });
