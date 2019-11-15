import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { Filter } from './saved-filters/saved-filters.component';

@Injectable()
export class BranchFilterClient {

  constructor(
      private http: HttpClient
  ) { }

  getFilters(): Observable<Filter[]> {
    return this.http.get(`${environment.serverApiUrl}api/filters`)
        .pipe(map(body => body as Filter[]));
  }

  saveFilter(name: string, query: string): Observable<any> {
    return this.http
        .post(`${environment.serverApiUrl}api/filters`, { name: name, query: query });
  }

  deleteFilter(id: number) {
    return this.http.delete(`${environment.serverApiUrl}api/filters/${id}`);
  }
}
