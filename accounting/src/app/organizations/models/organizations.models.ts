import {Role} from '../../roles/models/roles.models';

export interface Organization {
  id?: string
  name: string
}

export interface UserOrganization {
  id: string
  active: boolean
  organization: Organization
  role: Role
}
