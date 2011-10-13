package colladaLoader.xmlMapping;
import processing.xml.*;
import java.util.HashMap;

import colladaLoader.xmlMapping.Library_Geometries;
import colladaLoader.xmlMapping.Library_Materials;
import colladaLoader.xmlMapping.Visual_Scene_sketchup8;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class maps the Library_Visual_scenes -tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Library_visual_scenes {

    private HashMap<String,Visual_Scene_sketchup8> library_visual_scenes = new HashMap<String, Visual_Scene_sketchup8>();

    Library_visual_scenes(XMLElement[] libScenes, Library_Materials libmat, Library_Geometries libGeo)
    {
        for (XMLElement aScene : libScenes)
        {
            Visual_Scene_sketchup8 s = new Visual_Scene_sketchup8(aScene, libmat, libGeo);
            library_visual_scenes.put(s.getID(), s);
        }
    }
/**
 *
 * @param id of the Visual_scenes-Tag
 * @return a mapped Visual_scenes-Object
 */
    Visual_Scene_sketchup8 getVisualScenesByID(String id)
    {
        return library_visual_scenes.get(id);
    }

}
