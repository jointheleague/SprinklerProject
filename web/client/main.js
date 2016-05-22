import { Template } from 'meteor/templating';
import { ReactiveVar } from 'meteor/reactive-var';

import './main.html';
Meteor.subscribe("times");

Template.main.events({
  "click #MakeNew": function(e){
    Router.go("/cal");
  }
});

Template.registerHelper("name", function(){
return ProjectName;
});
