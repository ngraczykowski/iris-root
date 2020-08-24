import { animate, AnimationBuilder, style } from '@angular/animations';
import { UniqueSelectionDispatcher } from '@angular/cdk/collections';
import {
  AfterViewInit,
  ChangeDetectorRef,
  Directive,
  ElementRef, HostBinding,
  OnDestroy
} from '@angular/core';
@Directive({
  selector: '[appExpanseAnimation]',
})
export class ExpanseAnimationDirective implements AfterViewInit, OnDestroy {

  @HostBinding('style.overflow') overflow: string = 'hidden';

  constructor(private builder: AnimationBuilder, cdr: ChangeDetectorRef, private el: ElementRef,
              protected _expansionDispatcher: UniqueSelectionDispatcher) {}

  ngAfterViewInit(): void {
    const factory = this.builder.build([
        style({opacity: 0}),
        animate(200, style({opacity: 1}))
    ]);
    const player = factory.create(this.el.nativeElement);
    player.play();
  }

  ngOnDestroy(): void {
    const factory = this.builder.build([
      style({opacity: 1}),
      animate(200, style({opacity: 0}))
    ]);
    const player = factory.create(this.el.nativeElement);
    player.play();
  }

}
