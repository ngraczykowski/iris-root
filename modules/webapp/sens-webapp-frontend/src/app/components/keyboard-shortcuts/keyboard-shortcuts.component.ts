import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-keyboard-shortcuts',
  templateUrl: './keyboard-shortcuts.component.html',
  styleUrls: ['./keyboard-shortcuts.component.scss']
})
export class KeyboardShortcutsComponent implements OnInit {

  @Input() shortcuts: string[];

  constructor() { }

  ngOnInit() {
  }

}
