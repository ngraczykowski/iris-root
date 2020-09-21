import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ExternalApp } from '@endpoint/external-apps/model/external-app.enum';
import { environment } from '@env/environment';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ExternalAppsEndpointService {

  constructor(private http: HttpClient) { }

  public getLink(app: ExternalApp): Observable<string> {
    return of(`${environment.serverApiUrl}/apps/${app}`);
  }

}
