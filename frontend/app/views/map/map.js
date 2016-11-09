'use strict';

angular.module('myApp.map', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/map', {
    templateUrl: 'views/map/map.html',
    controller: 'MapCtrl'
  });
}])

.controller('MapCtrl', ['$scope', '$rootScope', function($scope, $rootScope) {
    $scope.user = $rootScope.currentUser;
}]);
