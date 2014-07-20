App.Model = {};

App.Model.App = Backbone.RelationalModel.extend({
	defaults: function() {
		return {
			updated: "Never",
			loggedin: false
		};
	},
	relations: [{
		type: Backbone.HasMany,
		key: 'students',
		relatedModel: 'App.Model.Student',
		collectionType: 'App.Model.Students'
	}]
});

App.Model.Absence = Backbone.Model.extend({
	initialize: function() {

	}
});
App.Model.Absences = Backbone.Collection.extend({
	model: App.Model.Absence,
	localStorage: new Backbone.LocalStorage("Absences")
});

App.Model.Event = Backbone.Model.extend({
	initialize: function() {

	}
});
App.Model.Events = Backbone.Collection.extend({
	model: App.Model.Event,
	localStorage: new Backbone.LocalStorage("Events")
});

App.Model.DataUpload = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'students',
		relatedModel: 'Student',
		collectionType: 'Students'
	}],
	url: "/m/data/upload"
});

App.Model.ClassList = Backbone.RelationalModel.extend({
	relations: [{
		type: Backbone.HasMany,
		key: 'students',
		relatedModel: 'Student',
		collectionType: 'Students'
	}],
	url: "/m/data/classlist"
});


App.Model.Student = Backbone.RelationalModel.extend({
	initialize: function() {

	}
});

App.Model.Students = Backbone.Collection.extend({
	model: App.Model.Student,
	localStorage: new Backbone.LocalStorage("Students")
});

