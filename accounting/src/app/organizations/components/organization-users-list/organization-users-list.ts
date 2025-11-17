import {Component, inject} from '@angular/core';
import {OrganizationUserStore} from '../../stores/organization-user.store';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';
import {OrganizationUserCard} from '../organization-user-card/organization-user-card';
import {OrganizationUser} from '../../models/organizations.models';

@Component({
  selector: 'app-organization-users-list',
  imports: [
    LoadingPlaceholder,
    EmptyListPlaceholder,
    OrganizationUserCard
  ],
  templateUrl: './organization-users-list.html',
  styleUrl: './organization-users-list.css'
})
export class OrganizationUsersList {

  readonly organizationUserStore = inject(OrganizationUserStore)

  onUpdateUser(payload: OrganizationUser){
    console.log(payload)
    this.organizationUserStore.updateOrganizationUser(payload)
  }

}
