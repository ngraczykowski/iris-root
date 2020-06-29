import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChangeRequestDecisionRequestBody } from '@app/pending-changes/models/change-request-decision-request-body';
import { ChangeRequestDecision } from '@app/pending-changes/models/change-request-decision.enum';
import {
  BulkChangeResponse,
  PendingChangeResponse
} from '@app/pending-changes/models/pending-changes';
import { environment } from '@env/environment';
import { forkJoin, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class PendingChangesService {

  constructor(
      private http: HttpClient
  ) { }

  getPendingChanges(): Observable<Array<PendingChangeResponse>> {
    return this.http.get<Array<PendingChangeResponse>>(`${environment.serverApiUrl}/change-requests/pending`);
  }

  getBulkChanges(): Observable<Array<BulkChangeResponse>> {
    return this.http.get<Array<BulkChangeResponse>>(`${environment.serverApiUrl}/bulk-changes`);
  }

  getPendingChangesDetails() {
    return forkJoin(
        {
          pendingChanges: this.getPendingChanges(),
          bulkChanges: this.getBulkChanges()
        }
    ).pipe(
        map(data => {
          return data.bulkChanges
              .filter(bulkChange =>
                  data.pendingChanges.filter(
                      pendingChange => pendingChange.bulkChangeId === bulkChange.id).length > 0)
              .map(bulkChanges => {
                return {
                  ...bulkChanges, ...data.pendingChanges.filter(
                      pendingChanges => pendingChanges.bulkChangeId === bulkChanges.id
                  )[0]
                };
              });
        })
    );
  }

  changeRequestDecision(changeRequestID: string, decision: ChangeRequestDecision, body: ChangeRequestDecisionRequestBody) {
    return this.http.patch(
        `${environment.serverApiUrl}/change-requests/${changeRequestID}/${decision}`,
        body,
        {
          headers: {...({CorrelationId: uuidv4()})}
        });
  }
}
