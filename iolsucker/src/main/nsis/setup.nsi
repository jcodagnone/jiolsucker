

; Author Kristen Gillard
; Author Dion Gillard
; Author Arnaud Heritier
; Setup Script for NSIS
; http://maven.apache.org/maven-1.x/plugins/nsis/

; Do a Cyclic Redundancy Check to make sure the installer is not corrupt
CRCCheck on

; Compress options
SetCompress force
SetCompressor /SOLID lzma
SetDatablockOptimize on

; add directories for the source if it exists, and the plugin

; add project source onto the include list
!addincludedir "/home/zauber/juan/proyectos/zauber/jiol/iolsucker/src/main/nsis"
; Directory to search NSIS plugins
!addplugindir "/home/zauber/juan/proyectos/zauber/jiol/iolsucker/src/main/nsis"

; add generated files onto the include list
!addincludedir "/home/zauber/juan/proyectos/zauber/jiol/iolsucker/target"
; Directory to search NSIS plugins
!addplugindir "/home/zauber/juan/proyectos/zauber/jiol/iolsucker/target"
; add plugin supplied files onto the include list

; macro for the image on the install screen
!include "BrandingImage.nsh"

; macro to check if the jdk is installed
!include "JDK.nsh"

; Functions to set/remove environment variables
!include "Environment.nsh"

; include project specific details
!include "project.nsh"

; The name of the installer
Name "${PROJECT_NAME}"

; Maven Setup executable
OutFile "${PROJECT_DIST_DIR}\${PROJECT_FINAL_NAME}.exe"

; Adds an XP manifest to the installer
XPStyle on

; branding with logo
!ifdef PROJECT_LOGO
  AddBrandingImage left 
!endif

; Sets the font of the installer
SetFont "Arial" 8

!ifdef PROJECT_LICENSE_FILE
  ; License Information
  LicenseText "${PROJECT_LICENSE_TEXT}"
  LicenseData "${PROJECT_LICENSE_FILE}"
!endif

; The default installation directory
InstallDir "$PROGRAMFILES\${PROJECT_ORGANIZATION}\${PROJECT_NAME} ${PROJECT_VERSION}"
; Registry key to check for directory (so if you upgrade, it will 
; overwrite the old one automatically)
InstallDirRegKey HKLM "${PROJECT_REG_KEY}" "Install_Dir"

; The text to prompt the user to enter a directory
ComponentText "This will install ${PROJECT_NAME} on your computer."
; The text to prompt the user to enter a directory
DirText "${PROJECT_NAME} Home Directory"

; -------------------------------------------------------------- Package up the files to be installed
Section "${PROJECT_NAME} v${PROJECT_VERSION} Binaries"
  ; Set output path to the installation directory.
  SetOutPath $INSTDIR
  ; Put files and directories there
  ; the script will be run from a directory below Maven
  File /r "${PROJECT_DIST_BIN_DIR}\*.*"

  ; Write the installation path into the registry
  WriteRegStr HKLM "${PROJECT_REG_KEY}" "Install_Dir" "$INSTDIR"

  ; Write the environment variables to the Registry
  

  ; Write the uninstall keys for Windows
  WriteRegStr HKLM "${PROJECT_REG_UNINSTALL_KEY}" "DisplayName" "${PROJECT_NAME} ${PROJECT_VERSION} (remove only)"
  WriteRegStr HKLM "${PROJECT_REG_UNINSTALL_KEY}" "UninstallString" '"$INSTDIR\Uninst.exe"'
SectionEnd


; -------------------------------------------------------------- Create Shortcuts
Section "Create Start Menu Shortcut(s)"
  CreateDirectory "${PROJECT_STARTMENU_FOLDER}"
  !include "startmenu-shortcuts.nsh"
SectionEnd




; -------------------------------------------------------------- Section to Install the Uninstaller
Section "Install Uninstaller"
   ; write the uninstaller to the installation directory
   WriteUninstaller "$INSTDIR\Uninst.exe"
SectionEnd

; -------------------------------------------------------------- Uninstaller
Section "Uninstall"
  DetailPrint "Remove files"
  Delete $INSTDIR\*.*
  ; MUST REMOVE UNINSTALLER, too
  Delete $INSTDIR\Uninst.exe
  !include "remove-shortcuts.nsh"
  ; Recursively remove files and directories used
  ; this should also take care of the installer  
  RMDir /r "$INSTDIR"
  DetailPrint "Remove registry keys"
  DeleteRegKey HKLM "${PROJECT_REG_UNINSTALL_KEY}"
  DeleteRegKey HKLM "${PROJECT_REG_KEY}"
  
SectionEnd

; -------------------------------------------------------------- Add Images to the Installer / UnInstaller
Function .onGUIInit
   !ifdef PROJECT_LOGO
     !insertmacro BrandingImage "${PROJECT_LOGO}" ""
   !endif
   
FunctionEnd

; add the logo to the un-installer
Function un.onGUIInit
   !ifdef PROJECT_LOGO
     !insertmacro BrandingImage "${PROJECT_LOGO}" ""
   !endif
FunctionEnd

; -------------------------------------------------------------- End of File
