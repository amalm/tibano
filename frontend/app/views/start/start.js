'use strict';

angular.module('myApp.start', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl: 'views/start/start.html',
        controller: 'StartCtrl'
    });
}])

.controller('StartCtrl', ['$scope', '$location', '$rootScope', function($scope, $location, $rootScope) {
    $scope.user = 'StartUser';
    $scope.licensePlates = ['HH FT 4711', 'TEST'];
    $scope.selectedLicensePlate = null;

    $scope.update = function() {
        $rootScope.selectedLicensePlate = $scope.selectedLicensePlate;
        $location.path("stop");
    }
}]);
