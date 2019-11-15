import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { CollectionResponse } from '@model/collection-response.model';
import { Model } from '@model/decision-tree.model';
import { Observable } from 'rxjs';
import { DecisionTreeSettings } from './create-new-decision-tree.model';

@Injectable()
export class CreateNewDecisionTreeService {

  constructor(
      private http: HttpClient
  ) { }

  getAvailableModels(): Observable<Model[]> {
    return this.http.get(environment.serverApiUrl + 'api/models',
        {
          observe: 'response'
        })
        .map((res: HttpResponse<any>) => <CollectionResponse<Model>> res.body)
        .map(response => response.results);
  }

  createNewDecisionTree(settings: DecisionTreeSettings): Observable<any> {
    return this.http.post(
        environment.serverApiUrl + 'api/decision-tree',
        settings,
        {observe: 'response'}
    );
  }
}
