import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

import { Observable } from 'rxjs';
import { environment } from '../../../../../environments/environment';

@Injectable()
export class CopyDecisionTreeService {

  constructor(private http: HttpClient) {
  }

  copy(sourceId, targetName): Observable<any> {
    return this.http.post(
        environment.serverApiUrl + 'api/decision-tree/copy',
        {sourceId: sourceId, targetName: targetName},
        {observe: 'response'}
    );
  }
}
