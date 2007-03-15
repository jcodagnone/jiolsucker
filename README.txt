Última modificación: Tue Sep  6 15:37:00 ART 2005

Para empezar a romper el jiol es necesario tener maven, y un jdk 1.5 (o 5 :).

Tanto maven 1 como maven 2 pueden funcionar (el oficial es maven 1 por ahora)

Para obtener los proyectos eclipse:
  $  maven -Dgoal=eclipse multiproject:goal
o
  $  m2 eclipse:eclipse


Para seguir con las codings conventions asegurarse de seguir las instrucciones
de dev-environment/eclipse.txt para la configuración del eclipse.

Tambien asegurarse de usar checkstyle. La definición está en el archivo
z_checkstyle.xml

Para obtener un tarball para distribuir:
   $ maven dist
y fijarse en target/distributions/

Modulos:
  leak-commons    clases reusables por cualquier proyecto. algun dia se va a
                  graduar de jiol
  jiol-api        API que provee jiol para acceder a IO L
  jiol-api-impl   Implementación del api usando HTTP, e implementación Mock
  jiol-iolsucker  Aplicación que usa jiol-api para bajar material didacticos

Juan
