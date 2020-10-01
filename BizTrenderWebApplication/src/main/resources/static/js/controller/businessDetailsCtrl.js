'use strict';

biztrenderApp
		.controller(
				'businessDetailsCtrl',
				[
						'$scope',
						'dataService',
						'$state',
						'$stateParams',
						'$window',
						'$timeout',
						'$uibModal',
						function($scope, dataService, $state, $stateParams, $window, $timeout, $uibModal) {
							$scope.business =  JSON.parse($stateParams.business);
							
							var H = Highcharts,
						    chart1,
						    chart2,
						    chart3;
							
							$scope.tips_and_reviews = function(t) {
								chart1 = H.chart('figure_word_cloud', {
									chart: {
							            events: {
							                load: function () {
							                	chart1 = this;
							        			
							        			var term = {
							        				business_id : $scope.business.business_id
							        			};
							        			
							        			dataService
							        			.getTipsAndReviewsTextByBusiness(term)
							        			.then(
							        				function(response) {
							        					var text = response.data;
							        					
														var lines = text.split(/[,\. ]+/g),
														    data = H.reduce(lines, function (arr, word) {
														        var obj = Highcharts.find(arr, function (obj) {
														            return obj.name === word;
														        });
														        if (obj) {
														            obj.weight += 1;
														        } else {
														            obj = {
														                name: word,
														                weight: 1
														            };
														            arr.push(obj);
														        }
														        return arr;
														    }, []);
														
														data = data.filter(function( obj ) {
															if (data.length > 1000) {
																return obj.weight >= 20;
															} else {
																return true;
															}
														});
														chart1.series[0].setData(data);
							        				},
							        				function(error) {
							        					console.log("error::", error);
							        				});
							                }
							            }
							        },
							        
								    accessibility: {
								        screenReaderSection: {
								            beforeChartFormat: '<h5>{chartTitle}</h5>' +
								                '<div>{chartSubtitle}</div>' +
								                '<div>{chartLongdesc}</div>' +
								                '<div>{viewTableButton}</div>'
								        }
								    },
								    series: [{
								        type: 'wordcloud',
								        turboThreshold: 2000,
								        data: [],
								        name: 'Occurrences'
								    }],
								    title: {
								        text: 'Wordcloud of Tips and Reviews'
								    }
								});
								
								var x = Array.from(document.getElementById("figures_parent").children);
								var elem = x[2];
								x = x.filter(function(item) {
								    return item !== elem
								});
								
								if(t) {
									elem.style.display = "";
									  x = x.map(p => p.style.display = "none");
								} else {
									if (elem.style.display === "none") {
										  elem.style.display = "";
										  x = x.map(p => p.style.display = "none");
									  } else {
										  elem.style.display = "none";
									  }
								}
							};
							
							$scope.review_sentiment = function(t) {
								var toggleAxisExtremes = function(event) {
									var series = this,
										yAxis = series.yAxis;

							    if (event.type === "show") {
							    		(yAxis.oldExtremes) ? yAxis.setExtremes(yAxis.oldExtremes.min, yAxis.oldExtremes.max, true, false) : false;
							    } else if (event.type === "hide" && !yAxis.hasVisibleSeries){
							    		yAxis.oldExtremes = {
							        		min: yAxis.min,
							            max: yAxis.max
							        }
							    		yAxis.setExtremes("null")
							    }
							}
								
								chart2 = H.chart('figure_histogram', {
									chart: {
							            events: {
							                load: function () {
							                	chart2 = this;
							                	
							        			var term = {
						        					business_id : $scope.business.business_id
							        			};
							        			
							        			dataService
							        			.getReviewSentimentByBusiness(term)
							        			.then(
							        				function(response) {
							        					var data = [];
							        					$scope.reviewSentiments = response.data
							        					$scope.reviewSentiments.forEach(function (p) {
							        						var point_attrs = {};
							        						point_attrs['y'] = p;
							        						
							        						if(p > 0) {
							        							point_attrs['color'] = 'green';
							        						} else if(p == 0){
							        							point_attrs['color'] = 'black';
							        						} else {
							        							point_attrs['color'] = 'red';
							        						}
							        						data.push(point_attrs);
							        				    });
							        					chart2.series[1].setData(data);
							        				},
							        				function(error) {
							        					console.log("error::", error);
							        				});
							                }
							            }
							        },
							        
									title: {
								        text: 'Frequency distribution of Reviews by sentiment'
								    },

								    xAxis: [{
								        title: { text: '' },
								        labels: {
								        	enabled: false
								        },
								        alignTicks: true
								    }, {
								        title: { text: 'Sentiment Score (Histogram)',
								        	style: {
								                fontSize: '20px'
								            }},
								        alignTicks: true,
								        opposite: true,
								        labels: {
								            style: {
								                fontSize: '20px'
								            }
								        }
								    }],

								    yAxis: [{
								        title: { text: 'Sentiment Score (Data)',
								        	style: {
								                fontSize: '20px'
								            }},
								            labels: {
									            style: {
									                fontSize: '20px'
									            }
									        },
									        plotLines: [{
									            value: 0,
									            width: 2,
									            color: '#aaa',
									            zIndex: 10
									          }]
								    }, {
								        title: { text: '' },
								        labels: {
								            style: {
								                fontSize: '20px'
								            }
								        },
								        opposite: true
								    }],
								    
								    tooltip: {
							            pointFormat: '{series.name} : {point.y}'
							        },
							        
								    plotOptions: {
								        histogram: {
								            accessibility: {
								                pointDescriptionFormatter: function (point) {
								                    var ix = point.index + 1,
								                        x1 = point.x.toFixed(3),
								                        x2 = point.x2.toFixed(3),
								                        val = point.y;
								                    return ix + '. ' + x1 + ' to ' + x2 + ', ' + val + '.';
								                }
								            }
								        },
								        
								        series: {
							        		events: {
							            		show: toggleAxisExtremes,
							                hide: toggleAxisExtremes
							            }
							        }
								    },

								    series: [{
								        name: 'Frequency',
								        type: 'histogram',
								        xAxis: 1,
								        yAxis: 1,
								        baseSeries: 's1',
								        zIndex: -1
								    }, {
								        name: 'Score',
								        type: 'scatter',
								        data: [],
								        id: 's1',
								        marker: {
								            radius: 5
								        }
								    }]
								});
								
								
								var x = Array.from(document.getElementById("figures_parent").children);
								var elem = x[0];
								x = x.filter(function(item) {
								    return item !== elem
								});
								
								if(t) {
									elem.style.display = "";
									  x = x.map(p => p.style.display = "none");
								} else {
									if (elem.style.display === "none") {
										  elem.style.display = "";
										  x = x.map(p => p.style.display = "none");
									  } else {
										  elem.style.display = "none";
									  }
								}
							};
							
							$scope.ratings_and_users = function(t) {
								chart3 = H.chart('figure_component_bars', {
									 chart: {
									        type: 'column',
									        events: {
								                load: function () {
								                	chart3 = this;
								                	
								        			var term = {
							        					business_id : $scope.business.business_id
								        			};
								        			
								        			dataService
								        			.getUsersAndRatingsByBusiness(term)
								        			.then(
								        				function(response) {
								        					$scope.users_and_ratings = response.data
								        					
								        					chart3.xAxis[0].setCategories($scope.users_and_ratings.user_names);
								        					chart3.series[0].setData($scope.users_and_ratings.ratings_average);
								        					chart3.series[1].setData($scope.users_and_ratings.ratings_business);
								        				},
								        				function(error) {
								        					console.log("error::", error);
								        				});
								                }
								            }
									    },
									    title: {
									        text: 'Average User Ratings vs Actual Ratings'
									    },
									    xAxis: {
									        categories: [],
									        labels: {
									            style: {
									                fontSize: '20px'
									            }
									        }
									    },
									    yAxis: [{
									        min: 0,
									        title: {
									        	margin: 40,
									            text: 'Ratings',
									            style: {
									                fontSize: '20px'
									            }
									        },
									        labels: {
									        	margin: 20,
									            style: {
									                fontSize: '20px'
									            }
									        }
									    }],
									    legend: {
									        shadow: false
									    },
									    tooltip: {
									        shared: true
									    },
									    plotOptions: {
									        column: {
									            grouping: false,
									            shadow: false,
									            borderWidth: 0
									        }
									    },
									    series: [{
									        name: 'Average rating by user',
									        color: 'rgba(250,150,217,1)',
									        data: [],
									        pointPadding: 0.3,
									        pointPlacement: -0.2
									    }, {
									        name: 'Rating for this business',
									        color: 'rgba(126,86,134,.9)',
									        data: [],
									        pointPadding: 0.4,
									        pointPlacement: -0.2
									    }]
								});
								
								
								var x = Array.from(document.getElementById("figures_parent").children);
								var elem = x[1];
								x = x.filter(function(item) {
								    return item !== elem
								});
								
								if(t) {
									elem.style.display = "";
									  x = x.map(p => p.style.display = "none");
								} else {
									if (elem.style.display === "none") {
										  elem.style.display = "";
										  x = x.map(p => p.style.display = "none");
									  } else {
										  elem.style.display = "none";
									  }
								}
							};
							
							$scope.review_sentiment(true);
						} ]);