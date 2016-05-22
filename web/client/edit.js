
Template.registerHelper("CurrentData", function(){
  return Times.findOne({sID:CurrentEdit});
});


Template.edit.events({
  "click #Update": function(e){
    e.preventDefault();
    var id = document.getElementById('sIDEdit').value;
    var name = document.getElementById('nameEdit').value;

    var Monday = {
      power: $('#MondayCheckEdit').is(':checked'),
      start: document.getElementById('mondayStartEdit').value,
      end: document.getElementById('mondayEndEdit').value,
    }
    var Tuesday = {
      power: $('#TuesdayCheckEdit').is(':checked'),
      start: document.getElementById('TuesdayStartEdit').value,
      end: document.getElementById('TuesdayEndEdit').value,
    }
    var Wednesday = {
      power: $('#WednesdayCheckEdit').is(':checked'),
      start: document.getElementById('WednesdayStartEdit').value,
      end: document.getElementById('WednesdayEndEdit').value,
    }
    var Thursday = {
      power: $('#ThursdayCheckEdit').is(':checked'),
      start: document.getElementById('ThursdayStartEdit').value,
      end: document.getElementById('ThursdayEndEdit').value,
    }
    var Friday = {
      power: $('#FridayCheckEdit').is(':checked'),
      start: document.getElementById('FridayStartEdit').value,
      end: document.getElementById('FridayEndEdit').value,
    }
    var Saturday = {
      power: $('#SaturdayCheckEdit').is(':checked'),
      start: document.getElementById('SaturdayStartEdit').value,
      end: document.getElementById('SaturdayEndEdit').value,
    }
    var Sunday = {
      power: $('#SundayCheckEdit').is(':checked'),
      start: document.getElementById('SundayStartEdit').value,
      end: document.getElementById('SundayEndEdit').value,
    }

    if (Times.findOne({sID: id}) != undefined) {
      Times.update({sId:id}, {$set:{
        sID: id,
        name: name,
        Monday:Monday,
        Tuesday:Tuesday,
        Wednesday:Wednesday,
        Thursday:Thursday,
        Friday:Friday,
        Saturday:Saturday,
        Sunday:Sunday
      }});
      Router.go("/");
    }else {
      alert("You cant create a new one here");
    }
  }
});
