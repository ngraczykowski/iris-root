import { Component, EventEmitter, Inject, Input, Output } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-state',
  templateUrl: './state.component.html',
  styleUrls: ['./state.component.scss']
})
export class StateComponent {

  @Input() stateContent: StateContent;
  @Output() submit = new EventEmitter();

  constructor() { }

  onSubmitClick() {
    this.submit.emit();
  }
}
