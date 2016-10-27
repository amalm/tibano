angular.
module('myApp').
component('getVersion', {
    template: 'Version: {{$ctrl.version}}!',
    controller: function GetVersionController($http) {
        //this.version = 'unknown';
        $http.get('http://localhost:8080/getVersion', {
            params: {
                headers: {
                    'Access-Control-Allow-Origin': '*',
                    //'Access-Control-Request-Headers': 'access-control-allow-origin'
                }
            }
        }).then(function(response) {
            this.version = response.data;
        }, function(response) {
          this.version = 'unknown';
        });
    }
});
