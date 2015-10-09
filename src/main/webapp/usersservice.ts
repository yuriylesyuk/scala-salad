import {Injectable} from 'angular2/angular2';
import {Http} from 'angular2/http';

import {User} from 'users';

@Injectable()
export class UsersAPI {
	constructor(private http:Http){
	}
	
	getUsers(): Promise<User[]> {
		return( window.fetch('/api/v1/users')
			.then((response)=> response.json())
			.then((usersData)=> {
				return usersData.map(user => this.parseUserData(user));
			})
		)		
	}
	
	private parseUserData(userData){
		return new User(userData)
	}
}