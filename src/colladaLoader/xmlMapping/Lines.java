package colladaLoader.xmlMapping;
import java.util.HashMap;

import colladaLoader.xmlMapping.Material;
import colladaLoader.xmlMapping.Source;
import colladaLoader.xmlMapping.SubTag;

import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p>helperclass for lines-tag  inside Geometry-tag (contains one or more Lines). It maps all points and their orders</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
   class Lines extends SubTag{
        private String materialSymbol;
        private Material material;
        private Source vertexSource; //pointmatrice
        private int offsetVertex = 8; //dummy defaultvalue
        private int[][] pointIndexMatrix;// format: [index][offset]
        private XMLElement lines;

        Lines(XMLElement lines, HashMap<String,SubTag> sources)
        {
            this.lines = lines;
            materialSymbol = lines.getString("material");

            //extract p-Tag to matrix
            XMLElement[] lines_inputs = lines.getChildren("input");
            int stride = lines_inputs.length;
            int count = Integer.parseInt(lines.getString("count"));
            String[] p_array = lines.getChild("p").getContent().split(" ");

            pointIndexMatrix = new int[count*2][stride];

            for (int i = 0, k = 0; i<count*2; i++)
            {
                for (int j = 0; j < stride; j++, k++)
                pointIndexMatrix[i][j] = Integer.parseInt(p_array[k]);
            }
            //analyze the lines-Tag, search for semantic VERTEX
            for (XMLElement lines_input : lines_inputs)
            {
                if (lines_input.getString("semantic").equals("VERTEX"))
                {
                    offsetVertex = Integer.parseInt(lines_input.getString("offset"));
                    vertexSource = (Source)sources.get(sources.get(lines_input.getString("source").substring(1)).getNextSource());
                }
            }

        }
    /**
     * invoke that methode after the Object is initiated. Because of cyclic depencies at runtime its not possible to set Material at initiating-time
     * @param instance of Material
     */
        void setMaterial(Material m)
        {
            material = m;
        }

        /**
         * @return the source of the vertices Position-Points (in 3D)
         */
        Source getVertexSource() {
            return vertexSource;
        }

        /**
         * @return the offset for vertice in the indexmatrix
         */
        int getOffsetVertex() {
            return offsetVertex;
        }


        /**
         * @return the pointIndexMatrix in the format: [index][offset]
         */
        int[][] getPointIndexMatrix() {
            return pointIndexMatrix;
        }

        /**
         * @return the material. It depends of the Runtime if it is null or not
         */
        Material getMaterial() {
            return material;
        }
        /**
         *
         * @return the Alias-Name of Material-ID
         */
        String getNextSource()
        {
            return materialSymbol;
        }
        /**
         * 
         * @return generates a Random-ID because the xml-Tag does'nt contain a defined ID
         */
        String getID()
        {
            return this.hashCode()+"_"+materialSymbol;
        }

        @Override
        public String toString()
        {
            String s = "";
            s+="Lines from Geometry "+lines.getParent().getParent().getString("id")+":\n";
            s+="uses Material symbol alias '"+getNextSource()+"'\n";
            s+= (material == null)? "material (still) not mapped \n": "uses material-ID"+material.getID()+"\n";
            s+= "has vertex-Points: \n";
            float[][] points = getVertexSource().getPointMatrix();
            int[][] indexes = getPointIndexMatrix();
            for (int i = 0, j = 0; j < indexes.length; i++ )
            {
                s+="Line_"+i+"\n";
                for (int k = 0; k < 2; k++, j++)
                {
                    int index = indexes[j][getOffsetVertex()];
                    s += "Point "+k+": x="+points[index][0]+",y="+points[index][1]+",z="+points[index][2]+"\n";

                }
            }

            return s;
        }
    }

