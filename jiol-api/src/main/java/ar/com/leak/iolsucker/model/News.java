/*
 * Creada el Apr 26, 2005
 */
package ar.com.leak.iolsucker.model;


/**
 * Representa una noticia de IOL
 * 
 * @author Juan F. Codagnone
 * @since Apr 26, 2005
 */
public interface News {
    /**
     * @return el título de la noticia
     */
    String getTitle();
    
    /**
     * @return el contenido de la noticia
     */
    String getBody();
    
    /**
     * marca como leida a la noticia
     *
     */
    void markAsReaded();
}
