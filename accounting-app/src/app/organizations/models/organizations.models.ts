import {Role} from '../../roles/models/roles.models';
import {User} from '../../users/models/users.models';

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

export interface CreateOrganizationUserRequest {
  organizationId: string
  roleId: string
  email: string
}

export interface OrganizationUser {
  id: string
  organizationId: string
  roleId: string
  role: Role
  user: User
}
