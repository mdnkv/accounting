import {Component, inject, OnInit} from '@angular/core';
import {Router} from '@angular/router';

import {OrganizationsList} from '../../components/organizations-list/organizations-list';
import {UserOrganizationStore} from '../../stores/user-organization.store';
import {Header} from '../../../core/components/header/header';

@Component({
  selector: 'app-organizations-view',
  imports: [
    OrganizationsList,
    Header
  ],
  templateUrl: './organizations-view.html',
  styleUrl: './organizations-view.css'
})
export class OrganizationsView implements OnInit {

  readonly store = inject(UserOrganizationStore)
  router: Router = inject(Router)

  ngOnInit() {
    this.store.getUserOrganizations()
  }

  onCreateClicked(){
    this.router.navigateByUrl('/organizations/create')
  }

}
