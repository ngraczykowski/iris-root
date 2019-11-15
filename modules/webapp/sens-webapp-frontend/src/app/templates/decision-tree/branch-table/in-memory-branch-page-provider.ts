import { BranchPage } from '@app/templates/decision-tree/branch-table/branch-page';
import { BranchPageProvider } from '@app/templates/decision-tree/branch-table/branch-page-loader';
import { Branch, BranchModel } from '@app/templates/model/branch.model';
import { Observable, of } from 'rxjs';

export class InMemoryBranchPageProvider implements BranchPageProvider {

  constructor(private branches: Branch[], private model: BranchModel) {}

  private static calculateOffset(page: number, size: number) {
    return (page - 1) * size;
  }

  getBranches(page: number, size: number): Observable<BranchPage> {
    const offset = InMemoryBranchPageProvider.calculateOffset(page, size);
    return of(<BranchPage> {
      total: this.branches.length,
      model: this.model,
      items: this.branches.slice(offset, offset + size)
    });
  }
}
