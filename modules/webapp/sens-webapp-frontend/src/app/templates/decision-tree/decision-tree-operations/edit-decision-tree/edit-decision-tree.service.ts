import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../../environments/environment';
import { DecisionTreeSettings } from './edit-decision-tree.component';

@Injectable()
export class EditDecisionTreeService {

  constructor(
      private http: HttpClient
  ) { }

  patchDecisionTree(id, settings: DecisionTreeSettings): Observable<any> {
    return this.http.patch(
        environment.serverApiUrl + 'api/decision-tree/' + id,
        settings,
        {observe: 'response'}
    );
  }
}
