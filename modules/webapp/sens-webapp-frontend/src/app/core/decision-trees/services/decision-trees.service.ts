import { Injectable } from '@angular/core';
import { DecisionTree } from '@core/decision-trees/model/decision-tree';
import { DecisionTreesEndpointService } from '@endpoint/decision-trees/services/decision-trees-endpoint.service';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DecisionTreesService {

  constructor(private decisionTreesEndpointService: DecisionTreesEndpointService) { }

  public getDecisionTree(decisionTreeId: any): Observable<DecisionTree> {
    return this.decisionTreesEndpointService.get(decisionTreeId);
  }
}
