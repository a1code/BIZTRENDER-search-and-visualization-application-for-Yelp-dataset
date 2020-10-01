var biztrenderApp = angular.module("biztrenderApp", [ 'ngAnimate', 'ui.bootstrap',
		'ui.router', 'LocalStorageModule', 'ngJsTree', 'angular-pivottable',
		'nvd3', 'gridster', 'highcharts-ng', 'angularjs-dropdown-multiselect']);

biztrenderApp
		.config([
				'$stateProvider',
				'$urlRouterProvider',
				'$locationProvider',
				'$httpProvider',
				function($stateProvider, $urlRouterProvider, $locationProvider,
						$httpProvider) {

					$urlRouterProvider.when('/', '');
					$urlRouterProvider.when('', '/home');

					$urlRouterProvider.otherwise("404");

					$stateProvider
							.state('home', {
								url : '/home',
								templateUrl : 'view/home.html',
								controller : 'homeCtrl',
								friendlyName : 'Home'
							})
							.state('listResults', {
								url : '/listResults?keyword',
								templateUrl : 'view/listResults.html',
								controller : 'listResultsCtrl',
								friendlyName : 'List Results'
							})
							.state('trendSummary', {
								url : '/trendSummary',
								templateUrl : 'view/trendSummary.html',
								controller : 'trendSummaryCtrl',
								friendlyName : 'Trend Summary'
							})
							.state(
									'businessDetails', {
										url : '/businessDetails?business?startDate?endDate?additionalConfigs',
										templateUrl : 'view/businessDetails.html',
										controller : 'businessDetailsCtrl',
										friendlyName : 'Business Details'
									})
							.state('marketData', {
								url : '/marketData?keyword?startDate?endDate',
								templateUrl : 'view/marketData.html',
								controller : 'marketDataCtrl',
								friendlyName : 'Market Data'
							})
							.state('404', {
								url : '/404',
								templateUrl : 'view/pageNotFound.html'
							})

					// $locationProvider.html5Mode(true).hashPrefix('!');;
				} ]);

biztrenderApp.config([ '$provide', function($provide) {
	$provide.decorator('selectDirective', [ '$delegate', function($delegate) {
		var directive = $delegate[0];

		directive.compile = function() {

			function post(scope, element, attrs, ctrls) {
				directive.link.post.apply(this, arguments);

				var ngModelController = ctrls[1];
				if (ngModelController && attrs.multiSelect !== null) {
					originalRender = ngModelController.$render;
					ngModelController.$render = function() {
						originalRender();
						element.multiselect('refresh');
					};
				}
			}

			return {
				pre : directive.link.pre,
				post : post
			};
		};

		return $delegate;
	} ]);
} ]);

biztrenderApp.run([ '$rootScope', '$state', function($rootScope, $state) {
	$rootScope.$state = $state;

	$rootScope.isActive = function(stateName) {
		return $state.includes(stateName);
	}

} ]);

biztrenderApp.constant('SERVICE_URL', 'http://localhost:8888'); 