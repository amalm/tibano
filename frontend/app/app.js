'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
    'ngRoute',
    'myApp.map',
    'myApp.start',
    'myApp.stop',
    'myApp.enforcement',
    'myApp.display'
]).
config(['$locationProvider', '$routeProvider', '$httpProvider',
function($locationProvider, $routeProvider, $httpProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.otherwise({
        redirectTo: '/map'
    });

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])
.run(function($rootScope, $http) {
    $rootScope.currentUser = { id:1, name:"Hubert Farnsworth"};
    $rootScope.selectedArea = { id:1, name:"Airport"};
    $rootScope.basisUrl = 'http://192.168.0.101:8080';
});
