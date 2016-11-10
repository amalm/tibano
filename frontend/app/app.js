'use strict';

// Declare app level module which depends on views, and components
angular.module('myApp', [
    'ngRoute',
    'uiGmapgoogle-maps',
    'myApp.map',
    'myApp.start',
    'myApp.stop',
    'myApp.enforcement',
    'myApp.display'
]).config(['$locationProvider', '$routeProvider', '$httpProvider',
function($locationProvider, $routeProvider, $httpProvider) {
    $locationProvider.hashPrefix('!');

    $routeProvider.otherwise({
        redirectTo: '/map'
    });

    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])
.config(function(uiGmapGoogleMapApiProvider) {
    uiGmapGoogleMapApiProvider.configure({
      //key: 'your api key',
      //libraries: 'weather,geometry,visualization',
      v: '3.17'
    });
  })
.run(function($rootScope, $http) {
    $rootScope.currentUser = { id:1, name:"Thomas"};
    $rootScope.selectedArea = { id:1, name:"Airport"};
    $rootScope.basisUrl = 'http://192.168.1.101:8080';
});
