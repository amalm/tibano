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
        $location.path("stop");
    }

    $scope.init = function() {
        $http.get($rootScope.basisUrl+'/user?userId='+ $rootScope.currentUser.id)
        .then(function(response) {
            $scope.licensePlates = response.data.licensePlates;
        });
    }

    $scope.init();
}]);
