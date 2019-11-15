import { Component, Input, OnInit } from '@angular/core';
import { DynamicComponent } from '@app/components/dynamic-view/dynamic-view.component';

export interface BranchFeatureViewData {
  value: any;
}

@Component({
  selector: 'app-branch-feature-view',
  templateUrl: './branch-feature-view.component.html',
  styleUrls: ['./branch-feature-view.component.scss']
})
export class BranchFeatureViewComponent implements DynamicComponent, OnInit {

  @Input() data: BranchFeatureViewData;

  oneValue: boolean;
  manyValues: boolean;
  emptyValue: boolean;
  emptyState: boolean;

  emptyStatesList = [
    'AGENT_SKIPPED',
    'NO_DATA'
  ];

  constructor() { }

  ngOnInit() {
    this.oneValue = this.hasOneValue();
    this.manyValues = this.hasManyValues();
    this.emptyValue = this.isEmptyValue();
    this.emptyState = this.isEmptyState();
  }

  private hasOneValue() {
    return this.data && (this.isArray() ? this.data.value.length === 1 : this.data.value);
  }

  private hasManyValues() {
    return this.data && (this.isArray() && this.data.value.length > 1);
  }

  private isEmptyValue() {
    return !this.data || (this.isArray() ? this.data.value.length === 0 : !this.data.value);
  }

  private isArray() {
    return Array.isArray(this.data.value);
  }

  isEmptyState() {
    return this.data && this.emptyStatesList.includes(this.data.value);
  }
}
