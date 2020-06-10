import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class ReasoningBranchesReportService {

  constructor(
      private http: HttpClient
  ) { }

  getReport(decisionTreeID: number) {
    return this.http.get(
        `${environment.serverApiUrl}/report/reasoning-branch-report?decisionTreeId=${decisionTreeID}`,
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
