import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Solution } from '@endpoint/configuration/model/solution.enum';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ConfigurationEndpointService {

  constructor(private http: HttpClient) { }

  public solutions(): Observable<Solution[]> {
    return this.http.get<Solution[]>(`${environment.serverApiUrl}/configuration/solutions`);
  }
}
