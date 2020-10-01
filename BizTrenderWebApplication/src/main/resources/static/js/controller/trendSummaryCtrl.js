'use strict';

biztrenderApp.controller('trendSummaryCtrl', [ 
	'$scope',
	'$state',
    '$timeout',
    'dataService',
    '$window',
    '$uibModal',
    'SERVICE_URL',
	function($scope, $state, $timeout, dataService, $window, $uibModal, SERVICE_URL) {
		 $scope.dropdown_settings = {
		      scrollableHeight: '200px',
		      scrollable: true,
		      enableSearch: true
		  };
		  
		  $scope.dropdown_options_categories = [{
		      "label": "Active Life",
		      "id": "Active Life"
		  }, {
		      "label": "Arts & Entertainment",
		      "id": "Arts & Entertainment"
		  }, {
		      "label": "Automotive",
		      "id": "Automotive"
		  }, {
		      "label": "Beauty & Spas",
		      "id": "Beauty & Spas"
		  }, {
		      "label": "Education",
		      "id": "Education"
		  }, {
		      "label": "Event Planning & Services",
		      "id": "Event Planning & Services"
		  }, {
		      "label": "Financial Services",
		      "id": "Financial Services"
		  }, {
		      "label": "Food",
		      "id": "Food"
		  }, {
		      "label": "Health & Medical",
		      "id": "Health & Medical"
		  }, {
		      "label": "Home Services",
		      "id": "Home Services"
		  }, {
		      "label": "Hotels & Travel",
		      "id": "Hotels & Travel"
		  }, {
		      "label": "Local Flavor",
		      "id": "Local Flavor"
		  }, {
		      "label": "Local Services",
		      "id": "Local Services"
		  }, {
		      "label": "Mass Media",
		      "id": "Mass Media"
		  }, {
		      "label": "Nightlife",
		      "id": "Nightlife"
		  }, {
		      "label": "Pets",
		      "id": "Pets"
		  }, {
		      "label": "Professional Services",
		      "id": "Professional Services"
		  }, {
		      "label": "Public Services & Government",
		      "id": "Public Services & Government"
		  }, {
		      "label": "Religious Organizations",
		      "id": "Religious Organizations"
		  }, {
		      "label": "Restaurants",
		      "id": "Restaurants"
		  }, {
		      "label": "Shopping",
		      "id": "Shopping"
		  }];
		  
		  $scope.dropdown_options_ratings = [{
		      "label": "1",
		      "id": "0.0"
		  }, {
		      "label": ">1 upto 2",
		      "id": "1.0"
		  }, {
		      "label": ">2 upto 3",
		      "id": "2.0"
		  }, {
		      "label": ">3 upto 4",
		      "id": "3.0"
		  }, {
		      "label": ">4",
		      "id": "4.0"
		  }];
			  
		$scope.businessData = [];
		$scope.businessDataAllRatings = [];
		$scope.businessDataCheckins = [];
		
		var category_selected_default = (null !== $window.localStorage.getItem('categories_selection')) 
			? JSON.parse($window.localStorage.getItem('categories_selection'))
			: $scope.dropdown_options_categories.slice(0, 1);
		var rating_selected_default = (null != $window.localStorage.getItem('ratings_selection')) 
			? JSON.parse($window.localStorage.getItem('ratings_selection'))
			: $scope.dropdown_options_ratings.slice(0, 1);
		$window.localStorage.clear();
		
		$scope.categories_model = category_selected_default;
		$scope.ratings_model = rating_selected_default;
		
	
	$scope.reloadTrends = function(){
		$window.localStorage.setItem('categories_selection', JSON.stringify($scope.categories_model));
		$window.localStorage.setItem('ratings_selection', JSON.stringify($scope.ratings_model));
        
        $window.location.reload();
    };
	
	var H = Highcharts,
    map = H.maps['custom/usa-and-canada'],
    chart1,
    chart2,
    chart3,
    chart4,
    chart5,
    chart6;
	
	function formatParams( params ){
	  return "?" + Object
	        .keys(params)
	        .map(function(key){
	          return key+"="+encodeURIComponent(params[key])
	        })
	        .join("&")
	};
	
	$scope.business_by_rating = function(t) {
		chart2 = H.chart('figure_stacked_bar_chart', {
		    chart: {
		        type: 'column',
		        options3d: {
		            enabled: true,
		            alpha: 15,
		            beta: 15,
		            viewDistance: 25,
		            depth: 40
		        },
		        events: {
		            load: function () {
		            	chart2 = this;
		            	var categories_for_query = $scope.categories_model.map(function(item) {
		    			    return item.id
		    			});
		    			   			
		    			var term = {
		    				category : categories_for_query
		    			};
		    			
		            	// After chart loads you can run fetch function, then update chart data
		            	dataService
		    			.getBusinessesWithAllRatingsByCategory(term)
		    			.then(
		    				function(response) {
		    					$scope.businessDataAllRatings = response.data
		    					
		    					var categories = [];
		    					$scope.businessDataAllRatings.forEach(function (p) {
		    				        if(!categories.includes(p.categories)) {
		    				        	categories.push(p.categories);
		    				        }
		    				    });
		    					
		    					chart2.xAxis[0].setCategories(categories);
		    					
		    					var data_comps = Object.create(null);
		    					$scope.businessDataAllRatings.forEach(function(business) {
		    				    	var data_comp = data_comps[business.categories];
		    				    	if (!data_comp) {
		    				    		data_comp = data_comps[business.categories] = [];
		    				    	}
		    				    	data_comp.push(business);
		    				    });
		    					
		    					var lists = [];
		    					for (var key in data_comps) {
		    						var data_comps2 = Object.create(null);
		    						data_comps[key].forEach(function(business) {
		    					    	var data_comp = data_comps2[business.stars];
		    					    	if (!data_comp) {
		    					    		data_comp = data_comps2[business.stars] = [];
		    					    	}
		    					    	data_comp.push(business);
		    					    });
		    						lists.push(data_comps2);
		    					}
		    					
		    					for (var i=0; i<5; i++) {
		    						var data_series_comp = [];
		    						for (var j=0; j<lists.length; j++) {
		    							if(lists[j][i] == null) {
		    								data_series_comp.push(0);
		    							} else {
		    								data_series_comp.push(lists[j][i].length);
		    							}
		    						}
		    						chart2.series[i].setData(data_series_comp);
		    					}
		    					
		    				},
		    				function(error) {
		    					console.log("error::", error);
		    				});
		            }
		          }
		    },
		    title: {
		        text: 'Rating distribution by category'
		    },
		    xAxis: {
		    	gridLineWidth: 2,
		        categories: [],
		        labels: {
		        	overflow: 'allow',
		            skew3d: true,
		            style: {
		                fontSize: '30px'
		            }
		        }
		    },
		    yAxis: {
		    	gridLineWidth: 2,
		    	allowDecimals: false,
		        min: 0,
		        title: {
		        	margin: 60,
		            text: 'Total number of businesses',
		            skew3d: true,
		            style: {
		                fontSize: '30px'
		            }
		        },
		        labels: {
		        	margin: 30,
		            skew3d: true,
		            style: {
		                fontSize: '30px'
		            }
		        }
		    },
		    legend: {
		        reversed: true
		    },
		    plotOptions: {
		        column: {
		            stacking: 'normal',
		            depth: 5
		        }
		    },
		    series: [{
		        name: $scope.dropdown_options_ratings[0]["label"],
		        data: []
		    }, {
		        name: $scope.dropdown_options_ratings[1]["label"],
		        data: []
		    }, {
		        name: $scope.dropdown_options_ratings[2]["label"],
		        data: []
		    },{
		        name: $scope.dropdown_options_ratings[3]["label"],
		        data: []
		    },{
		        name: $scope.dropdown_options_ratings[4]["label"],
		        data: []
		    }]
		});
		
		
		var x = document.querySelectorAll('#figures_parent > div');
		x = Array.prototype.slice.call(x);
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
	
	$scope.business_by_location = function(t) {
	    chart3 = H.mapChart('figure_map', {
	    	chart: {
	            events: {
	                load: function () {
	                	chart3 = this;
	                	
	                	var categories = $scope.categories_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var ratings = $scope.ratings_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var term = {
	        				category : categories,
	        				rating : ratings
	        			};
	        			
	        			dataService
	        			.getBusinessesByCategoryAndRating(term)
	        			.then(
	        				function(response) {
	        					$scope.businessData = response.data
	        					
	        					var data = [];
	        				    var series_comp = [];
	        				    var categories = [];
	        				    $scope.businessData.forEach(function (p) {
	        				        p.lat = p.latitude;
	        				        p.lon = p.longitude;
	        				        p.z = p.stars;
	        				        p.province = p.state;
	        				        
	        				        var test = $scope.categories_model;
	        				        var result = test
	        				        	.filter(value => -1 !== p.categories.split(", ").indexOf(value.id))
	        				        	.map(value => value.id);
	        				        
	        				        p.categories = result + "";
	        				        if(!categories.includes(result + "")) {
	        				        	categories.push(result + "");
	        				        }
	        				        data.push(p);
	        				    });
	        				    
	        				    var data_comps = Object.create(null);
	        				    data.forEach(function(business) {
	        				    	var data_comp = data_comps[business.categories];
	        				    	console.log('Hello', data_comp);
	        				    	if (!data_comp) {
	        				    		data_comp = data_comps[business.categories] = [];
	        				    	}
	        				    	data_comp.push(business);
	        				    });
	        				    
	        				    data.forEach(function (p) {
	        				    	series_comp[categories.indexOf(p.categories)] = 
	        			        	{
	        				            type: 'mapbubble',
	        				            dataLabels: {
	        				                enabled: true,
	        				                format: '{point.city}'
	        				            },
	        				            name: p.categories,
	        				            data: data_comps[p.categories],
	        				            maxSize: '5%',
	        				            color: H.getOptions().colors[
	        				            	categories.indexOf(data_comps[p.categories][0].categories) + 10]
	        				        };
	        				    });
	        				    
	        				    for(var i=0; i<series_comp.length; i++){
	        				    	chart3.addSeries(series_comp[i]);
	        				    }
	        				},
	        				function(error) {
	        					console.log("error::", error);
	        				});
	                }
	            }
	        },
	        
	    	mapNavigation: {
	    		enableDoubleClickZoom: true,
		    	enableMouseWheelZoom: true,
		    	enableTouchZoom: true
	    	},
	          
	        title: {
	            text: 'Business geo-distribution by category'
	        },

	        tooltip: {
	            pointFormat: '<b>{point.name}</b><br>' +
	            	'{point.city}, {point.province}<br>' +
	                'Lat: {point.lat}<br>' +
	                'Lon: {point.lon}<br>' +
	                'Rating: {point.stars}'
	        },

	        xAxis: {
	            crosshair: {
	                zIndex: 5,
	                dashStyle: 'dot',
	                snap: false,
	                color: 'gray'
	            }
	        },

	        yAxis: {
	            crosshair: {
	                zIndex: 5,
	                dashStyle: 'dot',
	                snap: false,
	                color: 'gray'
	            }
	        },

	        series: [{
	            name: 'Basemap',
	            mapData: map,
	            borderColor: '#606060',
	            nullColor: 'rgba(200, 200, 200, 0.2)',
	            showInLegend: false
	        }, {
	            name: 'Separators',
	            type: 'mapline',
	            data: H.geojson(map, 'mapline'),
	            color: '#101010',
	            enableMouseTracking: false,
	            showInLegend: false
	        }]
	    });
	    
	// Display custom label with lat/lon next to crosshairs
	document.getElementById('figure_map').addEventListener('mousemove', function (e) {
	    var position;
	    if (chart3) {
	        if (!chart3.lab) {
	            chart3.lab = chart3.renderer.text('', 0, 0)
	                .attr({
	                    zIndex: 5
	                })
	                .css({
	                    color: '#505050'
	                })
	                .add();
	        }

	        e = chart3.pointer.normalize(e);
	        position = chart3.fromPointToLatLon({
	            x: chart3.xAxis[0].toValue(e.chartX),
	            y: chart3.yAxis[0].toValue(e.chartY)
	        });

	        chart3.lab.attr({
	            x: e.chartX + 5,
	            y: e.chartY - 22,
	            text: 'Lat: ' + position.lat.toFixed(2) + '<br>Lon: ' + position.lon.toFixed(2)
	        });
	    }
	});

	document.getElementById('figure_map').addEventListener('mouseout', function () {
	    if (chart3 && chart3.lab) {
	        chart3.lab.destroy();
	        chart3.lab = null;
	    	}
		});
	
//	var x = Array.from(document.getElementById("figures_parent").children);
	var x = document.querySelectorAll('#figures_parent > div');
	x = Array.prototype.slice.call(x);
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

	$scope.business_by_checkins = function(t) {
	    chart4 = H.chart('figure_packed_bubble_chart', {
	    	chart: {
	            type: 'packedbubble',
	            height: '100%',
	            events: {
		            load: function () {
		            	chart4 = this;
		            	
		            	var categories = $scope.categories_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var term = {
	        				category : categories
	        			};
		    			
		            	// After chart loads you can run fetch function, then update chart data
		            	dataService
		    			.getBusinessesWithCheckinsByCategory(term)
		    			.then(
		    				function(response) {
		    					$scope.businessDataCheckins = response.data
		    					
		    					var data = [];
		    					var categories = [];
		    					$scope.businessDataCheckins.forEach(function (p) {
		    				        if(!categories.includes(p.categories)) {
		    				        	categories.push(p.categories);
		    				        }
		    				        p.value = p.checkinCount;
		    				        
		    				        p = {
	    				        		name : p.name,
	    				        	    value: p.value,
	    				        	    categories: p.categories,
	    				        	    ratings: p.stars
	    				        	};
		    				        
		    				        if($scope.businessDataCheckins.length > 500) {
		    				        	 if(p.value > 1000) {
				    				        	data.push(p);
				    				        }
		    				        } else {
		    				        	data.push(p);
		    				        }
		    				       
		    				    });
		    					
		    					var data_comps = Object.create(null);
		    					data.forEach(function(business) {
		    				    	var data_comp = data_comps[business.categories];
		    				    	if (!data_comp) {
		    				    		data_comp = data_comps[business.categories] = [];
		    				    	}
		    				    	data_comp.push(business);
		    				    });
		    					
		    					for(var i=0; i<categories.length; i++) {
		    						var obj = {};
		    						obj.name = categories[i];
		    						var series_data = data_comps[categories[i]];
		    						series_data.forEach(function(v){ delete v.categories});
		    						obj.data = series_data;
		    						
		    						chart4.addSeries(obj);
		    					}
		    				},
		    				function(error) {
		    					console.log("error::", error);
		    				});
		            	}
		          }
	        },
	        title: {
	            text: 'Checkins for businesses by category'
	        },
	        tooltip: {
	            useHTML: true,
	            pointFormat: '<b>{point.name}:</b> {point.value}' +
	            '<br>Rating: {point.ratings}'
	        },
	        plotOptions: {
	            packedbubble: {
	                minSize: '30%',
	                maxSize: '120%',
	                layoutAlgorithm: {
	                    splitSeries: false,
	                    gravitationalConstant: 0.02
	                },
	                dataLabels: {
	                    enabled: true,
	                    format: '{point.value}',
	                    filter: {
	                        property: 'y',
	                        operator: '>',
	                        value: 250
	                    },
	                    style: {
	                        color: 'black',
	                        textOutline: 'none',
	                        fontWeight: 'normal'
	                    }
	                }
	            }
	        },
	        series: []
	    });
	
//		var x = Array.from(document.getElementById("figures_parent").children);
	    var x = document.querySelectorAll('#figures_parent > div');
		x = Array.prototype.slice.call(x);
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
	
	$scope.all_checkins = function(t) {
	    chart5 = H.chart('visits_vs_days_chart', {
	    	chart: {
	            type: 'column',
	            events: {
		            load: function () {
		            	chart5 = this;
		    			   			
		            	var categories = $scope.categories_model.map(function(item) {
	        			    return item.id
	        			});
		            	
		            	var ratings = $scope.ratings_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var term = {
	        				category : categories,
	        				rating : ratings
	        			};
	        			
		    			const Http = new XMLHttpRequest();
		    			var url = 
		    				SERVICE_URL 
		    				+ '/service/business/findAllCheckinsByCategoryAndRating'
		    				+ formatParams(term);;
		    			Http.open("GET", url, true);
		    			
		    			var start = 0;
		    			var end = 0;
		    			
		    			var days = [];
		    			var data_comps1 = Object.create(null);
		    			
		    			Http.onprogress = function () {
		    				end = Http.response.length;
		    				
		    				var resp = Http.response.substring(start, end);
		    				var resp_trimmed = "[" 
		    					+ resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1) 
    							+ "]";
		    				
		    				var resp_parsed = JSON.parse(resp_trimmed);
		    				
		    				start = end;
		    				
		    				resp_parsed.forEach(function (p) {
	    				        if(!days.includes(p.day)) {
	    				        	days.push(p.day);
	    				        }
	    				    });
		    			  	
		    				resp_parsed.forEach(function(checkin) {
	    				    	var data_comp = data_comps1[checkin.day];
	    				    	if (!data_comp) {
	    				    		data_comp = data_comps1[checkin.day] = [];
	    				    	}
	    				    	data_comp.push(checkin);
	    				    });
	    					
	    					chart5.series[0].setData([]);
	    					var series_component = [];
	    					for (var i=0; i<days.length; i++) {
	    						var series_data = {};
	    						series_data['name'] = days[i];
	    						series_data['y'] = data_comps1[days[i]].length;
	    						series_component.push(series_data);
	    					}
	    					chart5.series[0].setData(series_component);
	    				}
		    			
		    			Http.send();
    				}
	            }
	        },

	        title: {
	            text: 'Visits by Day of the week'
	        },

	        accessibility: {
	            announceNewData: {
	                enabled: true
	            }
	        },
	        
	        xAxis: {
	        	type: 'category',
	        	
	        	title: {
	        		text: 'Day of the week',
	        		style: {
		                fontSize: '20px'
		            }
	            },
	            labels: {
		            style: {
		                fontSize: '15px'
		            }
		        }
	        },
	        
	        yAxis: {
	            title: {
	            	margin: 40,
	            	text: 'Number of visits',
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
	        },
	        
	        legend: {
	            enabled: false
	        },
	        
	        plotOptions: {
	            series: {
	                borderWidth: 0,
	                dataLabels: {
	                    enabled: true,
	                    format: '{point.y}'
	                }
	            }
	        },

	        tooltip: {
	            pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y}</b><br/>'
	        },

	        series: [{
	        	boostThreshold: 0,
	            name: 'VisitsByDay',
	            colorByPoint: true,
	            data: []
	        }]
	    });
	
	    chart6 = H.chart('visits_vs_times_chart', {
	    	chart: {
	            type: 'column',
	            events: {
		            load: function () {
		            	chart6 = this;
		    			   			
		            	var categories = $scope.categories_model.map(function(item) {
	        			    return item.id
	        			});
		            	
		            	var ratings = $scope.ratings_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var term = {
	        				category : categories,
	        				rating : ratings
	        			};
	        			
		    			const Http = new XMLHttpRequest();
		    			var url = 
		    				SERVICE_URL 
		    				+ '/service/business/findAllCheckinsByCategoryAndRating'
		    				+ formatParams(term);;
		    			Http.open("GET", url, true);
		    			
		    			var start = 0;
		    			var end = 0;
		    			
		    			var hours = [];
		    			var data_comps2 = Object.create(null);
		    			
		    			Http.onprogress = function () {
		    				end = Http.response.length;
		    				
		    				var resp = Http.response.substring(start, end);
		    				var resp_trimmed = "[" 
		    					+ resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1) 
    							+ "]";
		    				
		    				var resp_parsed = JSON.parse(resp_trimmed);
		    				
		    				start = end;
		    				
		    				resp_parsed.forEach(function (p) {
	    				        if(!hours.includes(p.hour)) {
	    				        	hours.push(p.hour);
	    				        }
	    				    });
		    			  	
		    				resp_parsed.forEach(function(checkin) {
	    				    	var data_comp = data_comps2[checkin.hour];
	    				    	if (!data_comp) {
	    				    		data_comp = data_comps2[checkin.hour] = [];
	    				    	}
	    				    	data_comp.push(checkin);
	    				    });
	    					
	    					chart6.series[0].setData([]);
	    					var series_component = [];
	    					for (var i=0; i<hours.length; i++) {
	    						var series_data = {};
	    						series_data['name'] = hours[i];
	    						series_data['y'] = data_comps2[hours[i]].length;
	    						series_component.push(series_data);
	    					}
	    					chart6.series[0].setData(series_component);
	    				}
		    			
		    			Http.send();
    				}
	            }
	        },

	        title: {
	            text: 'Visits by Hour of the day'
	        },

	        accessibility: {
	            announceNewData: {
	                enabled: true
	            }
	        },

	        xAxis: {
	        	type: 'category',
	        	
	        	title: {
	        		text: 'Hour of the day',
	        		style: {
		                fontSize: '20px'
		            }
	            },
	            labels: {
		            style: {
		                fontSize: '15px'
		            }
		        }
	        },
	        
	        yAxis: {
	            title: {
	            	margin: 40,
	            	text: 'Number of visits',
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
	        },
	        
	        legend: {
	            enabled: false
	        },
	        
	        plotOptions: {
	            series: {
	                borderWidth: 0,
	                dataLabels: {
	                    enabled: true,
	                    format: '{point.y}'
	                }
	            }
	        },

	        tooltip: {
	            pointFormat: '<span style="color:{point.color}">Visits</span>: <b>{point.y}</b><br/>'
	        },

	        series: [{
	        	boostThreshold: 0,
	            name: 'VisitsByHour',
	            colorByPoint: true,
	            data: []
	        }]
	    });
	    
//		var x = Array.from(document.getElementById("figures_parent").children);
	    var x = document.querySelectorAll('#figures_parent > div');
		x = Array.prototype.slice.call(x);
		var elem = x[3];
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
	
	$scope.business_by_reviewSentimentVsTime = function(t) {
		chart1 = H.stockChart('figure_time_series', {
		    chart: {
		        events: {
		            load: function () {
		            	chart1 = this;
			   			
		            	var categories = $scope.categories_model.map(function(item) {
	        			    return item.id
	        			});
		            	
		            	var ratings = $scope.ratings_model.map(function(item) {
	        			    return item.id
	        			});
	        			
	        			var term = {
	        				category : categories,
	        				rating : ratings
	        			};
	        			
		    			const Http = new XMLHttpRequest();
		    			var url = 
		    				SERVICE_URL
		    				+ '/service/business/findReviewSentimentVsTimeByCategoryAndRating'
		    				+ formatParams(term);
		    			Http.open("GET", url, true);
		    			
		    			var start = 0;
		    			var end = 0;
		    			var points = [];
		    			
		    			Http.onprogress = function () {
		    				end = Http.response.length;
		    				
		    				var resp = Http.response.substring(start, end);
		    				var resp_trimmed = 
		    					resp.substring(resp.indexOf("{"), resp.lastIndexOf("}") + 1); 
		    				
		    				var resp_parsed = resp_trimmed.split(",");
		    				start = end;
		    				
		    				resp_parsed.forEach(function (p) {
		    					var kv_pair = [];
		    					
		    					var resp_obj = JSON.parse(p);
		    					
		    					for(var x in resp_obj) {
		    						kv_pair.push(new Date(x));
		    						kv_pair.push(parseFloat(resp_obj[x]) - 3);
		    						
		    						points.push(kv_pair);
	    						}
	    				    });
		    				
		    				points = points.sort(function(a,b){
		    					return a[0] - b[0];
	    					});
		    			  	
		    				chart1.series[0].setData(points);
	    				}
		    			
		    			Http.send();
		            }
		        }
		    },

		    time: {
		        useUTC: false
		    },

		    rangeSelector: {
		        buttons: [{
		            count: 1,
		            type: 'year',
		            text: '1Y'
		        }, {
		            count: 5,
		            type: 'year',
		            text: '5Y'
		        }, {
		            type: 'all',
		            text: 'All'
		        }],
		        inputEnabled: false,
		        selected: 0
		    },

		    title: {
		        text: 'Review sentiment Time series'
		    },
		    
		    xAxis: {
		    	 title: {
		            text: 'Time',
		            style: {
		                fontSize: '20px'
		            }
		        },
		        labels: {
		            style: {
		                fontSize: '15px'
		            }
		        }
		    },
		    yAxis: {
		    	opposite: false,
		        title: {
		        	margin: 40,
		            text: 'Review Sentiment Score',
		            style: {
		                fontSize: '17px'
		            }
		        },
		        labels: {
		        	margin: 20,
		            style: {
		                fontSize: '20px'
		            }
		        }
		    },

		    exporting: {
		        enabled: true
		    },

		    series: [{
		        name: 'Score',
		        data: [],
		        boostThreshold: 0
		    }]
		});

//		var x = Array.from(document.getElementById("figures_parent").children);
		var x = document.querySelectorAll('#figures_parent > div');
		x = Array.prototype.slice.call(x);
		var elem = x[4];
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
	
	$scope.business_by_rating(true);
	
}
]);