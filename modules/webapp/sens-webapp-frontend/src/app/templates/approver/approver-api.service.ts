import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApprovalQueueView, ChangeRequest } from '@app/templates/approver/approver.model';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable()
export class ApproverApiService {

  constructor(private http: HttpClient) { }

  fetchApprovalQueue(): Observable<ChangeRequest[]> {
    const endpointPath = `${environment.serverApiUrl}api/approval-queue`;

    return this.http.get<ApprovalQueueView>(endpointPath)
        .pipe(map(queueView => queueView.changeRequests));
  }

  rejectChanges(changes: ChangeRequest[], comment: string): Observable<any> {
    const endpointPath = `${environment.serverApiUrl}api/changes/bulk/reject`;

    return this.http.post(endpointPath, {
      changeIds: changes.map(change => change.changeId),
      comment
    });
  }

  approveChanges(changes: ChangeRequest[], comment: string): Observable<any> {
    const endpointPath = `${environment.serverApiUrl}api/changes/bulk/approve`;

    return this.http.post(endpointPath, {
      changeIds: changes.map(change => change.changeId),
      comment
    });
  }
}
