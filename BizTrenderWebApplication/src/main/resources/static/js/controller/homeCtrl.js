'use strict';

biztrenderApp.controller('homeCtrl', [ 
	'$scope',
	'$state',
	function($scope, $state) {
		$scope.seachResults = function(){
			$state.go('listResults', {
				keyword : $scope.homeSearchTerm
			});
		}
	}	
]);