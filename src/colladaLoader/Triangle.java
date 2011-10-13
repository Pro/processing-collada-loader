package colladaLoader;

import colladaLoader.Color;
import colladaLoader.Point2D;
import colladaLoader.Point3D;
import processing.core.PImage;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
public class Triangle
{
     /**
     * the 3 Vertices of the Triangle
     */
    public Point3D A,B,C;
    /**
     * Texture-Points
     */
    public Point2D texA,texB,texC;
    /**
     * the Filename of the texture-Image
     */
    public String imageFileName;
    /**
     * says if the Triangle has Texture
     */
    public boolean containsTexture = false;
    /**
     * an Objectreference of a loaded image by processing. Default is null.
     */
    public PImage imageReference;
    /**
     * the color of the Triangle if the Triangle has no texture
     */
     public Color colour;
}
