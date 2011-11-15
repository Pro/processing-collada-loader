package colladaLoader.xmlMapping;
import java.util.HashMap;

import colladaLoader.xmlMapping.Image;
import colladaLoader.xmlMapping.Library_Images;
import colladaLoader.xmlMapping.NewParam;

import processing.xml.*;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 * <p>this class maps the effect-tag</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
class Effect {

    private String ID;
    private Image image;
    private float[] color;
    private boolean hasTexture = false;

    //add support for your chosen effect here
    Effect(XMLElement effect, Library_Images images)
    {
        ID = effect.getString("id");
        XMLElement diffuse = null;

        //Get the effect type. Common examples are lambert and Phong
        String effectType = effect.getChild("profile_COMMON/technique").getChild(0).getName().toString();
         
        if(effectType.equals("lambert"))    {        	 
        	 //get lambert -> diffuse if possible
	        diffuse = effect.getChild("profile_COMMON/technique/lambert/diffuse");	         
	         //if lambert -> diffuse does not exist then take the transparency value as the diffuse value	        
	        
	        if (diffuse == null) //not sure about this constant/transparent...havent seen this in collada file before but leaving it in case 
	        	diffuse = effect.getChild("profile_COMMON/technique/constant/transparent");
	        
	        if (diffuse == null) //in case the transparent above doesnt catch the transparent value when a diffuse is not present 
	        	diffuse = effect.getChild("profile_COMMON/technique/lambert/transparent");
        }
        
        if(effectType.equals("phong"))	{
        	//get Phong -> diffuse if possible
	        diffuse = effect.getChild("profile_COMMON/technique/phong/diffuse");	
	        
	         //if Phong -> diffuse does not exist then take the transparency value as the diffuse value	        
	        if (diffuse == null)
	        	diffuse = effect.getChild("profile_COMMON/technique/phong/transparent");
        }
        
         //give user error message to tell them where to look to add support for their shader listed in the collada file
        if( diffuse == null)
        	System.out.print("Unable to find diffuse value. Try adding support for your chosen effect in the Effect.java"); 
        
         //if diffuse is null an error will be thrown here
        if (diffuse.getChildren()[0].getName().equals("texture"))
        {
            hasTexture = true;
            //read Paramtags
            HashMap<String, NewParam> params = new HashMap<String, NewParam>();
            XMLElement[] newParams = effect.getChildren("profile_COMMON/newparam");
            for (XMLElement e: newParams)
            {
                NewParam p = new NewParam(e);
                params.put(p.getID(), p);
            }
            //set Imagevariable
            String imageID = params.get(params.get(diffuse.getChild("texture").getString("texture")).getNextSource()).getNextSource();
            image = images.getImageByID(imageID);
        }
        if (diffuse.getChildren()[0].getName().equals("color"))
        {
            hasTexture = false;
            String[] color = diffuse.getChild("color").getContent().split(" ");
            Float.parseFloat(color[0]);
            this.color = new float[]{Float.parseFloat(color[0]),Float.parseFloat(color[1]),Float.parseFloat(color[2]),Float.parseFloat(color[3])};
        }
    }
/**
 * 
 * @return the ID of the Effect-Tag
 */
    String getID()
    {
        return ID;
    }
    /**
     * gives the answer if the effect contains a color or an image
     * @return false on color, true on image
     */
    boolean hasTexture()
    {
        return hasTexture;
    }
/**
 * returns the image of the texture
 * @return the image
 */
    Image getImage()
    {
        return image;
    }
    /**
     * returns an Array in the Format [red,green,blue,transparent]
     * @return the color
     */
    float[] getColor()
    {
        return color;
    }

    @Override
    public String toString()
    {
        String s = "";
        s+= "Effect ID '"+ID+"' contains"+((hasTexture)?" an Image with "+image:"Colors: Red = "+color[0]+" green = "+color[1]+" blue = "+color[2]+" transparence = "+color[3]);
        return s;
    }

}
