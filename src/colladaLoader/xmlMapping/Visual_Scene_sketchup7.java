package colladaLoader.xmlMapping;
import java.util.HashMap;

import colladaLoader.xmlMapping.Geometry;
import colladaLoader.xmlMapping.Library_Geometries;
import colladaLoader.xmlMapping.Library_Materials;
import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p>this class does the same job as class 'Visual_Scene_sketchup8' for Sketchup version 7 but <u>is unused</u></p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Visual_Scene_sketchup7 {

    private String ID;


    Visual_Scene_sketchup7(XMLElement aScene, Library_Materials lib_materials, Library_Geometries lib_geometries)
    {
        ID = aScene.getString("id");

        for (XMLElement modelAndViews : aScene.getChildren("node")) {
            for (XMLElement groupNode : modelAndViews.getChildren("node")){
                for (XMLElement meshNode : groupNode.getChildren("node")){

                    XMLElement instance_geometry = meshNode.getChild("instance_geometry");

                    String geometryID = instance_geometry.getString("url").substring(1);
                    HashMap<String,String> materialBinds = new HashMap<String, String>(); //Format: <Symbol,MatID>

                    //scan all Materialbinds that belongs to the geometry
                    for (XMLElement instance_material : instance_geometry.getChild("bind_material/technique_common").getChildren("instance_material"))
                    {
                        String materialSymbol = instance_material.getString("symbol");
                        String materialID = instance_material.getString("target").substring(1);
                        materialBinds.put(materialSymbol, materialID);
                    }
                    //get the geometry-obj and start the material-binds to its triangles and lines
                    Geometry geometry = lib_geometries.getGeometryByID(geometryID);
                    geometry.bindMaterials(lib_materials, materialBinds);
                    //System.out.println(geometry);  //useful on debugging

                }

            }
        }
    }
    /**
     * 
     * @return the ID of this scene
     */
    String getID()
    {
        return ID;
    }
}
