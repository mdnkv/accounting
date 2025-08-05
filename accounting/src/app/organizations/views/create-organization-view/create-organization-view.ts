import {Component, inject, signal} from '@angular/core';
import {Router} from '@angular/router';

import {Organization} from '../../models/organizations.models';
import {OrganizationForm} from '../../components/organization-form/organization-form';
import {OrganizationService} from '../../services/organization';
import {UserOrganizationStore} from '../../stores/user-organization.store';

@Component({
  selector: 'app-create-organization-view',
  imports: [
    OrganizationForm
  ],
  templateUrl: './create-organization-view.html',
  styleUrl: './create-organization-view.css'
})
export class CreateOrganizationView {

  readonly userOrganizationStore = inject(UserOrganizationStore)

  error = signal<string>('')

  organizationService: OrganizationService = inject(OrganizationService)
  router: Router = inject(Router)

  onCreateOrganization(organization: Organization){
    this.error.set('')
    this.organizationService.createOrganization(organization).subscribe({
      next: result => {
        console.log(result)
        const hasActiveOrganization = this.userOrganizationStore.activeOrganization() != undefined
        if (hasActiveOrganization){
          // go to a list of organizations
          this.router.navigateByUrl('/organizations')
        } else {
          // go to a dashboard
          this.router.navigateByUrl('/dashboard')
        }
      },
      error: (err) => {
        console.log(err)
      }
    })
  }

}
