import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  BulkChangeResponse,
  PendingChangeResponse
} from '@app/pending-changes/models/pending-changes';
import { environment } from '@env/environment';
import { forkJoin, Observable, throwError } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class PendingChangesService {

  constructor(
      private http: HttpClient
  ) { }

  getPendingChanges(): Observable<Array<PendingChangeResponse>> {
    return this.http.get<Array<PendingChangeResponse>>(`${environment.serverApiUrl}/change-requests`);
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
          return data.bulkChanges.map(bulkChanges => {
            return {
              ...bulkChanges, ...data.pendingChanges.filter(
                  pendingChanges => pendingChanges.bulkChangeId === bulkChanges.id
              )[0]
            };
          });
        })
    );
  }

  changeRequestDecision(changeRequestID: string, decision: string) {
    return this.http.patch(`${environment.serverApiUrl}/change-request/${changeRequestID}/${decision}`, null);
  }
}
