import { ChangeDetectionStrategy, Component, Input } from '@angular/core';

@Component({
  selector: 'app-footnote',
  templateUrl: './footnote.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FootnoteComponent {
  @Input() public label: string;
}
