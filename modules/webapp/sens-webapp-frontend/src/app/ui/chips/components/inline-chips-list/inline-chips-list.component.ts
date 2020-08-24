import { Overlay } from '@angular/cdk/overlay';
import {
  AfterContentInit,
  ChangeDetectionStrategy, ChangeDetectorRef,
  Component,
  ContentChildren,
  HostBinding,
  OnDestroy,
  QueryList,
  ViewContainerRef,
  ViewEncapsulation
} from '@angular/core';
import { MatChip } from '@angular/material';
import { LifecycleHelper } from '@app/shared/helpers/lifecycle-helper';
import { WindowService } from '@ui/layouts/services/window.service';
import { combineLatest, Observable } from 'rxjs';
import { startWith, takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-inline-chips-list',
  templateUrl: './inline-chips-list.component.html',
  styleUrls: ['./inline-chips-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush,
  encapsulation: ViewEncapsulation.None
})
export class InlineChipsListComponent implements AfterContentInit, OnDestroy {

  lifecycle: LifecycleHelper = new LifecycleHelper();

  values: string[] = [];

  wrappedChips: number;

  @HostBinding('class.inline-chips-list') true;

  @ContentChildren(MatChip) private chips: QueryList<MatChip>;

  hovered: boolean;

  constructor(private windowService: WindowService,
              private _viewContainerRef: ViewContainerRef,
              private cdr: ChangeDetectorRef) {}

  ngAfterContentInit(): void {
    const chipsChanges: Observable<MatChip[]> = this.chips.changes.pipe(
        startWith(this.chips.toArray()));

    chipsChanges.pipe(
        takeUntil(this.lifecycle.destroyed)
    ).subscribe((chips: MatChip[]) => {
      if (chips) {
        this.values = chips.map((matChip: MatChip) => matChip.value);
      } else {
        this.values = [];
      }
      this.cdr.markForCheck();
    });

    combineLatest([
        chipsChanges,
        this.windowService.getResize()
            .pipe(startWith(null))
    ]).pipe(takeUntil(this.lifecycle.destroyed)).subscribe(([chips, resize]) => {
      if (this.chips && this.chips.length && this.chips.first._elementRef.nativeElement.offsetTop
          !== this.chips.last._elementRef.nativeElement.offsetTop) {
        this.wrappedChips = this.countWrappedChips();
      } else {
        this.wrappedChips = 0;
      }
      this.cdr.markForCheck();
    });

  }

  setHovered(to: boolean): void {
    if (this.wrappedChips) {
      this.hovered = to;
    }
  }

  countWrappedChips(): number {
    const firstOffset: number = this.chips.first._elementRef.nativeElement.offsetTop;
    return this.chips.filter((chip: MatChip) =>
        chip._elementRef.nativeElement.offsetTop !== firstOffset).length;
  }

  ngOnDestroy(): void {
    this.lifecycle.destroy();
  }

}
