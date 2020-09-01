import { ChangeDetectionStrategy, Component, HostBinding, ViewEncapsulation } from '@angular/core';

@Component({
  selector: 'app-section-layout',
  templateUrl: './section-layout.component.html',
  styleUrls: ['./section-layout.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SectionLayoutComponent {
  @HostBinding('class.section-layout') true;
}
