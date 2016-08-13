Update: https://twitter.com/itba_ti/status/762730856565895168 IOL no utilza más sharepoint, armar una interface para la nueva plataforma implica implementar ciertas interfaces. Ver sharepoint/

Para empezar a romper el jiol es necesario tener maven, y un jdk 1.6 (o 6 :).

El proyecto se construye con maven2: http://maven.apache.org/

Para obtener los proyectos eclipse:
  $  mvn eclipse:eclipse
Luego en el eclipse:  File / Import / Existing Projects into workspace


Modulos:
  leak-commons    clases reusables por cualquier proyecto. algun dia se va a
                  graduar de jiol
  jiol-api        API que provee jiol para acceder a IO L
  jiol-api-impl   Implementación Mock (a ser renombrado a mock)
  jiol-iolsucker  Aplicación que usa jiol-api para bajar material didacticos
  sharepoint      Implementacion IOL2 que va contra Sharepoint.

Juan
