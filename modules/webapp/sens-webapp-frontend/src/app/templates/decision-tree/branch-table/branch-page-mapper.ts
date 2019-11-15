import { TableData } from '../../../components/dynamic-view-table/dynamic-view-table.component';
import { TablePageMapper } from '../../../components/pageable-dynamic-table/simple-table-data-provider';
import { Branch } from '../../model/branch.model';
import { BranchPage } from './branch-page';
import { BranchTableDataFactory } from './views/branch-table-views-factory';

export class BranchPageMapper implements TablePageMapper<Branch> {
  map(page: BranchPage): TableData {
    return BranchTableDataFactory.create(page.total, page.model, page.items);
  }
}
