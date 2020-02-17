import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

@Component({
  selector: 'app-confirm-changes',
  templateUrl: './confirm-changes.component.html',
  styleUrls: ['./confirm-changes.component.scss']
})
export class ConfirmChangesComponent implements OnInit {

  @Input() show;
  @Input() branchId;

  @Output() closeModal = new EventEmitter();
  @Output() applyChanges = new EventEmitter();

  constructor() { }

  ngOnInit() {
  }

  confirm() {
    this.applyChanges.emit();
  }

  cancel() {
    this.closeModal.emit();
  }
}
