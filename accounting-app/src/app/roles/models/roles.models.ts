import {Organization} from '../../organizations/models/organizations.models';

export interface Authority {
  id?: string
  name: string
  organizationId?: string
}

export interface Role {
  id?: string
  name: string
  organizationId?: string
  organization?: Organization
  authorities: Authority[]
}
