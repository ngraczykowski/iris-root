import { AfterViewInit, Component, ElementRef, Input, OnDestroy, ViewChild } from '@angular/core';
import { Subscription } from 'rxjs';
import { BatchTypeManagementService } from '../../batch-type-management.service';

@Component({
  selector: 'app-batch-type-list-element',
  templateUrl: './batch-type-list-element.component.html',
  styleUrls: ['./batch-type-list-element.component.scss']
})
export class BatchTypeListElementComponent implements OnDestroy, AfterViewInit {
  @Input() batchType;
  @Input() index;
  @Input() type;
  @Input() batchTypeNameFilter = '';
  @ViewChild('checkBox', {static: true}) checkBox: ElementRef;
  selectedSubscription: Subscription;

  constructor(
    private batchManagementService: BatchTypeManagementService
  ) { }

  ngAfterViewInit() {
    this.selectedSubscription = this.batchManagementService.checkedElements.subscribe(() => this.updateElements());
  }

  ngOnDestroy() {
    this.selectedSubscription.unsubscribe();
  }

  canActivate() {
    return this.batchType.canActivate;
  }

  checkElement(element: HTMLInputElement) {
    if (element.checked) {
      this.batchManagementService.setCheckedElements(this.batchType);
    } else {
      this.batchManagementService.removeCheckedElement(this.batchType);
    }
  }

  updateElements() {
    this.checkBox.nativeElement.checked = this.batchManagementService.isChecked(this.batchType);
  }
}
