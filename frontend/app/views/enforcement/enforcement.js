'use strict';

angular.module('myApp.enforcement', ['ngRoute'])

.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider.when('/enforcement', {
        templateUrl: 'views/enforcement/enforcement.html',
        controller: 'EnforcementCtrl'
    });
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])

.controller('EnforcementCtrl', ['$scope', function($scope, $http) {
    $scope.areaInfo = [{
        areaName: 'Central',
        capacity: 52,
        occupied: 37,
        paying: 34,
        attentionClass: 'success'
    }, {
        areaName: 'Stadium',
        capacity: 108,
        occupied: 66,
        paying: 45,
        attentionClass: 'danger'
    }, {
        areaName: 'P&R south',
        capacity: 78,
        occupied: 41,
        paying: 22,
        attentionClass: 'danger'
    }];
}]);
