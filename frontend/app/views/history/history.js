'use strict';

angular.module('myApp.history', ['ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.when('/history', {
        templateUrl: 'views/history/history.html',
        controller: 'HistoryCtrl'
    });
}])

.controller('HistoryCtrl', ['$scope', '$location', '$rootScope', '$http', function($scope, $location, $rootScope, $http) {
    $scope.user = $rootScope.currentUser;
    $scope.getPaymentHistory = function() {
        $http.get($rootScope.basisUrl + '/getPaymentHistory?userId=1')
            .then(function(response) {
                $scope.payments = response.data;
            });
    }
    $scope.getPaymentHistory();
}]);
