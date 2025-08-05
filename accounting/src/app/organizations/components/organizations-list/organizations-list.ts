import {Component, inject} from '@angular/core';
import {UserOrganizationStore} from '../../stores/user-organization.store';
import {OrganizationCard} from '../organization-card/organization-card';
import {LoadingPlaceholder} from '../../../core/components/loading-placeholder/loading-placeholder';
import {EmptyListPlaceholder} from '../../../core/components/empty-list-placeholder/empty-list-placeholder';

@Component({
  selector: 'app-organizations-list',
  imports: [
    OrganizationCard,
    LoadingPlaceholder,
    EmptyListPlaceholder
  ],
  templateUrl: './organizations-list.html',
  styleUrl: './organizations-list.css'
})
export class OrganizationsList {

  readonly store = inject(UserOrganizationStore)

}
