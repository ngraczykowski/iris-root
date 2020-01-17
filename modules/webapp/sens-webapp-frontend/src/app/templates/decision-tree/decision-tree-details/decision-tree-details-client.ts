import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import 'rxjs/add/operator/map';
import { Observable } from 'rxjs/Observable';
import { environment } from '../../../../environments/environment';
import { DecisionTreeDetails } from '../../model/decision-tree.model';

@Injectable()
export class DecisionTreeDetailsClient {

  constructor(
      private http: HttpClient
  ) { }

  getDecisionTreeDetails(id): Observable<DecisionTreeDetails> {
    return this.http.get<DecisionTreeDetails>(environment.serverApiUrl + 'api/decision-trees/' + id);
  }
}
