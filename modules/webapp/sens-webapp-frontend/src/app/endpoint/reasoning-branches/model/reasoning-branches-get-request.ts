import { HttpParams } from '@angular/common/http';

export interface ReasoningBranchesGetRequest extends HttpParams {
  aiSolution?: string;
  active?: boolean;
  pageIndex: number;
  pageSize: number;
}
