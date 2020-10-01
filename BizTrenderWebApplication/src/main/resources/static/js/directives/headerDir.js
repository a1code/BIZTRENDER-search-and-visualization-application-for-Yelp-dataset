'use strict';
biztrenderApp.directive('header', function () {
    return {
        restrict: 'E',
        replace: true,
        scope: {},
        templateUrl: "/view/header.html",
        controller: ['$scope', '$filter', function ($scope, $filter) {
            
        }]
    }
});