import {
  Component,
  ElementRef,
  HostBinding,
  ViewChild
} from '@angular/core';
import { FitAnimation } from '@ui/animation/triggers/fit.animation';

@Component({
  selector: 'app-dynamic-height',
  templateUrl: './dynamic-height.component.html',
  styleUrls: ['./dynamic-height.component.scss'],
  animations: [FitAnimation.transition()]
})
export class DynamicHeightComponent {

  @ViewChild('wrapper', {static: true}) private wrapper: ElementRef;

  startHeight: number = 0;

  private i: number = 0;
  @HostBinding('@fitTransition') get transition() {
    return {value: '' + this.i++, params: {startHeight: this.startHeight}};
  }

  changed(): void {
    this.startHeight = this.wrapper.nativeElement.clientHeight;
  }

}
