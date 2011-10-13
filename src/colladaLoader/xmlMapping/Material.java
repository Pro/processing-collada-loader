package colladaLoader.xmlMapping;
import colladaLoader.xmlMapping.Effect;
import colladaLoader.xmlMapping.Library_Effects;
import processing.xml.*;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 *<p> this class maps the Material -tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Material {

    private String ID;
    private Effect effect;

    Material(XMLElement material, Library_Effects effects)
    {
        ID = material.getString("id");

        XMLElement instance_effect = material.getChild("instance_effect");
        String effectID = instance_effect.getString("url").substring(1);
        effect = effects.getEffectByID(effectID);
       
    }
/**
 *
 * @return the ID of the Material-tag
 */
    String getID()
    {
        return ID;
    }
/**
 *
 * @return the Effect that maps to the Material
 */
    Effect getEffect()
    {
        return effect;
    }

    public String toString()
    {
        return "Material ID '"+ID+"' with "+effect+" ";
    }
 
}
