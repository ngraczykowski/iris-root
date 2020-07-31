import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReasoningBranchesListResponse } from '@app/reasoning-branches-browser/model/branches-list';
import { ReasoningBranchesGetRequest } from '@endpoint/reasoning-branches/model/reasoning-branches-get-request';
import { ReasoningBranchesGetResponse } from '@endpoint/reasoning-branches/model/reasoning-branches-get-response';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchesEndpointService {

  constructor(private http: HttpClient) { }

  get(params: ReasoningBranchesGetRequest): Observable<ReasoningBranchesGetResponse> {
    return this.http.get<ReasoningBranchesListResponse>(`${environment.serverApiUrl}/reasoning-branches`, { params });
  }
}
