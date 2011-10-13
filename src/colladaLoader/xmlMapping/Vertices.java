package colladaLoader.xmlMapping;
import colladaLoader.xmlMapping.SubTag;
import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class is a Helperclass to map the vertice-tag inside geometry-tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Vertices extends SubTag{

    private String ID;
    private String nextSource;

    Vertices(XMLElement vertice)
    {

        //analyze the vertices-Tag, search for semantic POSITION (only that is interesting)
        for (XMLElement vertice_input : vertice.getChildren("input"))
            if (vertice_input.getString("semantic").equals("POSITION"))
            {
                ID = vertice.getString("id");
                nextSource = vertice_input.getString("source").substring(1);
            }

    }
 
    @Override
    String getNextSource() {
        return nextSource;
    }

    @Override
    String getID() {
        return ID;
    }


}
