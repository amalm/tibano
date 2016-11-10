'use strict';

angular.module('myApp.start', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/start', {
        templateUrl: 'views/start/start.html',
        controller: 'StartCtrl'
    });
}])

.controller('StartCtrl', ['$scope', '$location', '$rootScope', '$http',
function($scope, $location, $rootScope, $http) {
    $scope.user = $rootScope.currentUser;
    $scope.licensePlates = [];
    $scope.selectedLicensePlate = null;

    $scope.update = function() {
        $rootScope.selectedLicensePlate = $scope.selectedLicensePlate;
        $http.post($rootScope.basisUrl+'/start/'+$rootScope.selectedArea.id+'/'+$rootScope.selectedLicensePlate)
        .then(function(response){
            $location.path("stop");
        });
    }

    $scope.back = function() {
        $location.path("map");
    }

    $scope.init = function() {
        $http.get($rootScope.basisUrl+'/user?userId='+ $rootScope.currentUser.id)
        .then(function(response) {
            $scope.licensePlates = response.data.licensePlates;
        });
    }

    $scope.init();
}]);
