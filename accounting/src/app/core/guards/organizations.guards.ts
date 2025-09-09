import {CanActivateFn, Router} from '@angular/router';
import {inject} from '@angular/core';

import {UserOrganizationStore} from '../../organizations/stores/user-organization.store';

export function HasActiveOrganizationGuard(): CanActivateFn {
  return () => {
    const router: Router = inject(Router)
    const userOrganizationStore = inject(UserOrganizationStore)
    if (userOrganizationStore.activeOrganization() != undefined){
      // Can continue
      return true
    } else {
      // Go to the "create organization" page
      router.navigateByUrl('/organizations/create')
      return false
    }
  }
}

export function HasAuthorityGuard(authorityName: string): CanActivateFn {
  return () => {
    const router: Router = inject(Router)
    const userOrganizationStore = inject(UserOrganizationStore)
    if (userOrganizationStore.activeOrganization() != undefined) {
      // Get authorities
      const authorities = userOrganizationStore.activeOrganization()!.role.authorities
      if (authorities.length == 0){
        // If no authorities at all
        router.navigateByUrl('/organizations')
        return false
      }
      // Find authority
      for (let i = 0; i < authorities.length; i++){
        // Check if the authorities' list contains the authority name
        if (authorities[i].name == authorityName){
          return true
        }
      }
      // Go to the "organizations" page
      router.navigateByUrl('/organizations')
      return false
    } else {
      router.navigateByUrl('/organizations/create')
      return false
    }
  }
}
