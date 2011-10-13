package colladaLoader.xmlMapping;
import processing.xml.*;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 *<p> this class maps the Image -tag</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Image {
    
    private String bild;
    private String ID;

    Image(XMLElement image)
    {
        ID = image.getString("id");
        bild = image.getChild("init_from").getContent();

   }
/**
 * the ID of the Image-tag
 * @return
 */
    String getID()
    {
        return ID;
    }
    /**
     *
     * @return the Filename of the picture
     */
    String getPicture()
    {
        return bild;
    }
    @Override
    public String toString()
    {
        return "Image-Filename '"+bild+"'";
    }

}
