import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { AlertPageResponse } from '../model/alert.model';
import { BranchDetails } from '../model/branch.model';

@Injectable()
export class ReasoningBranchService {

  constructor(private http: HttpClient) { }

  getBranchDetails(decisionTreeId, matchGroupId): Observable<BranchDetails> {
    return this.http.get(environment.serverApiUrl + 'api/decision-tree/' + decisionTreeId + '/branch/' + matchGroupId,
        {observe: 'response'})
        .map((res: HttpResponse<any>) => res.body)
        .map((body) => new BranchDetails().deserialize(body));
  }

  getAlertPage(page, count, filters?): Observable<AlertPageResponse> {
    const offset = this.calculateOffset(page, count);
    const limit = count;
    return this.http.get(environment.serverApiUrl + 'api/alerts',
        {
          observe: 'response',
          params: this.createAlertPageParams(offset, limit, filters)
        })
        .map((res: HttpResponse<any>) => res.body)
        .map((body) => new AlertPageResponse().deserialize(body));
  }

  private createAlertPageParams(offset, limit, filters?) {
    const params = {
      offset: offset,
      limit: limit
    };
    if (filters) {
      Object.keys(filters)
          .filter(k => filters[k] !== null && filters[k] !== undefined)
          .forEach(k => params[k] = filters[k]);
    }
    return params;
  }

  private calculateOffset(page, size): any {
    return (page - 1) * size;
  }
}
