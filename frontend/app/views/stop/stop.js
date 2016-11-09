'use strict';

angular.module('myApp.stop', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/stop', {
        templateUrl: 'views/stop/stop.html',
        controller: 'StopCtrl'
    });
}])

.controller('StopCtrl', ['$scope', '$location', '$rootScope', function($scope, $location, $rootScope) {
    $scope.user = $rootScope.currentUser;
    $scope.licensePlate = $rootScope.selectedLicensePlate;

    $scope.stopPressed = function() {
        $location.path("map");
    }
}]);
