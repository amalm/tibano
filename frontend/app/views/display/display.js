'use strict';

angular.module('myApp.display', ['ngRoute'])

.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider.when('/display', {
        templateUrl: 'views/display/display.html',
        controller: 'DisplayCtrl'
    });
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])

.controller('DisplayCtrl', ['$scope', '$http', '$rootScope', '$interval', '$timeout',
function($scope, $http, $rootScope, $interval, $timeout) {
    $scope.refreshData = function() {
        $http.get($rootScope.basisUrl+'/getAreas')
        .then(function(response) {
            $scope.areaInfo = response.data;
            for (var i=0; i<$scope.areaInfo.length; i++) {
                var a = $scope.areaInfo[i];
                a.free = a.capacity - a.occupied;
                if (0 == a.free)
                    a.attentionClass = 'danger';
                else
                    a.attentionClass = 'success';
            }
        });
    }

    $scope.refreshData();

    $scope.startTimer = function () {
        $scope.timer = $timeout(function () {
        $scope.refreshData();
        $scope.startTimer();
        }, 2000);
    };

    $scope.startTimer();

}]);
