import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-tag',
  templateUrl: './tag.component.html',
  styleUrls: ['./tag.component.scss']
})
export class TagComponent implements OnInit {

  @Input()
  set translatePrefix(translatePrefix: string) {
    this._translatePrefix = translatePrefix ? translatePrefix + '.' : '';
  }

  get translatePrefix() {
    return this._translatePrefix;
  }

  private _translatePrefix: string;

  @Input() value: string;

  constructor() { }

  ngOnInit() {
  }
}
