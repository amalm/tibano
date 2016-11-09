'use strict';

angular.module('myApp.stop', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/stop', {
        templateUrl: 'views/stop/stop.html',
        controller: 'StopCtrl'
    });
}])

.controller('StopCtrl', ['$scope', '$rootScope', function($scope, $rootScope) {
    $scope.user = 'StopUser';
    $scope.licensePlate = $rootScope.selectedLicensePlate;
}]);
