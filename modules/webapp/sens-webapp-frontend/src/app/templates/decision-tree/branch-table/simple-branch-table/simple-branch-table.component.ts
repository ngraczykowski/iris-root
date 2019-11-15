import { Component, Input, OnInit } from '@angular/core';
import { TableDataProvider } from '../../../../components/pageable-dynamic-table/pageable-dynamic-table.component';
import { SimpleTableDataProvider } from '../../../../components/pageable-dynamic-table/simple-table-data-provider';
import { BranchPageLoader } from '../branch-page-loader';
import { BranchPageMapper } from '../branch-page-mapper';

@Component({
  selector: 'app-simple-branch-table',
  templateUrl: './simple-branch-table.component.html',
  styleUrls: ['../branch-table.scss']
})
export class SimpleBranchTableComponent implements OnInit {

  @Input()
  set loader(loader: BranchPageLoader) {
    this._loader = loader;
    this.reset();
  }

  private _loader: BranchPageLoader;
  private readonly mapper = new BranchPageMapper();

  provider: TableDataProvider;

  constructor() { }

  ngOnInit() {
  }

  reset() {
    this.provider = new SimpleTableDataProvider(this._loader, this.mapper);
  }
}
