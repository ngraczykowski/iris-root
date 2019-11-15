import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { TestModule } from '../../../test/test.module';
import { BranchBrowserComponent } from './branch-browser.component';
import { BranchBrowserModule } from './branch-browser.module';

describe('BranchBrowserComponent', () => {
  let component: BranchBrowserComponent;
  let fixture: ComponentFixture<BranchBrowserComponent>;

  beforeEach(async(() => {
    TestBed
        .configureTestingModule({
          imports: [
            TestModule,
            BranchBrowserModule
          ],
        })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BranchBrowserComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    fixture.detectChanges();

    expect(component).toBeTruthy();
  });
});
