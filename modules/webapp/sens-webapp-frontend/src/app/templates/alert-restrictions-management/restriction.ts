export interface Restriction {
  id: number;
  countries: string[];
  name: string;
  units: string[];
  userIds: string[];
}

export interface RestrictionsData {
  restrictions: Restriction[];
}
