import {Role} from '../../roles/models/roles.models';

export interface Organization {
  id?: string
  name: string
}

export interface UserOrganization {
  id: string
  organization: Organization
  role: Role
  active: boolean
}
