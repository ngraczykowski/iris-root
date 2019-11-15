import { TablePage } from '../../../components/pageable-dynamic-table/simple-table-data-provider';
import { Branch, BranchModel } from '../../model/branch.model';

export class BranchPage implements TablePage<Branch> {
  total: number;
  model: BranchModel;
  items: Branch[];
}
