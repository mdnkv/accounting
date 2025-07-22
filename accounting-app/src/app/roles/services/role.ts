import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment.development';
import {Role} from '../models/roles.models';

@Injectable({
  providedIn: 'root'
})
export class RoleService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getRolesForOrganization(): Observable<Role[]>{
    const organizationId = localStorage.getItem('activeOrganizationId') as string
    return this.http.get<Role[]>(`${this.serverUrl}/roles/organization/${organizationId}`)

  }

}
