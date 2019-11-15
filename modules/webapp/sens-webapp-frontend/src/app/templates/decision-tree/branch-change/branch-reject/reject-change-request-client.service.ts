import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChangeRequest } from '@app/templates/decision-tree/branch-change/branch-reject/reject-request.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable()
export class RejectChangeRequestClient {

  constructor(private http: HttpClient) { }

  rejectChange(request: ChangeRequest): Observable<any> {
    return this.http.post(
        `${environment.serverApiUrl}api/changes/reject`, request);
  }
}
