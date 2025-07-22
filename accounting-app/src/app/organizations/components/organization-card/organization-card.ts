import {Component, input, output, signal} from '@angular/core';
import {RouterLink} from '@angular/router';

import {UserOrganization} from '../../models/organizations.models';

@Component({
  selector: 'app-organization-card',
  imports: [RouterLink],
  templateUrl: './organization-card.html',
  styleUrl: './organization-card.css'
})
export class OrganizationCard {

  organization = input.required<UserOrganization>()
  setActive = output<string>()

  isActive = signal(false)

  toggleDropdown(){
    this.isActive.update(v => !v)
  }

  onSetActive(){
    this.setActive.emit(this.organization().id)
  }

}
