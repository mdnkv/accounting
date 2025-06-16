import {Organization} from '../../organizations/models/organizations.models';

export interface Role {
  id: string
  active: boolean
  organization: Organization
}
