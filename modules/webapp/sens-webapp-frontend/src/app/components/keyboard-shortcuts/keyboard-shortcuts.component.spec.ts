import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../test/test.module';

import { KeyboardShortcutsComponent } from './keyboard-shortcuts.component';

describe('KeyboardShortcutsComponent', () => {
  let component: KeyboardShortcutsComponent;
  let fixture: ComponentFixture<KeyboardShortcutsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [TestModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(KeyboardShortcutsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
