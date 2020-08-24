import { AfterViewInit, ChangeDetectorRef, Directive, HostBinding, Optional } from '@angular/core';

@Directive({
  selector: '[fx]'
})
export class FxDirective implements AfterViewInit {

  @HostBinding('class.fx-inited')
  private fxInited: boolean;

  constructor(@Optional() private cdr: ChangeDetectorRef) {}

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.fxInited = true;
      if (this.cdr) {
        this.cdr.markForCheck();
      }
    });
  }

}
