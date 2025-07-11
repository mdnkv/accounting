import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {map, Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {Role} from '../models/roles.models';


@Injectable({
  providedIn: 'root'
})
export class RoleService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  getActiveRole (): Observable<Role> {
    return this.http.get<Role>(`${this.serverUrl}/roles/active`).pipe(
      map(result => {
        localStorage.setItem('activeOrganizationId', result.organization.id!)
        localStorage.setItem('activeRoleType', result.roleType)
        return result
      })
    )
  }

  getRoles (): Observable<Role[]>{
    return this.http.get<Role[]>(`${this.serverUrl}/roles/user`)
  }

  setActiveRole(id: string): Observable<Role>{
    return this.http.post<Role>(`${this.serverUrl}/roles/active/${id}`, {}).pipe(
      map(result => {
        localStorage.setItem('activeOrganizationId', result.organization.id!)
        localStorage.setItem('activeRoleType', result.roleType)
        return result
      })
    )
  }


}
