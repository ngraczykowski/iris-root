import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

export interface SelectEvent {
  item: any;
  index: number;
}

@Component({
  selector: 'app-switch-view',
  templateUrl: './switch-view.component.html',
  styleUrls: ['./switch-view.component.scss']
})
export class SwitchViewComponent implements OnInit {

  @Input() defaultIndex = 0;
  @Input() translatePrefix = '';
  @Input() items: any[];

  @Output() select: EventEmitter<SelectEvent> = new EventEmitter<SelectEvent>();

  private activeIndex: number;
  private indexMapping = new Map<any, number>();

  constructor() {
  }

  ngOnInit() {
    this.createIndexMapping();
    this.switch(this.defaultIndex);
  }

  getName(item) {
    return this.translatePrefix + item;
  }

  isActive(item) {
    return this.activeIndex === this.indexMapping.get(item);
  }

  onClick(item) {
    this.switch(this.indexMapping.get(item));
  }

  private createIndexMapping() {
    if (this.items) {
      this.items.forEach((item, index) => this.indexMapping.set(item, index));
    }
  }

  private switch(index) {
    if (this.items && index < this.items.length) {
      this.activeIndex = index;
      this.select.emit({item: this.items[index], index: index});
    }
  }
}
