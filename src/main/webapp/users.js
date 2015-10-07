/// <reference path="typings/angular2/angular2.d.ts"/>
/// <reference path="typings/es6-shim/es6-shim.d.ts"/>
/// <reference path="typings/es6-promise/es6-promise.d.ts"/>
if (typeof __decorate !== "function") __decorate = function (decorators, target, key, desc) {
    if (typeof Reflect === "object" && typeof Reflect.decorate === "function") return Reflect.decorate(decorators, target, key, desc);
    switch (arguments.length) {
        case 2: return decorators.reduceRight(function(o, d) { return (d && d(o)) || o; }, target);
        case 3: return decorators.reduceRight(function(o, d) { return (d && d(target, key)), void 0; }, void 0);
        case 4: return decorators.reduceRight(function(o, d) { return (d && d(target, key, o)) || o; }, desc);
    }
};
if (typeof __metadata !== "function") __metadata = function (k, v) {
    if (typeof Reflect === "object" && typeof Reflect.metadata === "function") return Reflect.metadata(k, v);
};
var angular2_1 = require("angular2/angular2");
var UsersComponent = (function () {
    function UsersComponent() {
        this.name = 'James';
    }
    UsersComponent = __decorate([
        angular2_1.Component({
            selector: 'users-app'
        }),
        angular2_1.View({
            template: '<h1>Hello {{name}}</h1'
        }), 
        __metadata('design:paramtypes', [])
    ], UsersComponent);
    return UsersComponent;
})();
angular2_1.bootstrap(UsersComponent);
