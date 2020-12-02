

angular.module('demo', [])
.controller('Hello', function($scope, $http) {
    //$http.get('http://localhost:8084/mvnSWP/json/hello-world').
    //$http.get('http://localhost:8084/FluidGPS/json/hello-world').
    $http.get('/json/hello-world').
        then(function(response) {
            $scope.greeting = response.data;
        });
});
