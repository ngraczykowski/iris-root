import {
  Component,
  ElementRef,
  EventEmitter,
  HostListener,
  Input,
  OnInit,
  Output
} from '@angular/core';

@Component({
  selector: 'app-dropdown-selector',
  templateUrl: './dropdown-selector.component.html',
  styleUrls: ['./dropdown-selector.component.scss']
})
export class DropdownSelectorComponent implements OnInit {

  show: boolean;

  @Input() translatePrefix = '';
  @Input() option;
  @Output() optionChange = new EventEmitter();
  @Input() possibleOptions;

  constructor(private eRef: ElementRef) { }

  ngOnInit() {
  }

  onDropdownClick() {
    this.show = !this.show;
  }

  onSelectOption(option: number) {
    this.show = false;
    this.option = option;
    this.optionChange.emit(this.option);
  }

  isOptionActive(option: number) {
    return this.option === option;
  }

  @HostListener('document:click', ['$event'])
  handleMouseClick(event) {
    const clickInsideComponent = this.eRef.nativeElement.contains(event.target);
    if (!clickInsideComponent) {
      this.show = false;
    }
  }
}
