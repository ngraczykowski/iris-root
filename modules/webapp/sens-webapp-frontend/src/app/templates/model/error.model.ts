export interface ServerError {
  error: ErrorDetails;
  status: number;
}

export interface ErrorDetails {
  date: Date;
  extras: { [key: string]: any };
  key: string;
}
