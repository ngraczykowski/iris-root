import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { BatchType } from '../batch-type-management.model';
import { BatchTypeManagementService } from '../batch-type-management.service';

@Component({
  selector: 'app-batch-type-list',
  templateUrl: './batch-type-list.component.html',
  styleUrls: ['./batch-type-list.component.scss']
})
export class BatchTypeListComponent implements OnInit {
  @Input() type;
  @Input() batchTypes: Observable<BatchType[]>;
  @Input() batchTypeNameFilter;
  @Input() selectedElementsPanel;

  showActiveBatches = true;

  constructor(
    private batchManagementService: BatchTypeManagementService
  ) { }

  ngOnInit() {
    this.batchManagementService.showUsedBatches.subscribe(val => this.showActiveBatches = val);
  }

  checkAll() {
    this.batchTypes.subscribe(data => {
      if (this.batchTypeNameFilter) {
        data.forEach(element => {
          if (element.name.toLowerCase().includes(this.batchTypeNameFilter.toLowerCase())) {
            this.batchManagementService.setCheckedElements(element);
          }
        });
      } else {
        data.forEach(batch => this.batchManagementService.setCheckedElements(batch));
      }
    }).unsubscribe();
  }

  uncheckAll() {
    this.batchTypes.subscribe(data => {
      data.forEach(element => this.batchManagementService.removeCheckedElement(element));
    }).unsubscribe();
  }

  checkBatchVisibility(batch: BatchType) {
    if (batch.canActivate) {
      return true;
    } else {
      return this.showActiveBatches ? true : false;
    }
  }

}
