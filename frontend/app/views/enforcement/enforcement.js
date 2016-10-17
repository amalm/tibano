'use strict';

angular.module('myApp.enforcement', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
  $routeProvider.when('/enforcement', {
    templateUrl: 'views/enforcement/enforcement.html',
    controller: 'View1Ctrl'
  });
}])

.controller('View1Ctrl', [function() {

}]);
