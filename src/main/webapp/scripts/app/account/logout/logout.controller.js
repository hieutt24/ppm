'use strict';

angular.module('ppmApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
