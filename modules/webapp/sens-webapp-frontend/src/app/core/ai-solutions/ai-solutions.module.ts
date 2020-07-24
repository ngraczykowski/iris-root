import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AiSolutionsEffects } from '@core/ai-solutions/store/ai-solutions.effects';
import { aiSolutionsReducer } from '@core/ai-solutions/store/ai-solutions.reducer';
import { AI_SOLUTIONS_STATE_NAME } from '@core/ai-solutions/store/ai-solutions.state';
import { EndpointModule } from '@endpoint/endpoint.module';
import { EffectsModule } from '@ngrx/effects';
import { StoreModule } from '@ngrx/store';

@NgModule({
  declarations: [],
  imports: [
    CommonModule,
    EndpointModule,
    StoreModule.forFeature(AI_SOLUTIONS_STATE_NAME, aiSolutionsReducer),
    EffectsModule.forFeature([AiSolutionsEffects])
  ]
})
export class AiSolutionsModule { }
