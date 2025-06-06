import React from 'react';
import { getCookie } from './../Helpers.js';
const RightClickFileManage = (props) => {
  const { dataName, dataType, fullOwner, dataPermission, currentPath, dataI, dataGroup } = props;


  /** Implement this:
       <RightClickFileManage
        dataName={this.state.dataName}
        dataType={this.state.dataType}
        fullOwner={this.state.fullOwner}
        dataPermission={this.state.dataPermission}
        currentPath={this.state.currentPath}
        dataI={this.state.dataI}
        dataGroup={this.state.dataGroup}

        extractArchive={this.extractArchive} // Pass the method here
        editFile={this.editFile} // Pass the method here
        rename={this.rename} // Pass the method here
        copyFullPath={this.copyFullPath} // Pass the method here
        emptyFile={this.emptyFile} // Pass the method here
        removeItems={this.removeItems} // Pass the method here
        checkFolderSize={this.checkFolderSize} // Pass the method here
        changePermission={this.changePermission} // Pass the method here
        addToArchive={this.addToArchive} // Pass the method here
        copyOrMove={this.copyOrMove} // Pass the method here
        changeOwner={this.changeOwner} // Pass the method here
        showPermissionDialog={this.showPermissionDialog} // Pass the method here

      />
  */

const extractArchive = () => {
        // Code for extracting archive
    };

    const editFile = () => {
        // Code for editing file
    };

    const rename = () => {
        // Code for renaming
    };

    const download = () => {
        // Code for downloading
    };

    const copyFullPath = () => {
        // Code for copying full path
    };

    const emptyFile = () => {
        // Code for emptying file content
    };

    const removeItems = () => {
        // Code for removing items
    };

    const checkFolderSize = () => {
        // Code for checking folder size
    };

    const showPermissionDialog = () => {
        // Code for showing permission dialog
    };

    const changePermission = () => {
        // Code for changing permission
    };

    const changeOwner = () => {
        // Code for changing owner
    };

    const addToArchive = () => {
        // Code for adding to archive
    };

    const copyOrMove = () => {
        // Code for copying or moving
    };

  return (
    <>
        {(dataName.includes(".zip") || dataName.includes(".tar")|| dataName.includes(".gz"))&& dataType=="2" ?
        <li><a class="dropdown-item" href="#" onClick={e=>extractArchive(e,dataName)}>
             <i class="bi bi-file-zip"></i> Extract</a></li>
        :<></>
        }
        {(dataType=="2" &&
        (!dataName.includes(".zip") && !dataName.includes(".tar") && !dataName.includes(".gz")) &&
        (!dataName.includes(".gif") && !dataName.toLowerCase().includes(".jpg") && !dataName.toLowerCase().includes(".jpeg")
        && !dataName.toLowerCase().includes(".png"))
        ) ?
        <li><a class="dropdown-item" href="#" onClick={e=>editFile(e,dataName,
                                                                       fullOwner,
                                                                       dataPermission)}>
          <i class="bi bi-pencil-square"></i> Edit</a></li>
        :<></> }

        <li>
        <a class="dropdown-item" href="#" onClick={e=>rename(e, dataName,dataI)}>
         <i class="bi bi-align-center"></i> Rename
        </a>
        </li>

        {dataType=="2" ?
        <li><a class="dropdown-item"
          href={window.API_URL+"download-file?pathToFile="+currentPath +"/"+dataName+
                                             "&fileName="+dataName+"&id="+localStorage.getItem("id")+
                                             "&tok="+getCookie("tokenauth")}>
        <i class="bi bi-download"></i> Download</a></li>:<></> }
        <li><a class="dropdown-item" href="#" onClick={e=>copyFullPath(e, dataName)}>
                          <i class="bi bi-clipboard-check"></i> Copy full path</a></li>

        {dataType=="2" ?
        <li><a class="dropdown-item" href="#" onClick={e=>emptyFile(e, dataName)}>
          <i class="bi bi-calendar-x"></i> Empty file content</a></li> :<></> }

        <li>
        <a class="dropdown-item" href="#" style={{color:"red"}}
                   onClick={e=>removeItems(e, currentPath +"/"+dataName )}>
          <i class="bi bi-trash3"></i> Delete
        </a>
        </li>

        {dataType=="1" ?
         <>
         <li><a class="dropdown-item" href="#"  onClick={e=> checkFolderSize(e, dataName )}>
                                     <i class="bi bi-info"></i> Check Size</a></li>
          <li><a class="dropdown-item" href="#"
                            onClick={e=> showPermissionDialog(e, dataName, dataPermission)}>
                                        <i class="bi bi-key"></i> Change Permission</a></li>
         </>:<>
            <li><a class="dropdown-item" href="#"
                      onClick={e=>changePermission(e, dataName, dataPermission)}>
              <i class="bi bi-key"></i> Change Permission</a></li>
           </> }
        <li>
        <a class="dropdown-item" href="#" onClick={e=>changeOwner(e, dataName, dataGroup)}>
           <i class="bi bi-people"></i> Change Owner</a>
        </li>

        <li><a class="dropdown-item" href="#" onClick={e=>addToArchive(e, dataName)}>
          <i class="bi bi-file-earmark-zip"></i> Add to zip</a></li>
        <li> <a  class="dropdown-item" href="#" onClick={e=>copyOrMove(e, dataName,"copy")}><i class="bi bi-clipboard-check"></i> Copy</a> </li>
        <li> <a  class="dropdown-item" href="#" onClick={e=>copyOrMove(e, dataName,"move")}><i class="bi bi-arrows-move"></i> Cut</a> </li>

    </>
  );
};

export default RightClickFileManage;