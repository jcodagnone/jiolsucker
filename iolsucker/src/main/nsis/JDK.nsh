Function AssertJavaHome
   ClearErrors
   ReadEnvStr $0 "JAVA_HOME"

   IfErrors 0 Found
      ClearErrors
      MessageBox MB_OK|MB_ICONSTOP "The JAVA_HOME environment variable must be set. Please set JAVA_HOME to the location of your JDK and try installing again"
      Abort

   Found:
FunctionEnd
