export interface CollectionResponse<T> {
  total: number;
  results: T[];
}

export interface Page {
  content: any[];
  totalElements: number;
  last: boolean;
  first: boolean;
  size: number;
  number: number;
  numberOfElements: number;
}
