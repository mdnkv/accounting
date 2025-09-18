import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {environment} from '../../../environments/environment';
import {Observable} from 'rxjs';
import {User} from '../models/users.models';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getCurrentUser(): Observable<User> {
    return this.http.get<User>(`${this.serverUrl}/users/current`)
  }

}
