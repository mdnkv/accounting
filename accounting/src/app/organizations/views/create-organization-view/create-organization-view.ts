import {Component, inject, signal} from '@angular/core';
import {Router} from '@angular/router';

import {Organization} from '../../models/organizations.models';
import {OrganizationForm} from '../../components/organization-form/organization-form';
import {UserOrganizationStore} from '../../stores/user-organization.store';
import {ErrorMessage} from '../../../core/components/error-message/error-message';

@Component({
  selector: 'app-create-organization-view',
  imports: [
    OrganizationForm,
    ErrorMessage
  ],
  templateUrl: './create-organization-view.html',
  styleUrl: './create-organization-view.css'
})
export class CreateOrganizationView {

  readonly userOrganizationStore = inject(UserOrganizationStore)

  router: Router = inject(Router)

  onCreateOrganization(organization: Organization){
    this.userOrganizationStore.createOrganization(organization)
  }

  onCancel(){
    this.router.navigateByUrl('/organizations')
  }

}
