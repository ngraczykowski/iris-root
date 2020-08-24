import { animate, AnimationMetadata, style, transition, trigger } from '@angular/animations';

export class FitAnimation {

  static transition(): AnimationMetadata {
    return trigger('fitTransition', [
      transition('void <=> *', []),
      transition('* <=> *', [
        style({height: '{{startHeight}}px'}),
        animate('.2s ease')])
    ]);
  }
}
