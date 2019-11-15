import { SelectableItemStore } from '@app/components/selectable-dynamic-table/selectable-item-store';
import { Branch, BranchModel } from '@app/templates/model/branch.model';

export class SelectedBranchStore extends SelectableItemStore<Branch> {

  private model: BranchModel;

  storeModel(model: BranchModel) {
    this.model = model;
  }

  getModel(): BranchModel {
    return this.model;
  }
}
