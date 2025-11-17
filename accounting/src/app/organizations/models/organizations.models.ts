import {Role} from '../../roles/models/roles.models';
import {User} from '../../users/models/users.models';

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

export interface OrganizationUser {
  id: string
  organizationId: string
  roleId: string
  user: User
  role: Role
}

export interface CreateOrganizationUserRequest {
  email: string
  organizationId?: string
  roleId: string
}
