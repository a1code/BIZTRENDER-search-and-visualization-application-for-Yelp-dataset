'use strict';
biztrenderApp.directive('starRating', function () {
	return {
	        restrict: 'A',
	        template: '<ul style="color: #a9a9a9;margin: 0;padding: 0;display: inline-block">' +
	            '<li style="list-style-type: none;display: inline-block;padding: 1px;text-align: center;font-weight: bold;cursor: pointer;" ng-repeat="star in stars" ng-class="star">' +
	            '\u2605' +
	            '</li>' +
	            '</ul>',
	        scope: {
	            ratingValue: '=',
	            max: '='
	        },
	        link: function (scope, elem, attrs) {
	            scope.stars = [];
	            var i=0;
	            while(i < Math.floor(scope.ratingValue)) {
	                scope.stars.push({
	                    filled: i < Math.floor(scope.ratingValue)
	                });
	                i++;
	            }
	            if((i == Math.floor(scope.ratingValue)) && 
	                	(scope.ratingValue - Math.floor(scope.ratingValue) > 0)){
	            	scope.stars.push({
	                    partial_filled: true
	                });
	                i++;
	            }
	        }
	    }
});