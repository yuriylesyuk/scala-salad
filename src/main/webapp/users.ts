import {bootstrap, Component, View,
	FormBuilder, Directive,
	FORM_DIRECTIVES, NgFormModel, NgIf, NgFor} from "angular2/angular2";
import {HTTP_BINDINGS} from 'angular2/http';	
import {UsersAPI} from 'usersservice';

export class User {
	id: number;
	name: string;
	age: number;
	role: string;

	constructor(userData: any){
		this.id = userData.id;
		this.name = userData.name;
		this.age = userData.age;
		this.role = userData.role;
	}	
}

@Component({
	selector: 'users-app',
})
@View({
	templateUrl: 'users.html',
	directives: [FORM_DIRECTIVES, NgFor]
})
export class UsersComponent {
	name: string;
	users: Array<User>;
	
	form: ng.ControlGroup;
	
	
	constructor(fb: FormBuilder, usersAPI: UsersAPI ) {
		this.name = 'James';
		
		// '[{"id":1,"name":"Janes","age":25,"position":"Developer"},{"id":1,"name":"Watson","age":25,"position":"Manager"}]'
		usersAPI.getUsers().then((users)=>{
			this.users = users;
		})
		//this.users = [new User({id:3,name:"xx", age:5, role:"xx"})];
		
		this.form = fb.group({
			name: [this.name],
			users: [this.users]
		})
	}
	
	onSubmit(value){
		console.log('value: ',value);
	}
}

bootstrap(UsersComponent, [HTTP_BINDINGS,UsersAPI])