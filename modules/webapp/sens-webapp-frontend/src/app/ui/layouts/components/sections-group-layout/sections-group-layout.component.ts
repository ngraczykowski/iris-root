import {
  ChangeDetectionStrategy,
  Component,
  HostBinding,
  ViewEncapsulation
} from '@angular/core';

@Component({
  selector: 'app-sections-group-layout',
  templateUrl: './sections-group-layout.component.html',
  styleUrls: ['./sections-group-layout.component.scss'],
  encapsulation: ViewEncapsulation.None,
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SectionsGroupLayoutComponent {
  @HostBinding('class.sections-group-layout') true;
}
