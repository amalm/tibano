'use strict';

angular.module('myApp.map', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/map', {
        templateUrl: 'views/map/map.html',
        controller: 'MapCtrl'
    });
}])


.controller('MapCtrl', ['$scope', '$rootScope', '$location', 'uiGmapGoogleMapApi', '$http',
function($scope, $rootScope, $location, uiGmapGoogleMapApi, $http) {
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
    $scope.user = $rootScope.currentUser;
    $scope.clickhandler = function(marker, eventName, markerModel) {
      $rootScope.selectedArea = {id:markerModel.id, name:markerModel.title};
      $location.path("start");
    };

    $scope.markers = [];

    $scope.refreshData = function() {
        $http.get($rootScope.basisUrl+'/getAreas')
        .then(function(response) {
            var as = response.data;
            for (var i=0; i<as.length; i++) {
                var a = as[i];
                var free = a.capacity-a.occupied;
                $scope.markers[i] = {
                    id: a.id,
                    latitude: a.latitude,
                    longitude: a.longitude,
                    options: {
                        title: a.name + " – "+(a.capacity-a.occupied) + " Free",
                        labelContent: ""+(free) + " Free",
                        labelClass: free==0?"labelFull":"labelFree"
                    }
                };
            }
        });
    };

    $scope.refreshData();

}]);
