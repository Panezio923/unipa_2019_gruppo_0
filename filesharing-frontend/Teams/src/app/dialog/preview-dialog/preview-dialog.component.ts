import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialog} from "@angular/material/dialog";
import {NgxSpinnerService} from "ngx-spinner";

@Component({
  selector: 'app-preview-dialog',
  templateUrl: './preview-dialog.component.html',
  styleUrls: ['./preview-dialog.component.scss']
})

export class PreviewDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialog,
              @Inject(MAT_DIALOG_DATA) public data: any,
              private spinner: NgxSpinnerService){}

  ngOnInit() {
  }

  closePreview(){
    this.dialogRef.closeAll();
  }


}
