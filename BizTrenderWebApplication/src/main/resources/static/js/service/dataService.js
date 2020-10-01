'use strict';

biztrenderApp.service('dataService', [
		'$http',
		'$q',
		'$timeout',
		'SERVICE_URL',
		function($http, $q, $timeout, SERVICE_URL) {
			this.searchBusinessResults = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/business/search', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getBusinessesByCategoryAndRating = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/business/findByCategoryAndRating', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getBusinessesWithAllRatingsByCategory = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/business/findWithAllRatingsByCategory', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getBusinessesWithCheckinsByCategory = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/business/findWithCheckinsByCategory', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getReviewSentimentByBusiness = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/review/getReviewSentimentByBusiness', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getUsersAndRatingsByBusiness = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/review/getUsersAndRatingsByBusiness', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
			
			this.getTipsAndReviewsTextByBusiness = function(queryParams) {
				var defer = $q.defer(), query = {
					params : queryParams
				};
				$http.get(SERVICE_URL + '/service/review/getTipsAndReviewsTextByBusiness', query)
						.success(function(data) {
							defer.resolve(data);
						}).error(function(err) {
							defer.reject(err);
						});
				return defer.promise;
			};
		} ]);