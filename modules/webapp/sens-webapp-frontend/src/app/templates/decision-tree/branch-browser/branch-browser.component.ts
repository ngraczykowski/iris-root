import { Component, Input, OnInit } from '@angular/core';
import { BranchPageLoader } from '../branch-table/branch-page-loader';

@Component({
  selector: 'app-branch-browser',
  templateUrl: './branch-browser.component.html',
  styleUrls: ['./branch-browser.component.scss']
})
export class BranchBrowserComponent implements OnInit {

  @Input() decisionTreeId: number;

  loader: BranchPageLoader;

  constructor() { }

  ngOnInit() {
  }

  onLoaderChange(loader: BranchPageLoader) {
    this.loader = loader;
  }
}
