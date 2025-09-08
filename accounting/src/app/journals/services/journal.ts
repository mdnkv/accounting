import {inject, Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';

import {environment} from '../../../environments/environment';
import {Journal} from '../models/journals.models';

@Injectable({
  providedIn: 'root'
})
export class JournalService {

  http: HttpClient = inject(HttpClient)
  serverUrl: string = environment.serverUrl

  createJournal(payload: Journal): Observable<Journal>{
    return this.http.post<Journal>(`${this.serverUrl}/journals/create`, payload)
  }

  updateJournal(payload: Journal): Observable<Journal>{
    return this.http.put<Journal>(`${this.serverUrl}/journals/update`, payload)
  }

  deleteJournal(id: string): Observable<void>{
    return this.http.delete<void>(`${this.serverUrl}/journals/delete/${id}`)
  }

  getJournals(organizationId: string): Observable<Journal[]>{
    return this.http.get<Journal[]>(`${this.serverUrl}/journals/organization/${organizationId}`)
  }

  getJournal(id: string): Observable<Journal> {
    return this.http.get<Journal>(`${this.serverUrl}/journals/journal/${id}`)
  }


}
