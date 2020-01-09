import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../environments/environment';
import { CollectionResponse } from '../../model/collection-response.model';
import { DecisionTree } from '../../model/decision-tree.model';

@Injectable()
export class DecisionTreeListClient {

  constructor(
      private http: HttpClient
  ) { }

  listDecisionTrees(): Observable<CollectionResponse<DecisionTree>> {
    return this.http.get<CollectionResponse<DecisionTree>>(environment.serverApiUrl + 'api/decision-trees');
  }
}
