import {
  ChangeDetectionStrategy,
  Component, ElementRef,
  Inject, ViewChild,
} from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-dialog-template',
  templateUrl: './dialog-template.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DialogTemplateComponent {

  @ViewChild('buttonPanel', {static: true}) private buttonPanel: ElementRef;

  constructor(@Inject(MAT_DIALOG_DATA) public data: any ) {}

}
