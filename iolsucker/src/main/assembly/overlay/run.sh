#!/bin/bash
# autogenerado, no editar
cd `dirname $0`
LD_LIBRARY_PATH=`pwd`/lib:$LD_LIBRARY_PATH

$JAVA_HOME/bin/java -ea -jar lib/jiol-iolsucker-3.17.jar 
       
