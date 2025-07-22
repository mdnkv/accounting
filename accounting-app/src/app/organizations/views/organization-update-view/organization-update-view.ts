import {Component, inject, input, OnInit, signal} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router} from '@angular/router';

import {OrganizationService} from '../../services/organization';
import {Organization} from '../../models/organizations.models';

@Component({
  selector: 'app-organization-update-view',
  imports: [
    ReactiveFormsModule
  ],
  templateUrl: './organization-update-view.html',
  styleUrl: './organization-update-view.css'
})
export class OrganizationUpdateView implements OnInit{

  id = input.required<string>()
  organization: Organization | undefined = undefined
  isLoading = signal<boolean>(false)

  organizationService: OrganizationService = inject(OrganizationService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)

  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]]
  })

  ngOnInit() {
    this.organizationService.getOrganization(this.id()).subscribe({
      next: result => {
        this.organization = result
        this.loadFormData(result)
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
      }
    })
  }

  loadFormData(payload: Organization){
    this.form.get('name')?.setValue(payload.name)
  }

  formSubmit(){
    this.isLoading.set(true)
    const payload: Organization = {
      ...this.organization!,
      name: this.form.get('name')?.value
    }
    this.organizationService.updateOrganization(payload).subscribe({
      next: result => {
        this.isLoading.set(false)
        this.router.navigateByUrl('/organizations')
      },
      error: (err: HttpErrorResponse) => {
        console.log(err)
        this.isLoading.set(false)
      }
    })
  }

  goBack(){
    if (this.form.touched){
      if (confirm('Do you want to quit without saving?')){
        this.router.navigateByUrl('/organizations')
      }
    } else {
      this.router.navigateByUrl('/organizations')
    }
  }

}
