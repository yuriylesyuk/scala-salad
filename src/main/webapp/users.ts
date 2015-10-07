/// <reference path="typings/angular2/angular2.d.ts"/>
/// <reference path="typings/es6-shim/es6-shim.d.ts"/>
/// <reference path="typings/es6-promise/es6-promise.d.ts"/>

import {bootstrap, Component, View} from "angular2/angular2";

@Component({
	selector: 'users-app'
})
@View({
	template: '<h1>Hello {{name}}</h1'
})
class UsersComponent {
	name: string;
	
	constructor() {
		this.name = 'James';
	}
}

bootstrap(UsersComponent)