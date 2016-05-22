Template.registerHelper("currentLocationsIteration", function() {
  result = [];
  //finds all locations by current user id
  Times.find({user:Meteor.userId()}).forEach(function(i) {
    result.push({
      sID: i.sID,
      name: i.name
    });
  });
  return result;
});
Template.CalSelector.events({
  "click .table-row": function(e) {
    var id = $(e.target).closest('tr').data('name')
    Router.go("/edit/"+id)
  }
});
