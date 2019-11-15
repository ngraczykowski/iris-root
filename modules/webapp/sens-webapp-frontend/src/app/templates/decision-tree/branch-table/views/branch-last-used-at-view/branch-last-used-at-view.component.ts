import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

export interface BranchLastUsedAtViewData {
  lastUsedAt: string;
}

@Component({
  selector: 'app-branch-last-used-at-view',
  templateUrl: './branch-last-used-at-view.component.html',
  styleUrls: ['./branch-last-used-at-view.component.scss']
})
export class BranchLastUsedAtViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchLastUsedAtViewData;

  constructor() { }

  ngOnInit() {
  }

  formatDate(timestamp): string {
    return new Date(timestamp).toISOString().substr(0, 10);
  }

}
