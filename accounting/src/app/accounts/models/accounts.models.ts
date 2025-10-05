export interface AccountCategory {
  id?: string
  organizationId: string
  name: string
  accountType: string
}

export interface Account {
  id?: string
  organizationId?: string
  name: string
  deprecated: boolean
  code: string
  accountType: string
  accountCategoryId?: string
  accountCategory?: AccountCategory | null
}
