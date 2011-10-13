package colladaLoader.xmlMapping;
import processing.xml.*;
import java.util.HashMap;

import colladaLoader.xmlMapping.Image;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class maps the Library_Images -tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Library_Images {

    private HashMap<String,Image> library_images = new HashMap<String, Image>();

    Library_Images(XMLElement[] images)
    {
        for (XMLElement img : images)
        {
            Image i = new Image(img);
            library_images.put(i.getID(), i);
        }

    }
/**
 *
 * @param id of the Image-Tag
 * @return a mapped Image
 */
    Image getImageByID(String id)
    {
        return library_images.get(id);
    }

}
