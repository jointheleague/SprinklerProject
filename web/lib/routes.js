Router.route("/", function(){
  this.render("main");
});
Router.route("cal", function(){
  this.render("cal");
});
Router.route("edit/:id", function(){
  CurrentEdit = this.params.id;
this.render("edit");

});
Router.map(function () {
  this.route('get/:ID', {
    path: '/get/:ID',
    where: 'server',
    action: function () {
      var json = Times.findOne({sID: this.params.ID});
      this.response.setHeader('Content-Type', 'application/json');
      this.response.end(JSON.stringify(json));
    }
  });
});
