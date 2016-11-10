'use strict';

angular.module('myApp.map', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/map', {
        templateUrl: 'views/map/map.html',
        controller: 'MapCtrl'
    });
}])


.controller('MapCtrl', ['$scope', '$rootScope', '$location', 'uiGmapGoogleMapApi', function($scope, $rootScope, $location, uiGmapGoogleMapApi) {
    $scope.user = $rootScope.currentUser;
    var position = {
        coords: {
            latitude: 47.829411,
            longitude: 13.005432
        }
    }
    $scope.map = {
        center: {
            latitude: position.coords.latitude,
            longitude: position.coords.longitude
        },
        zoom: 11
    };
    $scope.clickhandler = function(marker, eventName, markerModel) {
      $rootScope.selectedArea = {id:marker.key, name:marker.title};
      $location.path("start");
    };

    $scope.markers = [
      {
        id: 1,
        coords: {
            latitude: 47.829411,
            longitude: 13.005432
        },
        options: {
            title: 'Airport'
        }
    },
    {
      id: 2,
      coords: {
          latitude: 47.813777,
          longitude: 13.044259
      },
      options: {
          title: 'Central Station'
      }
    },
    {
      id: 3,
      coords: {
          latitude: 47.769629,
          longitude: 13.071660
      },
      options: {
          title: 'Park_Ride south'
      }
    }
];
}]);
