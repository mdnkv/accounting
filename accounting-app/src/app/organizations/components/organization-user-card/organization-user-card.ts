import {Component, input} from '@angular/core';
import {OrganizationUser} from '../../models/organizations.models';

@Component({
  selector: 'app-organization-user-card',
  imports: [],
  templateUrl: './organization-user-card.html',
  styleUrl: './organization-user-card.css'
})
export class OrganizationUserCard {

  user = input.required<OrganizationUser>()

}
