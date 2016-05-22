
Template.cal.helpers({
});

Template.cal.events({
  "click #Update": function(e){
    e.preventDefault();
    var id = document.getElementById('sID').value;
    var name = document.getElementById('name').value;

    var Monday = {
      power: $('#MondayCheck').is(':checked'),
      start: document.getElementById('mondayStart').value,
      end: document.getElementById('mondayEnd').value,
    }
    var Tuesday = {
      power: $('#TuesdayCheck').is(':checked'),
      start: document.getElementById('TuesdayStart').value,
      end: document.getElementById('TuesdayEnd').value,
    }
    var Wednesday = {
      power: $('#WednesdayCheck').is(':checked'),
      start: document.getElementById('WednesdayStart').value,
      end: document.getElementById('WednesdayEnd').value,
    }
    var Thursday = {
      power: $('#ThursdayCheck').is(':checked'),
      start: document.getElementById('ThursdayStart').value,
      end: document.getElementById('ThursdayEnd').value,
    }
    var Friday = {
      power: $('#FridayCheck').is(':checked'),
      start: document.getElementById('FridayStart').value,
      end: document.getElementById('FridayEnd').value,
    }
    var Saturday = {
      power: $('#SaturdayCheck').is(':checked'),
      start: document.getElementById('SaturdayStart').value,
      end: document.getElementById('SaturdayEnd').value,
    }
    var Sunday = {
      power: $('#SundayCheck').is(':checked'),
      start: document.getElementById('SundayStart').value,
      end: document.getElementById('SundayEnd').value,
    }

    if (Times.findOne({sID: id}) === undefined) {
      Times.insert({
        sID: id,
        user:Meteor.userId(),
        name: name,
        Monday:Monday,
        Tuesday:Tuesday,
        Wednesday:Wednesday,
        Thursday:Thursday,
        Friday:Friday,
        Saturday:Saturday,
        Sunday:Sunday
      });
      Router.go('/');
    }else {
      alert("That data id exists. YOU CANT USE IT!");
    }
    console.log(times.findOne({sID: id}));
}
});
