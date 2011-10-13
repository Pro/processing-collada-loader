package colladaLoader.xmlMapping;
import colladaLoader.xmlMapping.SubTag;
import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class is a helper-class to map the newparam-tag inside effect-tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
 class NewParam extends SubTag{

        private String nextSource;
        private String ID;

       NewParam(XMLElement newParam)
        {
            ID = newParam.getString("sid");

            XMLElement initfrom = newParam.getChild("surface/init_from");
            if (initfrom != null) nextSource = initfrom.getContent();

            XMLElement src = newParam.getChild("sampler2D/source");
            if (src != null) nextSource = src.getContent();

        }
        /**
         * @return the next Information Tag
         */
        String getNextSource() {
            return nextSource;
        }

        /**
         * @return the ID
         */
        String getID() {
            return ID;
        }

    }
