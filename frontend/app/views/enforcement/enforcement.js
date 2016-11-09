'use strict';

angular.module('myApp.enforcement', ['ngRoute'])

.config(['$routeProvider', '$httpProvider', function($routeProvider, $httpProvider) {
    $routeProvider.when('/enforcement', {
        templateUrl: 'views/enforcement/enforcement.html',
        controller: 'EnforcementCtrl'
    });
    $httpProvider.defaults.useXDomain = true;
    delete $httpProvider.defaults.headers.common['X-Requested-With'];
}])

.controller('EnforcementCtrl', ['$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {
    $scope.init = function() {
        $http.get($rootScope.basisUrl+'/getAreas')
        .then(function(response) {
            $scope.areaInfo = response.data;
            for (var i=0; i<$scope.areaInfo.length; i++) {
                var a = $scope.areaInfo[i];
                a.attentionClass = 'success';
                if (0 < a.occupied) {
                    var dangerThreshold = a.occupied * 0.95;
                    var percentPaying = 100.0 * a.runningPayments / a.occupied;
                    if (percentPaying < dangerThreshold) {
                        a.attentionClass = 'danger';
                    }
                }
            }
        });
    }

    $scope.init();

}]);
