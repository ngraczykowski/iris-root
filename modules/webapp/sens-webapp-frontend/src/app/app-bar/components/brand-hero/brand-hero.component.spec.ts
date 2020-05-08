import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { BrandHeroComponent } from './brand-hero.component';

describe('BrandHeroComponent', () => {
  let component: BrandHeroComponent;
  let fixture: ComponentFixture<BrandHeroComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ BrandHeroComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrandHeroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
