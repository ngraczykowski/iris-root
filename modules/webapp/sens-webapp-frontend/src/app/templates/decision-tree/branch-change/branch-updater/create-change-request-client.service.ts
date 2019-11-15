import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  ChangeRequest,
  ProposeChangesResponse
} from '@app/templates/decision-tree/branch-change/branch-updater/change-request.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable()
export class CreateChangeRequestClient {

  constructor(private http: HttpClient) { }

  createChanges(request: ChangeRequest): Observable<ProposeChangesResponse> {
    return this.http.post<ProposeChangesResponse>(
        `${environment.serverApiUrl}api/changes/create`, request);
  }
}
