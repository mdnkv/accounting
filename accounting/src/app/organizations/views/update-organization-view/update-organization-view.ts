import {Component, inject, input, OnInit, signal} from '@angular/core';
import {Organization} from '../../models/organizations.models';
import {OrganizationForm} from '../../components/organization-form/organization-form';
import {OrganizationService} from '../../services/organization';
import {Router} from '@angular/router';

@Component({
  selector: 'app-update-organization-view',
  imports: [
    OrganizationForm
  ],
  templateUrl: './update-organization-view.html',
  styleUrl: './update-organization-view.css'
})
export class UpdateOrganizationView implements OnInit{

  router: Router = inject(Router)
  organizationService: OrganizationService = inject(OrganizationService)

  id = input.required<string>()
  organization = signal<Organization|undefined>(undefined)

  ngOnInit() {
    // load organization
    this.organizationService.getOrganization(this.id()).subscribe({
      next: result => {
        this.organization.set(result)
      },
      error: err =>{
        console.log(err)
      }
    })
  }

  onUpdateOrganization(organization: Organization){
    this.organizationService.updateOrganization(organization).subscribe({
      next: result => {
        console.log(result)
        this.router.navigateByUrl('/organizations')
      },
      error: err =>{
        console.log(err)
      }
    })
  }

  onCancel(){
    this.router.navigateByUrl('/organizations')
  }

}
