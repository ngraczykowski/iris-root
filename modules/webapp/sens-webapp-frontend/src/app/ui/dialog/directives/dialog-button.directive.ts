import { Directive, HostListener, Inject, Input, Optional } from '@angular/core';
import { DialogInstanceComponent } from '@ui/dialog/components/dialog-instance/dialog-instance.component';

@Directive({
  selector: '[appDialogButton]'
})
export class DialogButtonDirective {

  @Input() closeDialogOnClick: boolean = true;

  constructor(@Optional() @Inject(DialogInstanceComponent) private dialogInstance) {}

  @HostListener('click')
  click() {
    if (this.closeDialogOnClick && this.dialogInstance) {
      this.dialogInstance.close();
    }
  }
}
