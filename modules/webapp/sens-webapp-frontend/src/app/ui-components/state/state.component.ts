import { Component, Input } from '@angular/core';
import { StateContent } from '@app/ui-components/state/state';

@Component({
  selector: 'app-state',
  templateUrl: './state.component.html',
  styleUrls: ['./state.component.scss']
})
export class StateComponent {

  constructor() { }
  @Input() stateContent: StateContent;
}
