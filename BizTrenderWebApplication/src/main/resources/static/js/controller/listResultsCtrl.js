'use strict';

biztrenderApp
		.controller(
				'listResultsCtrl',
				[
						'$scope',
						'$state',
						'$stateParams',
						'$uibModal',
						'$window',
						'$timeout',
						'dataService',
						'$location',
						function($scope, $state, $stateParams, $uibModal,
								$window, $timeout, dataService, $location) {
							$scope.businessData = [];
							$scope.listSearchTerm = $stateParams.keyword;
							$scope.selectedBusiness = null;
							
							$scope.itemsPerPage = 35;
							$scope.currentPage = 0;
							
							$scope.getBusinessData = function() {
								var term = {
									query : $scope.listSearchTerm ? $scope.listSearchTerm
											: $stateParams.keyword,
									offset : $scope.currentPage*$scope.itemsPerPage,
									limit : $scope.itemsPerPage
								};
								dataService
										.searchBusinessResults(term)
										.then(
											function(response) {
												$scope.businessData = response.data;
												$scope.selectedBusiness = $scope.businessData[0];
												$scope.total = response.pagination.total;
											},
											function(error) {
												console.log("error::", error);
											});
							}
							
							$timeout($scope.getBusinessData(), 50);
							
							$scope.setSelected = function(selectedBusiness) {
							       $scope.selectedBusiness = selectedBusiness;
							       console.log(selectedBusiness.business_id);
							}
							
							  $scope.range = function() {
							    var rangeSize = 10;
							    var ret = [];
							    var start;
							    var end;

							    start = $scope.currentPage;
							    if ( start > $scope.pageCount()-rangeSize ) {
							      start = $scope.pageCount()-rangeSize;
							    }

							    for (var i=start; i<start+rangeSize; i++) {
							      ret.push(i);
							    }
							    
							    ret = ret.filter(function(x){ return x > -1 });
							    return ret;
							  };


							  $scope.prevPage = function() {
							    if ($scope.currentPage > 0) {
							      $scope.currentPage--;
							    }
							    $scope.getBusinessData();
							  };
							  
							  $scope.firstPage = function() {
								  if ($scope.currentPage > 0) {
								      $scope.currentPage = 0;
								    }
								  $scope.getBusinessData();
								  };

							  $scope.prevPageDisabled = function() {
							    return $scope.currentPage === 0 ? "disabled" : "";
							  };

							  $scope.nextPage = function() {
							    if ($scope.currentPage < $scope.pageCount() - 1) {
							      $scope.currentPage++;
							    }
							    $scope.getBusinessData();
							  };
							  
							  $scope.lastPage = function() {
								    if ($scope.currentPage < $scope.pageCount() - 1) {
								      $scope.currentPage = $scope.pageCount() - 1;
								    }
								    $scope.getBusinessData();
								  };

							  $scope.nextPageDisabled = function() {
							    return $scope.currentPage === $scope.pageCount() - 1 ? "disabled" : "";
							  };

							  $scope.pageCount = function() {
							    return Math.ceil($scope.total/$scope.itemsPerPage);
							  };

							  $scope.setPage = function(n) {
							    if (n >= 0 && n < $scope.pageCount()) {
							      $scope.currentPage = n;
							    }
							    $scope.getBusinessData();
							  };
							  
							  $scope.popUpDetailView = function(selectedObj) {
										var url = $state.href('businessDetails', {
											business : JSON.stringify(selectedObj)
										}, {
											absolute : true
										});
										var randomnumber = Math.floor((Math
												.random() * 100) + 1);
										$window.open(url, "_blank", 'popwin'
												+ randomnumber,
												'width=840, height=480');
							};
						} ]);