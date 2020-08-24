import { animate, AnimationMetadata, state, style, transition, trigger } from '@angular/animations';

export class GrowAnimation {
  static inOut(): AnimationMetadata {
    return trigger('growInOut', [
      transition(
          'void => *',
          [
            style({ opacity: 0, transform: 'scale(0.8)' }),
            animate('0.2s', style({ opacity: '*', transform: 'scale(1)' }))
          ]
      ),
      transition(
          '* => void',
          [
            style({ opacity: '*', transform: 'scale(1)' }),
            animate('0.1s', style({ opacity: 0, transform: 'scale(0.8)' }))
          ]
      )
    ]);
  }

  static hide(): AnimationMetadata {
    return trigger('growHide', [
      state('visible', style({ opacity: '*', transform: 'scale(1)' })),
      state('hidden', style({ opacity: 0, transform: 'scale(0.8)' })),
      transition('void => hidden', []),
      transition('void => visible', []),
      transition(
          'hidden => visible',
          [
            style({ opacity: 0, transform: 'scale(0.8)' }),
            animate('0.2s', style({ opacity: '*', transform: 'scale(1)' }))
          ]
      ),
      transition(
          'visible => hidden',
          [
            style({ opacity: '*', transform: 'scale(1)' }),
            animate('0.1s', style({ opacity: 0, transform: 'scale(0.8)' }))
          ]
      )
    ]);
  }
}
