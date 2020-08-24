import { animate, AnimationMetadata, state, style, transition, trigger } from '@angular/animations';

export class ExpanseAnimations {
  static inOut(): AnimationMetadata {
    return trigger('expanseInOut', [
      transition(
          'void => *',
          [
            style({ opacity: 0, height: 0 }),
            animate('0.1s', style({ opacity: 0, height: '*' })),
            animate('0.1s', style({ opacity: 1, height: '*' }))
          ]
      ),
      transition(
          '* => void',
          [
            style({ opacity: 1, height: '*' }),
            animate('0.1s', style({ opacity: 0, height: '*' })),
            animate('0.1s', style({ opacity: 0, height: 0 }))
          ]
      )
    ]);
  }
  static passing(): AnimationMetadata {
    return trigger('expansePassing', [
      transition(
          'void => *',
          [
            style({ opacity: 0, height: 0 }),
            animate('0.2s', style({ opacity: 1, height: '*' }))
          ]
      ),
      transition(
          '* => void',
          [
            style({ opacity: 1, height: '*' }),
            animate('0.2s', style({ opacity: 0, height: 0 }))
          ]
      )
    ]);
  }
  static hide(): AnimationMetadata {
    return trigger('expanseHide', [
      state('visible', style({  opacity: 1, height: '*' })),
      state('hidden', style({  opacity: 0, height: 0, overflow: 'hidden' })),
      transition('void => hidden', []),
      transition('void => visible', []),
      transition('* => hidden', [
        style({ opacity: 1, height: '*' }),
        animate('0.2s ease-out', style({ opacity: 0, height: '*' })),
        animate('0.2s ease-in', style({ opacity: 0, height: 0 }))
      ]),
      transition('* => visible', [
        style({ opacity: 0, height: 0 }),
        animate('0.2s ease-out', style({ opacity: 0, height: '*' })),
        animate('0.2s ease-in', style({ opacity: 1, height: '*' }))
      ]),
    ]);
  }
}
