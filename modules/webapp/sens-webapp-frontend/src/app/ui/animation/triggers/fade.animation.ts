import { animate, AnimationMetadata, state, style, transition, trigger } from '@angular/animations';

export class FadeAnimation {

  static inOut(): AnimationMetadata {
    return trigger('fadeInOut', [
      transition(
          'void => *',
          [
            style({ opacity: 0 }),
            animate('0.2s', style({ opacity: '*' }))
          ]
      ),
      transition(
          '* => void',
          [
            style({ opacity: '*' }),
            animate('0.1s', style({ opacity: 0 }))
          ]
      )
    ]);
  }

  static hide(): AnimationMetadata {
    return trigger('fadeHide', [
      state('visible', style({ opacity: '*' })),
      state('hidden', style({ opacity: 0 })),
      transition('void => hidden', []),
      transition('void => visible', []),
      transition(
          'hidden => visible',
          [
            style({ opacity: 0 }),
            animate('0.2s', style({ opacity: '*' }))
          ]
      ),
      transition(
          'visible => hidden',
          [
            style({ opacity: '*' }),
            animate('0.1s', style({ opacity: 0 }))
          ]
      )
    ]);
  }
}
