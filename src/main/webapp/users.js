var __decorate = (this && this.__decorate) || function (decorators, target, key, desc) {
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") return Reflect.decorate(decorators, target, key, desc);
    switch (arguments.length) {
        case 2: return decorators.reduceRight(function(o, d) { return (d && d(o)) || o; }, target);
        case 3: return decorators.reduceRight(function(o, d) { return (d && d(target, key)), void 0; }, void 0);
        case 4: return decorators.reduceRight(function(o, d) { return (d && d(target, key, o)) || o; }, desc);
    }
};
var __metadata = (this && this.__metadata) || function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var angular2_1 = require("angular2/angular2");
var http_1 = require('angular2/http');
var usersservice_1 = require('usersservice');
var User = (function () {
    function User(userData) {
        this.id = userData.id;
        this.name = userData.name;
        this.age = userData.age;
        this.role = userData.role;
    }
    return User;
})();
exports.User = User;
var UsersComponent = (function () {
    function UsersComponent(fb, usersAPI) {
        var _this = this;
        this.name = 'James';
        // '[{"id":1,"name":"Janes","age":25,"position":"Developer"},{"id":1,"name":"Watson","age":25,"position":"Manager"}]'
        usersAPI.getUsers().then(function (users) {
            _this.users = users;
        });
        //this.users = [new User({id:3,name:"xx", age:5, role:"xx"})];
        this.form = fb.group({
            name: [this.name],
            users: [this.users]
        });
    }
    UsersComponent.prototype.onSubmit = function (value) {
        console.log('value: ', value);
    };
    UsersComponent = __decorate([
        angular2_1.Component({
            selector: 'users-app',
        }),
        angular2_1.View({
            templateUrl: 'users.html',
            directives: [angular2_1.FORM_DIRECTIVES, angular2_1.NgFor]
        }), 
        __metadata('design:paramtypes', [angular2_1.FormBuilder, (typeof (_a = typeof usersservice_1.UsersAPI !== 'undefined' && usersservice_1.UsersAPI) === 'function' && _a) || Object])
    ], UsersComponent);
    return UsersComponent;
    var _a;
})();
exports.UsersComponent = UsersComponent;
angular2_1.bootstrap(UsersComponent, [http_1.HTTP_BINDINGS, usersservice_1.UsersAPI]);
