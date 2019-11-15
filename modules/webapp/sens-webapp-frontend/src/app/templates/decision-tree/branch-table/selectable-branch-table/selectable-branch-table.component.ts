import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {ProviderConfiguration} from '@app/components/selectable-dynamic-table/selectable-dynamic-table.component';
import {BranchPage} from '@app/templates/decision-tree/branch-table/branch-page';
import {SelectedBranchStore} from '@app/templates/decision-tree/branch-table/selectable-branch-table/selected-branch-store';
import {Branch, BranchModel} from '../../../model/branch.model';
import {BranchLoaderListener, BranchPageLoader} from '../branch-page-loader';
import {BranchPageMapper} from '../branch-page-mapper';
import {BranchDiscriminator} from '@app/templates/decision-tree/branch-change/branch-bulk-selector/branch-bulk-selector.component';

@Component({
  selector: 'app-selectable-branch-table',
  templateUrl: './selectable-branch-table.component.html',
  styleUrls: ['../branch-table.scss']
})
export class SelectableBranchTableComponent implements OnInit, OnDestroy, BranchLoaderListener {

  @Input()
  set loader(loader: BranchPageLoader) {
    this.updateLoader(loader);
    this.refresh();
  }

  private _loader: BranchPageLoader;

  @Input()
  set store(store: SelectedBranchStore) {
    this._store = store;
    this.refresh();
  }

  private _store: SelectedBranchStore;

  @Input()
  set discriminator(discriminator: BranchDiscriminator) {
    this._discriminator = discriminator;
    this.refresh();
  }

  private _discriminator: BranchDiscriminator;

  providerConfig: ProviderConfiguration<Branch>;
  private readonly mapper = new BranchPageMapper();

  constructor() { }

  ngOnInit(): void {
  }

  ngOnDestroy(): void {
    this.unregisterListener();
  }

  refresh() {
    this.providerConfig = {
      loader: this._loader,
      store: this._store,
      discriminator: this._discriminator,
      mapper: this.mapper
    };
  }

  onLoadSuccess(page: BranchPage) {
    this.storeModel(page.model);
  }

  private storeModel(model: BranchModel) {
    if (this._store) {
      this._store.storeModel(model);
    }
  }

  private updateLoader(loader: BranchPageLoader) {
    this.unregisterListener();
    this._loader = loader;
    this.registerListener();
  }

  private registerListener() {
    this._loader.registerListener(this);
  }

  private unregisterListener() {
    if (this._loader) {
      this._loader.unregisterListener(this);
    }
  }
}
