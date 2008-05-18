; macro for the image on the install screen
!macro BrandingImage IMAGE PARMS
  Push $0
  GetTempFileName $0
  File /oname=$0 "${IMAGE}"
  SetBrandingImage ${PARMS} $0
  Delete $0
  Pop $0
!macroend