import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { ReasoningBranchDetails } from '../models/reasoning-branch-management';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchManagementService {

  constructor(
    private http: HttpClient
  ) { }

  getReasoningBranch(id: string): Observable<ReasoningBranchDetails> {
    const decisionTreeId = this.parseId(id)[0];
    const reasoningBranchId = this.parseId(id)[1];

    // tslint:disable-next-line:max-line-length
    return this.http.get<ReasoningBranchDetails>(`${environment.serverApiUrl}/decision-trees/${decisionTreeId}/branches/${reasoningBranchId}`);
  }

  updateReasoningBranch(id: string, payload): Observable<any> {
    const decisionTreeId = this.parseId(id)[0];
    const reasoningBranchId = this.parseId(id)[1];
    Object.assign(payload, {
      branchIds: [reasoningBranchId]
    });

    return this.http.patch(`${environment.serverApiUrl}/decision-trees/${decisionTreeId}/branches`, payload);
  }

  private parseId(id: string): string[] {
    return id.split('-');
  }
}
