import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable()
export class BranchReviewButtonService {

  constructor(private http: HttpClient) { }

  reviewBranch(decisionTreeId, matchGroupId): Observable<any> {
    const url = `${environment.serverApiUrl}api/decision-tree/${decisionTreeId}/branch/${matchGroupId}/review`;
    return this.http.post(url, {});
  }
}
