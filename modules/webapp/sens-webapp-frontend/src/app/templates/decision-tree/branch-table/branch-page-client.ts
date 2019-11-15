import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../environments/environment';
import { BranchPageResponse } from '../../model/branch.model';

@Injectable()
export class BranchPageClient {
  constructor(
      private http: HttpClient
  ) { }

  private static calculateOffset(page, size): any {
    return (page - 1) * size;
  }

  private static createBranchPageParams(offset, limit, decisionTreeId, query?) {
    const params = {
      offset: offset,
      limit: limit,
      decisionTreeId: decisionTreeId
    };
    if (query) {
      params['q'] = query;
    }
    return params;
  }

  getBranchPage(page, count, decisionTreeId, query?): Observable<BranchPageResponse> {
    const offset = BranchPageClient.calculateOffset(page, count);
    return this.http.get(environment.serverApiUrl + 'api/branches',
        {
          params: BranchPageClient.createBranchPageParams(offset, count, decisionTreeId, query)
        })
        .map((body) => new BranchPageResponse().deserialize(body));
  }
}
