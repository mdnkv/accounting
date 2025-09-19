import {Component, inject} from '@angular/core';

import {MatDialogRef} from '@angular/material/dialog';

@Component({
  selector: 'app-cancel-form-dialog',
  imports: [],
  templateUrl: './cancel-form-dialog.html',
  styleUrl: './cancel-form-dialog.css'
})
export class CancelFormDialog {

  dialogRef:MatDialogRef<CancelFormDialog> = inject(MatDialogRef<CancelFormDialog>)

  onConfirm(){
    this.dialogRef.close(true)
  }

  onCancel() {
    this.dialogRef.close(false)
  }

}
