import {Component, OnInit} from '@angular/core';
import {BucketService} from "../../services/bucket.service";
import {ActivatedRoute, UrlSegment} from "@angular/router";
import {ResourceDTO, TeamDTO} from "../../models/models";
import {ResourceService} from "../../services/resource.service";
import {SYNC_TYPE, SyncService} from "../../services/sync.service";
import {TeamService} from "../../services/team.service";
import {UploadProgressModel} from "../../models/UploadProgressModel";
import {HttpEventType} from "@angular/common/http";
import {FolderDialogComponent} from "../../dialog/folder-dialog/folder-dialog.component";
import {MatDialog} from "@angular/material/dialog";
import {PreviewDialogComponent} from "../../dialog/preview-dialog/preview-dialog.component";
import {NgxSpinnerService} from "ngx-spinner";

class PathDescriptor{
  path: string;
  uniqueId: string;
}

@Component({
  selector: 'app-bucket-detail',
  templateUrl: './bucket-detail.component.html',
  styleUrls: ['./bucket-detail.component.scss']
})
export class BucketDetailComponent implements OnInit {

  public files: ResourceDTO;
  private originalResources: ResourceDTO;
  public team: string;
  public bucket: string;
  public teamDTO: TeamDTO;
  public urlparams: UrlSegment[] = [];
  public folderList: PathDescriptor[];

  constructor(public dialog: MatDialog,
              private teamService: TeamService,
              private bucketService: BucketService,
              private resourceService: ResourceService,
              private syncService: SyncService,
              private router: ActivatedRoute,
              private spinner: NgxSpinnerService) { }

  ngOnInit() {
    this.router.paramMap.subscribe(params=>{
      console.log("ROUTE PARAMS");
      this.team = params.get('team');
      this.bucket = params.get('bucket');
      this.loadResource();
      this.syncService.register().subscribe((type: SYNC_TYPE)=>{
        console.log("Sync", type);
        if(type == SYNC_TYPE.Resource){
          this.loadResource();
        }
      });
    });
    this.router.url.subscribe((data) => {
      console.log('params ', data); //contains all the segments so put logic here of determining what to do according to nesting depth
      this.urlparams = data;
      this.navigateFolder(this.originalResources);
    });
  }

  loadResource(){
    this.teamDTO = this.teamService.getTeamByUUID(this.team);
    this.resourceService.get(this.team, this.bucket).subscribe(data =>{
      this.originalResources = data;
      this.navigateFolder(data);
    });
  }

  private navigateFolder(data: ResourceDTO){
    this.folderList = [];
    if(this.urlparams && this.urlparams.length > 0 && data) {
      let find = data;
      for (let folder of this.urlparams) {
        find = find.childs.find(x=>{return x.uniqueKey == folder.path});
        if(find) {
          this.folderList.push({path: find.name, uniqueId: folder.path});
        }else{
          console.log("non ho trovato la folder "+folder, find);
        }
      }
      this.files = find;
    }else{
      this.files = data;
    }
  }

  deleteAttachment(index) {
  }

  uploadFileEvent(file: File){
    this.resourceService.addContent(this.team, this.bucket, this.urlparams.length>0?this.urlparams[this.urlparams.length-1].path:null, file).subscribe((data: UploadProgressModel)=>{
      console.log(data);
      if(data.status == HttpEventType.Response.toString()) {
        this.syncService.sendEvent(SYNC_TYPE.Resource);
      }else{
        //upload progress
      }
    });
  }

  download(file: ResourceDTO){
    this.resourceService.download(this.team, this.bucket, file.uniqueKey);
  }

  getPathForLink(index){
    return this.urlparams.slice(0,index+1).reduce((initial, item)=>{initial.push(item.path); return initial},[]).join('/');
  }

  getPreview(file: ResourceDTO){
      this.openDialogPreview(this.resourceService.previewDocuments(this.team, this.bucket, file.uniqueKey), file.name);
  }

  openDialogPreview(url: string, title: string): void{
    const dialogRef = this.dialog.open(PreviewDialogComponent, {
      width: '50vw',
      data: {url: url, title: title, ext: this.getExt(title)}
    });
  }

  private getExt(fileName : string){
      if(fileName != null){
        return fileName.split('.').pop();
      }
      return "";
  }


}
