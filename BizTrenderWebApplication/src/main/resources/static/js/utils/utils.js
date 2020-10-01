var convertTreeData = function(array){
    var map = {};
    for(var i = 0; i < array.length; i++){
        var obj = array[i];
        obj.children= [];

        map[obj.id] = obj;

        var parent = obj.parentNodeId || '-';
        if(!map[parent]){
            map[parent] = {
                children: []
            };
        }
        map[parent].children.push(obj);
    }
    return map['-'].children;
}

var capitalizeText = function(str) {
    str = str.toLowerCase();
    return str.replace(/\w\S*/g, function(txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}

var correctString = function(str) {
    var crtStr = str.replace("_", " ");
    crtStr = crtStr.toLowerCase();
    return crtStr.replace(/\w\S*/g, function(txt) {
        return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}

var formatDate =  function(date) {
    var d = new Date(date),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
}

var emptyObjCheck = function(obj){
	for(var key in obj) {
        if(obj.hasOwnProperty(key))
            return false;
    }
    return true;
}

var randColor = function () {
    var color = (function lol(m, s, c) {
                    return s[m.floor(m.random() * s.length)] +
                        (c && lol(m, s, c - 1));
                })(Math, '3456789ABCDEF', 4);
    return color;
}

var callbackFun = function(value) {
    setTimeout(function() {
        angular.element(document.getElementById('pivotTableUI')).scope().rowLabelClick(value);
    }, 0);
}

var xLabelClick = function(value){
    setTimeout(function() {
        angular.element(document.getElementById('listResultTabs')).scope().xLabelClick(value);
    }, 0);
}

var pivotChartLabelClick = function(val){
	setTimeout(function() {
        angular.element(document.getElementById('pivotTableUI')).scope().pivotChartLabelClick(val);
    }, 0);
}

var getAllCategories = function(){
	var categories = [{
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
      "label": "Real estate",
      "id": "Real estate"
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
}

var getAllRatingCategories = function(){
	var rating_categories = [{
      "label": ">1 <=2",
      "id": 1.0
  }, {
      "label": ">2 <=3",
      "id": 2.0
  }, {
      "label": ">3 <=4",
      "id": 3.0
  }, {
      "label": ">4 <=5",
      "id": 4.0
  }];
}