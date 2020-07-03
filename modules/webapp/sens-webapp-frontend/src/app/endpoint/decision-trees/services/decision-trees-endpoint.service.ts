import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DecisionTreesGetResponse } from '@app/endpoint/decision-trees/model/decision-trees-get-response';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';

@Injectable()
export class DecisionTreesEndpointService {

  constructor(private http: HttpClient) { }

  get(decisionTreeId: any): Observable<DecisionTreesGetResponse> {
    return this.http.get<DecisionTreesGetResponse>(`${environment.serverApiUrl}/decision-trees/${decisionTreeId}`);
  }
}
