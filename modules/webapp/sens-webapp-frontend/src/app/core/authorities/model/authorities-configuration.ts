import { Authority } from '@core/authorities/model/authority.enum';

export interface AuthoritiesConfiguration {
  features: {
    [key: string]: Authority[]
  };
}
