package colladaLoader.xmlMapping;
import java.util.HashMap;
import java.util.HashSet;

import colladaLoader.xmlMapping.Library_Materials;
import colladaLoader.xmlMapping.Lines;
import colladaLoader.xmlMapping.Material;
import colladaLoader.xmlMapping.Source;
import colladaLoader.xmlMapping.SubTag;
import colladaLoader.xmlMapping.Triangles;
import colladaLoader.xmlMapping.Vertices;

import processing.xml.*;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 *<p> this class maps the Geometry -tag</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Geometry {

    private String ID;
    private HashSet<Triangles> trianglesSet;
    private HashSet<Lines> linesSet;


    Geometry(XMLElement geometry)
    {
        ID = geometry.getString("id");

        XMLElement mesh = geometry.getChild("mesh");
 
        //dump the source-Tags
        HashMap<String,SubTag> sources = new HashMap<String, SubTag>();
        for (XMLElement src : mesh.getChildren("source"))
        {
            Source s = new Source(src);
            sources.put(s.getID(), s);
        }
        //dump the vertice-tag to the sources
        Vertices v = new Vertices(mesh.getChild("vertices"));
        sources.put(v.getID(), v);
 
        XMLElement[] shapes;

        //dump triangles
        if ((shapes = mesh.getChildren("triangles")).length !=0)
        {
            trianglesSet = new HashSet<Triangles>();
            for (XMLElement triangles : shapes)
            {
                trianglesSet.add(new Triangles(triangles, sources));
            }

        }
        //dump lines
        if ((shapes = mesh.getChildren("lines")).length !=0)
        {
            linesSet = new HashSet<Lines>();
            for (XMLElement lines : shapes)
            {
                linesSet.add(new Lines(lines, sources));
            }

        }
      
    }
    /**
     * sets all to the Lines and Triangles its materials
     * @param matlib
     * @param bindmap in the format Hashmap&lt;materialSymbol, materialID&gt;
     */
    void bindMaterials(Library_Materials matlib, HashMap<String,String> bindmap)
    {
        if (trianglesSet != null)
        {
            for (Triangles triangles : trianglesSet) {
                String matSymbol = triangles.getNextSource();
                String matID = bindmap.get(matSymbol);
                Material m = matlib.getMaterialByID(matID);
                triangles.setMaterial(m);
            }
        }
        if (linesSet != null)
        {
                for (Lines lines : linesSet) {
                String matSymbol = lines.getNextSource();
                String matID = bindmap.get(matSymbol);
                Material m = matlib.getMaterialByID(matID);
                lines.setMaterial(m);
            }
        }

    }

/**
 *
 * @return the ID of the Geometry-Tag
 */
    String getID()
    {
        return ID;
    }
    /**
     * returns if any triangles found in xml-File, null if not
     * @return
     */
    HashSet<Triangles> getTriangles()
    {
        return trianglesSet;
    }
    /**
     * returns if any lines found in xml-File, null if not
     * @return
     */
    HashSet<Lines> getLines()
    {
        return linesSet;
    }


    @Override
    public String toString() {
        
        String s = "";
        s+= "Geometry "+ID+" contains: \n";
        if (trianglesSet != null)
            for (Triangles t : trianglesSet)
                s+= "a set of "+t+"\n";
        if (linesSet != null)
            for (Lines t : linesSet)
                s+= "a set of "+t+"\n";
        return s;
        
    }
  
}
