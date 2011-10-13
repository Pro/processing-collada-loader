package colladaLoader.xmlMapping;
import colladaLoader.xmlMapping.SubTag;
import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class is a helper-class to map the source-tag inside the Geometry-tag </p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Source extends SubTag{

        private float[][] pointmatrix; // format: [index][x,y,z or x,y]
        private String ID;

        Source(XMLElement source)
        {
            ID = source.getString("id");
            XMLElement accessor = source.getChild("technique_common/accessor");
            String[] float_array = source.getChild("float_array").getContent().split(" ");
            int accessor_count = Integer.parseInt(accessor.getString("count"));
            int accessor_stride = Integer.parseInt(accessor.getString("stride"));

            pointmatrix = new float[accessor_count][accessor_stride];

            for (int i = 0, k = 0; i<accessor_count; i++)
            {
                for (int j = 0; j < accessor_stride; j++, k++)
                pointmatrix[i][j] = Float.parseFloat(float_array[k]);
            }


        }

        /**
         *
         * @return returns position-Points in the format format: [index][x,y,z or x,y]
         */
        float[][] getPointMatrix()
        {
            return pointmatrix;
        }

        /**
         * @return the ID
         */
        String getID() {
            return ID;
        }

        String getNextSource()
        {
            return ID;
        }
    }
