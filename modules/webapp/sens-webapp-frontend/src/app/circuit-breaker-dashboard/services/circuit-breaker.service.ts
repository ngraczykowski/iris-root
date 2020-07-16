import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {
  DiscrepanciesList,
  DiscrepantBranchesResponse
} from '@app/circuit-breaker-dashboard/models/circuit-breaker';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CircuitBreakerService {

  constructor(
      private http: HttpClient
  ) { }

  getBranchesWithDiscrepancies(): Observable<Array<DiscrepantBranchesResponse>> {
    return this.http.get<Array<DiscrepantBranchesResponse>>(
        `${environment.serverApiUrl}/discrepant-branches?withArchivedDiscrepancies=false`);
  }

  getBranchesWithArchivedDiscrepancies(): Observable<Array<DiscrepantBranchesResponse>> {
    return this.http.get<Array<DiscrepantBranchesResponse>>(
        `${environment.serverApiUrl}/discrepant-branches?withArchivedDiscrepancies=true`);
  }

  getDiscrepanciesIds(discrepancyId): Observable<Array<any>> {
    return this.http.get<Array<any>>(`${environment.serverApiUrl}/discrepant-branches/${discrepancyId}/discrepancy-ids`);
  }

  getDiscrepanciesList(discrepanciesIds): Observable<Array<DiscrepanciesList>> {
    return this.http.get<Array<DiscrepanciesList>>(`${environment.serverApiUrl}/discrepancies?id=${discrepanciesIds}`);
  }

  archiveDiscrepancies(discrepanciesIds: any[]): Observable<any> {
    return this.http.patch(`${environment.serverApiUrl}/discrepancies/archive`, [...discrepanciesIds]);
  }
}
