import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';

import {Role} from '../models/roles.models';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getRoles(organizationId: string): Observable<Role[]> {
    return this.http.get<Role[]>(`${this.serverUrl}/roles/organization/${organizationId}`)
  }

}
