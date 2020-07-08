import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BranchFeaturesResponse } from '@app/reasoning-branches-browser/model/branch-features';
import { environment } from '@env/environment';
import { forkJoin, Observable, of } from 'rxjs';
import 'rxjs/add/observable/forkJoin';
import { catchError, map, timeout } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchDetailsService {
  constructor(
      private http: HttpClient
  ) { }

  getFeaturesNames(dtId, fvId): Observable<Array<BranchFeaturesResponse>> {
    return this.http.get<Array<BranchFeaturesResponse>>(`${environment.serverApiUrl}/reasoning-branches/features/${dtId}-${fvId}/names`);
  }

  getFeaturesValues(id): Observable<Array<BranchFeaturesResponse>> {
    return this.http.get<Array<BranchFeaturesResponse>>(`${environment.serverApiUrl}/reasoning-branches/features/${id}/values`);
  }

  getFeaturesList(dtId, fvId) {
    return forkJoin(
        {
          names: this.getFeaturesNames(dtId, fvId),
          values: this.getFeaturesValues(fvId)
        }
    ).pipe(
        map(data => {
          const namesList = data.names
              .map((name, index) => ({name: name, id: index + 1}));

          const valuesList = data.values
              .map((value, index) => ({value: value, id: index + 1}));

          const featuresList = (names, values) =>
              names.map(name => ({
                ...values.find((value) => (value.id === name.id) && value),
                ...name
              }));

          return featuresList(namesList, valuesList);
        }));
  }
}
