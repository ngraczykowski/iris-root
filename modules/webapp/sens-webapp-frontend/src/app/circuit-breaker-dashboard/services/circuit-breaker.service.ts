import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  CircuitBreakerDiscrepancyStatus,
  DiscrepanciesList,
  DiscrepantBranchesResponse
} from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { environment } from '@env/environment';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CircuitBreakerService {

  private activeDiscrepantBranchesCount = new Subject<any>();

  private static shouldUpdateActiveCount(discrepancyStatuses: CircuitBreakerDiscrepancyStatus[]) {
    return discrepancyStatuses.includes(CircuitBreakerDiscrepancyStatus.ACTIVE);
  }

  constructor(
      private http: HttpClient
  ) { }

  getBranchesWithDiscrepancies(discrepancyStatuses: CircuitBreakerDiscrepancyStatus[]):
      Observable<Array<DiscrepantBranchesResponse>> {
    const observable = this.http.get<Array<DiscrepantBranchesResponse>>(
        `${environment.serverApiUrl}/discrepant-branches`
        + `?discrepancyStatuses=${discrepancyStatuses}`);

    if (CircuitBreakerService.shouldUpdateActiveCount(discrepancyStatuses)) {
      observable.subscribe(data => this.activeDiscrepantBranchesCount.next(data.length));
    }
    return observable;
  }

  getDiscrepanciesIds(discrepancyId, discrepancyStatuses: CircuitBreakerDiscrepancyStatus[]):
      Observable<Array<any>> {
    return this.http.get<Array<any>>(
        `${environment.serverApiUrl}/discrepant-branches/${discrepancyId}`
        + `/discrepancy-ids?discrepancyStatuses=${discrepancyStatuses}`);
  }

  getDiscrepanciesList(discrepanciesIds): Observable<Array<DiscrepanciesList>> {
    return this.http.get<Array<DiscrepanciesList>>(
        `${environment.serverApiUrl}/discrepancies?id=${discrepanciesIds}`);
  }

  archiveDiscrepancies(discrepanciesIds: any[]): Observable<any> {
    return this.http.patch(
        `${environment.serverApiUrl}/discrepancies/archive`, [...discrepanciesIds]);
  }

  getActiveDiscrepantBranchesCount(): Observable<any> {
    return this.activeDiscrepantBranchesCount.asObservable();
  }

}
