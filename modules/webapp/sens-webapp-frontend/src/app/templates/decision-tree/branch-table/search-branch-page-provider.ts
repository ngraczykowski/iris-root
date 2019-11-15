import { BranchPage } from '@app/templates/decision-tree/branch-table/branch-page';
import { BranchPageClient } from '@app/templates/decision-tree/branch-table/branch-page-client';
import { BranchPageProvider } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export interface ProviderConfig {
  decisionTreeId: number;
  query?: any;
}

export class SearchBranchPageProvider implements BranchPageProvider {

  constructor(private client: BranchPageClient, private config: ProviderConfig) { }

  getBranches(page: number, size: number): Observable<BranchPage> {
    return this.client.getBranchPage(page, size, this.config.decisionTreeId, this.config.query)
        .pipe(
            map(p => <BranchPage> {
              total: p.total,
              items: p.branches,
              model: p.branchModel
            })
        );
  }
}
