import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable()
export class RemoveDecisionTreeService {

  constructor(private http: HttpClient) {
  }

  remove(id): Observable<any> {
    return this.http.delete(
        environment.serverApiUrl + 'api/decision-tree/' + id,
        {observe: 'response'});
  }
}
