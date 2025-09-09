export interface Authority {
  id?: string
  organizationId?: string
  name: string
}

export interface Role {
  id?: string
  organizationId?: string
  name: string
  authorities: Authority[]
}
