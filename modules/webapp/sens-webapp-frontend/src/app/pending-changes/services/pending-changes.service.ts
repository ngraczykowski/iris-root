import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ChangeRequestDecisionRequestBody } from '@app/pending-changes/models/change-request-decision-request-body';
import { ChangeRequestDecision } from '@app/pending-changes/models/change-request-decision.enum';
import {
  BulkChangeResponse,
  PendingChange,
  PendingChangeResponse,
  PendingChangesStatus
} from '@app/pending-changes/models/pending-changes';
import { environment } from '@env/environment';
import { forkJoin, Observable, Subject } from 'rxjs';
import { map, tap } from 'rxjs/operators';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class PendingChangesService {

  queuedPendingChangesCount: Subject<number> = new Subject<number>();

  constructor(
      private http: HttpClient
  ) { }

  private getPendingChanges(urlSufix: string): Observable<Array<PendingChangeResponse>> {
    return this.http.get<Array<PendingChangeResponse>>(
        `${environment.serverApiUrl}/change-requests?statesFamily=${urlSufix}`);
  }

  private getBulkChanges(urlSufix: string): Observable<Array<BulkChangeResponse>> {
    return this.http.get<Array<BulkChangeResponse>>(
        `${environment.serverApiUrl}/bulk-changes?statesFamily=${urlSufix}`);
  }

  getPendingChangesDetails(changeRequestStatuses: PendingChangesStatus[]): Observable<Array<PendingChange>> {
    const shouldFetchPendingChanges = changeRequestStatuses.includes(PendingChangesStatus.PENDING);
    const urlSufix = shouldFetchPendingChanges ? 'pending' : 'closed';

    return forkJoin(
        {
          pendingChanges: this.getPendingChanges(urlSufix),
          bulkChanges: this.getBulkChanges(urlSufix)
        }
    ).pipe(
        map(data =>
            data.bulkChanges
                .filter(bulkChange =>
                    data.pendingChanges.filter(
                        pendingChange => pendingChange.bulkChangeId === bulkChange.id).length > 0)
                .map(bulkChanges => {
                  return {
                    ...bulkChanges, ...data.pendingChanges.filter(
                        pendingChanges => pendingChanges.bulkChangeId === bulkChanges.id
                    )[0]
                  };
                })
        ),
        tap(
            data => {
              if (shouldFetchPendingChanges) {
                this.queuedPendingChangesCount.next(data.length);
              }
            }
        )
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

  getQueuedPendingChangesCount() {
    return this.queuedPendingChangesCount.asObservable();
  }
}
