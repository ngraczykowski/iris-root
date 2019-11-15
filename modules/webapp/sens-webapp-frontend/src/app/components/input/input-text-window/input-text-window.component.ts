import { Component, ElementRef, EventEmitter, Input, Output, ViewChild } from '@angular/core';

@Component({
  selector: 'app-input-text-window',
  templateUrl: './input-text-window.component.html',
  styleUrls: ['./input-text-window.component.scss']
})
export class InputTextWindowComponent {

  @Input() placeholder: string;
  @Input() textAreaValue: string;
  @Input() listOfErrors: string[];

  @Output() outputValue = new EventEmitter();
  @Output() textAreaValueChange = new EventEmitter();

  constructor() { }

  @ViewChild('textArea', {static: false, read: ElementRef}) textArea: ElementRef;

  newValue(event) {
    this.outputValue.emit(event);
    this.textAreaValueChange.emit(event);
  }

  setHeight() {
    const textArea = this.textArea.nativeElement;
    const unit = 'px';

    textArea.style.height = 0 + unit;
    textArea.style.height = textArea.scrollHeight + unit;
  }

  isValid() {
    if (this.listOfErrors) {
      return this.listOfErrors.length > 0;
    }
  }
}
