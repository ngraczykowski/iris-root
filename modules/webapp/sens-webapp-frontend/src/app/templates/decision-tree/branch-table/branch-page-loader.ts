import {TablePageLoader} from '@app/components/pageable-dynamic-table/simple-table-data-provider';
import {Observable, throwError} from 'rxjs';
import {catchError, tap} from 'rxjs/operators';
import {Branch} from '../../model/branch.model';
import {BranchPage} from './branch-page';

export interface BranchLoaderListener {
  onLoadSuccess?(page: BranchPage);

  onLoadError?(error);
}

export interface BranchPageProvider {

  getBranches(page: number, size: number): Observable<BranchPage>;
}

export class BranchPageLoader implements TablePageLoader<Branch> {

  private listeners: BranchLoaderListener[] = [];

  constructor(private provider: BranchPageProvider) { }

  load(page: number, size: number): Observable<BranchPage> {
    return this.provider.getBranches(page, size)
        .pipe(
            tap(p => this.onLoadSuccess(p)),
            catchError(error => {
              this.onLoadError(error);
              return throwError(error);
            })
        );
  }

  registerListener(listener: BranchLoaderListener) {
    this.listeners.push(listener);
  }

  unregisterListener(listener: BranchLoaderListener) {
    const index = this.listeners.indexOf(listener);
    if (index > -1) {
      this.listeners.splice(index, 1);
    }
  }

  private onLoadError(error) {
    this.listeners
        .filter(l => l.onLoadError)
        .forEach(l => l.onLoadError(error));
  }

  private onLoadSuccess(p) {
    this.listeners
        .filter(l => l.onLoadSuccess)
        .forEach(l => l.onLoadSuccess(p));
  }

  loadWithLimit(size): Observable<BranchPage> {
    return this.provider.getBranches(1, size);
  }
}
