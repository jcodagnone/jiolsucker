#CreateShortCut "${PROJECT_STARTMENU_FOLDER}\jiolsucker.lnk" \
#               "$INSTDIR\run.bat" "noarg" 0 0 SW_SHOWMINIMIZED

CreateShortCut "${PROJECT_STARTMENU_FOLDER}\jiolsucker.lnk" \
         "javaw" '-ea -jar "$INSTDIR\lib\${PROJECT_FINAL_NAME}.jar"' \
          0 0 SW_SHOWMINIMIZED

#CreateShortCut "${PROJECT_STARTMENU_FOLDER}\jiolnotifier.lnk" \

CreateShortCut "${PROJECT_STARTMENU_FOLDER}\Homepage.lnk" "${PROJECT_URL}"
CreateShortCut "${PROJECT_STARTMENU_FOLDER}\Uninstall.lnk" "$INSTDIR\Uninst.exe" "" "$INSTDIR\Uninst.exe" 0


