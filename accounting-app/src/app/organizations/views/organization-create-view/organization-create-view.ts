import {Component, inject, signal} from '@angular/core';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterLink} from '@angular/router';

import {OrganizationService} from '../../services/organization';
import {Organization} from '../../models/organizations.models';

@Component({
  selector: 'app-organization-create-view',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './organization-create-view.html',
  styleUrl: './organization-create-view.css'
})
export class OrganizationCreateView {

  loading = signal(false)
  error = signal<string|null>(null)

  organizationService: OrganizationService = inject(OrganizationService)
  formBuilder: FormBuilder = inject(FormBuilder)
  router: Router = inject(Router)
  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]],
  })

  submit(){
    this.error.set(null)
    this.loading.set(true)

    // Create payload
    const payload: Organization = {
      name: this.form.get('name')?.value
    }

    // Execute request
    this.organizationService.createOrganization(payload).subscribe({
      next: result => {
        console.log(result)
        this.loading.set(false)
        this.router.navigateByUrl('/dashboard')
      },
      error: (err) => {
        console.log(err)
        this.loading.set(false)
        this.error.set('Cannot create the organization')
      }
    })
  }

}
