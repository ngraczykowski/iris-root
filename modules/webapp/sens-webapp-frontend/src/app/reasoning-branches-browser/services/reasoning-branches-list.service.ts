import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  BulkChanges,
  BulkChangesResponse,
  PendingChanges,
  PendingChangesResponse,
  ReasoningBranchesList,
  ReasoningBranchesListRequest,
  ReasoningBranchesListResponse
} from '@app/reasoning-branches-browser/model/branches-list';
import { ReasoningBranchesService } from '@core/reasoning-branches/services/reasoning-branches.service';
import { environment } from '@env/environment';
import { forkJoin, Observable, of } from 'rxjs';
import { map, switchMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchesListService {

  constructor(
      private http: HttpClient,
      private reasoningBranchesService: ReasoningBranchesService
  ) { }

  getReasoningBranchesList(request: ReasoningBranchesListRequest): Observable<ReasoningBranchesListResponse> {
    return this.reasoningBranchesService.getList(request).pipe(
        switchMap((response: ReasoningBranchesListResponse) => response.branches.length ? forkJoin({
          branchesList: of(response),
          bulkChanges: this.getBulkChangesIds(response.branches),
          pendingChanges: this.getPendingChanges(),
        }).pipe(
            map(data => {
                  const bulkChanges: BulkChanges[] = data.bulkChanges
                      .map((bulkChange: BulkChangesResponse) => {
                        return {
                          ...bulkChange,
                          bulkChanges: bulkChange.bulkChangeIds.map((bulkChangeId: string) =>
                              data.pendingChanges.find((pendingChange: PendingChangesResponse) =>
                                  bulkChangeId === pendingChange.bulkChangeId))
                              .filter(anyValue => typeof anyValue !== 'undefined')
                        };
                      });

                  const result: ReasoningBranchesListResponse = {
                    ...data.branchesList,
                    branches: data.branchesList.branches.map((branch: ReasoningBranchesList) => {
                      return {
                        ...branch,
                        pendingChanges: this.findPendingChanges(bulkChanges, branch)
                      };
                    })
                  };
                  return result;
                }
            )
        ) : of(response))
    );
  }

  private findPendingChanges(bulkChanges: BulkChanges[], branch: ReasoningBranchesList): PendingChanges[] {
    const bulkChange: BulkChanges = bulkChanges.find((change: BulkChanges) =>
        branch.reasoningBranchId.featureVectorId === change.reasoningBranchId.featureVectorId &&
        branch.reasoningBranchId.decisionTreeId === change.reasoningBranchId.decisionTreeId);
    return bulkChange ? bulkChange.bulkChanges : null;
  }

  private getBulkChangesIds(branches): Observable<Array<BulkChangesResponse>> {
    const params = this.generateBulkChangeParams(branches);
    return this.http.get<Array<BulkChangesResponse>>(
        `${environment.serverApiUrl}/bulk-changes/ids`
        + `?reasoningBranchId=${params}`
        + `&statesFamily=pending`);
  }

  private generateBulkChangeParams(branches) {
    const params = [];
    branches.forEach(branch => {
      params.push(`${branch.reasoningBranchId.decisionTreeId}-${branch.reasoningBranchId.featureVectorId}`);
    });
    return params.join(',');
  }

  private getPendingChanges(): Observable<Array<PendingChangesResponse>> {
    return this.http.get<Array<PendingChangesResponse>>(
        `${environment.serverApiUrl}/change-requests?statesFamily=pending`);
  }
}
