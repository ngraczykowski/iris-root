import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReportGenerationResponse } from '@app/users-report/models/report-generation-response';
import { environment } from '@env/environment';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(
    private http: HttpClient
  ) { }

  getReport(): Observable<ReportGenerationResponse> {
    return this.http.get(
      `${environment.serverApiUrl}/reports/users-report`,
      {
        responseType: 'text',
        observe: 'response'
      }
    ).pipe(
      map(response => ({
          file: response.body,
          filename: this.getFilename(response.headers.get('Content-Disposition'))
        })
      )
    );
  }

  getFilename(header): string {
    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
    return filenameRegex.exec(header)[1];
  }
}
