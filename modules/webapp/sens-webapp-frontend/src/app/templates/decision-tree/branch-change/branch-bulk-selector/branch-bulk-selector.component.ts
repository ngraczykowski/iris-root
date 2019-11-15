import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {SelectableItemDiscriminator} from '@app/components/selectable-dynamic-table/selectable-dynamic-table.component';
import {BranchPage} from '@app/templates/decision-tree/branch-table/branch-page';
import {SelectedBranchStore} from '@app/templates/decision-tree/branch-table/selectable-branch-table/selected-branch-store';
import {Branch} from '@app/templates/model/branch.model';
import {BranchLoaderListener, BranchPageLoader} from '../../branch-table/branch-page-loader';
import {BranchChangeStateService} from '../branch-change-state.service';
import {BranchSelectStateData} from '../branch-change.component';


export class BranchDiscriminator implements SelectableItemDiscriminator<Branch> {
  getId(item: Branch): any {
    return `${item.decisionTreeInfo.id}-${item.matchGroupId}`;
  }
}

@Component({
  selector: 'app-branch-bulk-selector',
  templateUrl: './branch-bulk-selector.component.html',
  styleUrls: ['./branch-bulk-selector.component.scss']
})
export class BranchBulkSelectorComponent implements OnInit, OnDestroy, BranchLoaderListener {

  @Input() data: BranchSelectStateData;

  loader: BranchPageLoader;

  store: SelectedBranchStore = new SelectedBranchStore();

  total: Number = 0;

  loading = false;

  showErrorSelectAll = false;

  private readonly discriminator = new BranchDiscriminator();

  constructor(private branchChangeStateService: BranchChangeStateService) {}

  ngOnInit() {
  }

  ngOnDestroy(): void {
    this.unregisterListener();
  }

  onLoaderChange(loader: BranchPageLoader) {
    this.unregisterListener();
    this.loader = loader;
    this.registerListener();
    this.store.clear();
  }

  private unregisterListener() {
    if (this.loader) {
      this.loader.unregisterListener(this);
    }
  }

  private registerListener() {
    this.loader.registerListener(this);
  }

  hasSelectedBranches() {
    return this.getSelectedBranchesCount() !== 0;
  }

  getSelectedBranchesCount() {
    return this.store.length();
  }

  onBranchSelected() {
    this.branchChangeStateService.setUpdateState({
      decisionTreeId: this.data.decisionTreeId,
      branchModel: this.store.getModel(),
      branches: this.store.getAll()
    });
  }

  onBranchResetSelected() {
    this.branchChangeStateService.setResetState({
      decisionTreeId: this.data.decisionTreeId,
      branchModel: this.store.getModel(),
      branches: this.store.getAll()
    });
  }

  onLoadSuccess(page: BranchPage) {
    this.total = page.total;
    this.showErrorSelectAll = false;
  }

  onLoadError(error) {
  }

  selectAllBranches() {
    this.loading = true;
    this.loader.loadWithLimit(this.total)
        .finally(() => this.loading = false)
        .subscribe(
        page => this.doSelectAll(page.items),
        error => this.onSelectAllError(error));
  }

  private onSelectAllError(error: any) {
    this.showErrorSelectAll = true;
    console.error(error);
  }

  private doSelectAll(branches: Branch[]) {
    this.store.clear();
    branches.forEach(branch => this.store.add(this.discriminator.getId(branch), branch));
  }

  areAllSelected() {
    return this.store.length() === this.total;
  }

  clearSelection() {
    this.store.clear();
    this.showErrorSelectAll = false;
  }
}
