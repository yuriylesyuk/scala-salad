/// <reference path="typings/angular2/angular2.d.ts"/>
/// <reference path="typings/es6-shim/es6-shim.d.ts"/>
/// <reference path="typings/es6-promise/es6-promise.d.ts"/>

import {bootstrap, Component, View} from "angular2/angular2";

class User {
	id: number;
	name: string;
	age: number;
	role: string;		
}

@Component({
	selector: 'users-app'
})
@View({
	template: '<h1>Hello {{name}}</h1'
})
class UsersComponent {
	name: string;
	users: [User];
	
	
	constructor() {
		this.name = 'James';
		
		this.users ='[{"id":1,"name":"Janes","age":25,"position":"Developer"},{"id":1,"name":"Watson","age":25,"position":"Manager"}]'
	}
}

bootstrap(UsersComponent)