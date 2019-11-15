import { Directive, ElementRef } from '@angular/core';

@Directive({
  selector: '[appNotImplemented]'
})
export class NotImplementedDirective {

  constructor(el: ElementRef) {
      el.nativeElement.remove();
  }
}
