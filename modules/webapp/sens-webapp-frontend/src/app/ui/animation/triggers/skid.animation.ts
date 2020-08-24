import { animate, AnimationMetadata, state, style, transition, trigger } from '@angular/animations';

export class SkidAnimation {

  static inOut(): AnimationMetadata {
    return trigger('skidInOut', [
      state('*', style({position: 'absolute'})),
      transition('void => visible', []),
      transition('void => *', [
          style({ opacity: 0, transform: 'translateX(60px)', position: 'absolute' }),
          animate('0.2s ease', style({ opacity: 1, transform: 'translateX(0)' }))
      ]),
      transition('* => void', [
          style({ opacity: 1, transform: 'translateX(0)' }),
          animate('0.2s ease', style({ opacity: 0, transform: 'translateX(60px)' }))
      ]),
    ]);
  }

  static hide(): AnimationMetadata {
    return trigger('skidHide', [
      state('visible', style({opacity: 1, marginLeft: 0})),
      state('hidden', style({opacity: 0, marginLeft: '60px', pointerEvents: 'none'})),
      transition('void => visible', []),
      transition('visible => hidden', [
        style({ opacity: 1, marginLeft: 0 }),
        animate('0.2s ease', style({ opacity: 0, marginLeft: '60px' }))
      ]),
      transition('hidden => visible', [
        style({ opacity: 0, marginLeft: '60px' }),
        animate('0.2s ease', style({ opacity: 1, marginLeft: 0 }))
      ]),
    ]);
  }
}
