'use strict';

angular.module('myApp.stop', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/stop', {
        templateUrl: 'views/stop/stop.html',
        controller: 'StopCtrl'
    });
}])

.controller('StopCtrl', ['$scope', '$location', '$rootScope', '$http', function($scope, $location, $rootScope, $http) {
    $scope.user = $rootScope.currentUser;
    $scope.licensePlate = $rootScope.selectedLicensePlate;

    $scope.stopPressed = function() {
        $http.post($rootScope.basisUrl+'/stop/'+$rootScope.selectedArea.id+'/'+$rootScope.selectedLicensePlate)
        .then(function(response){
            $location.path("map");
        });
    }
}]);
