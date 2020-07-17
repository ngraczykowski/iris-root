import { Component, EventEmitter, Output } from '@angular/core';

@Component({
  selector: 'app-configure-step',
  templateUrl: './configure-step.component.html'
})
export class ConfigureStepComponent {
  @Output() formSubmit = new EventEmitter();
  constructor() { }

  onFormSubmit() {
    this.formSubmit.emit();
  }
}
