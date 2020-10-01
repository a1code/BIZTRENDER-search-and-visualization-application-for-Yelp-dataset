biztrenderApp.directive('multiSelect', function() {

  function link(scope, element) {
    var options = {
      enableClickableOptGroups: true,
      includeSelectAllOption: true,
      enableCollapsibleOptGroups: true,
      onChange: function() {
        element.change();
        //scope.onChange();
      }
    };
    element.multiselect(options);
  }

  return {
    restrict: 'A',
    link: link
  };
});