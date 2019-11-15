import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AlertDetailsComponent } from './alert-details.component';
import { TestModule } from '@app/test/test.module';

describe('AlertDetailsComponent', () => {
  let component: AlertDetailsComponent;
  let fixture: ComponentFixture<AlertDetailsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ TestModule ],
      declarations: [ AlertDetailsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AlertDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
