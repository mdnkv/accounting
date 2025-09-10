import {Component, effect, inject, input, output} from '@angular/core';

import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatButtonModule} from '@angular/material/button';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';

import {Organization} from '../../models/organizations.models';
import {MatDialog} from '@angular/material/dialog';
import {CancelFormDialog} from '../../../core/components/cancel-form-dialog/cancel-form-dialog';

@Component({
  selector: 'app-organization-form',
  imports: [
    ReactiveFormsModule,
    MatButtonModule,
    MatFormFieldModule,
    MatInputModule
  ],
  templateUrl: './organization-form.html',
  styleUrl: './organization-form.css'
})
export class OrganizationForm {

  dialog: MatDialog = inject(MatDialog)
  cancel = output()

  formBuilder: FormBuilder = inject(FormBuilder)
  form: FormGroup = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(255)]]
  })

  saveOrganization = output<Organization>()
  currentOrganization = input<Organization>()

  constructor() {
    effect(() => {
      if (this.currentOrganization() != undefined){
        this.updateForm(this.currentOrganization()!)
      }
    });
  }

  updateForm (payload: Organization){
    this.form.get('name')?.setValue(payload.name)
  }

  onSubmit() {
    if (this.currentOrganization() != undefined){
      const payload: Organization = {
        ...this.currentOrganization()!,
        name: this.form.get('name')?.value
      }
      this.saveOrganization.emit(payload)
    } else {
      const payload: Organization = {
        name: this.form.get('name')?.value
      }
      this.saveOrganization.emit(payload)
    }
  }

  onCancel() {
    if (this.form.touched){
      // show cancel dialog
      let dialogRef = this.dialog.open(CancelFormDialog, {
        width: '400px'
      })
      dialogRef.afterClosed().subscribe(result => {
        // Close form
        if (result == true){
          this.cancel.emit()
        }
      })
    } else {
      // Form was not modified
      this.cancel.emit()
    }
  }

}
