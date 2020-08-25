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
import { forkJoin, Observable, of, Subject } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';
import { v4 as uuidv4 } from 'uuid';

@Injectable({
  providedIn: 'root'
})
export class PendingChangesService {

  queuedPendingChangesCount: Subject<number> = new Subject<number>();

  constructor(
      private http: HttpClient
  ) { }

  getPendingChangesDetails(changeRequestStatuses: PendingChangesStatus[]): Observable<Array<PendingChange>> {
    const shouldFetchPendingChanges = changeRequestStatuses.includes(PendingChangesStatus.PENDING);
    const urlSuffix = shouldFetchPendingChanges ? 'pending' : 'closed';

    return this.getPendingChanges(urlSuffix)
        .pipe(
            this.collectBulkChangeIds(),
            this.fetchBulkChangesByIds(urlSuffix),
            this.mergePendingChangesAndBulkChangesData()
        );
  }

  private collectBulkChangeIds(): any {
    return map((pendingChanges: PendingChangeResponse[]) => {
      return {
        pendingChanges: pendingChanges,
        bulkChangesIds: pendingChanges
            .map((pendingChange: PendingChangeResponse) => pendingChange.bulkChangeId)
      };
    });
  }

  private fetchBulkChangesByIds(urlSuffix: string) {
    return switchMap((data: PendingChangesAndBulkChangesIds) =>
        forkJoin({
              pendingChanges: of(data.pendingChanges),
              bulkChanges: this.getBulkChangesByIds(urlSuffix, data.bulkChangesIds)
            }
        )
    );
  }

  private mergePendingChangesAndBulkChangesData() {
    return map((data: PendingChangesAndBulkChanges) => {
      return data.pendingChanges.map(pendingChange => {
        const bulkChangeData: BulkChangeResponse = data.bulkChanges.find(bc => pendingChange.bulkChangeId === bc.id);

        return {
          ...pendingChange,
          reasoningBranchIds: bulkChangeData.reasoningBranchIds,
          aiSolution: bulkChangeData.aiSolution,
          active: bulkChangeData.active
        };
      });
    });
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

  private getPendingChanges(urlSufix: string): Observable<Array<PendingChangeResponse>> {
    return this.http.get<Array<PendingChangeResponse>>(
        `${environment.serverApiUrl}/change-requests?statesFamily=${urlSufix}`);
  }

  private getBulkChangesByIds(urlSufix: string, ids: string[]): Observable<Array<BulkChangeResponse>> {
    return this.http.get<Array<BulkChangeResponse>>(
        `${environment.serverApiUrl}/bulk-changes?statesFamily=${urlSufix}&ids=${ids}`);
  }
}

interface PendingChangesAndBulkChangesIds {
  pendingChanges: PendingChangeResponse[];
  bulkChangesIds: string[];
}

interface PendingChangesAndBulkChanges {
  pendingChanges: any;
  bulkChanges: any;
}
