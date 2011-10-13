package colladaLoader;

import colladaLoader.ColladaLoader;
import colladaLoader.Line;
import colladaLoader.LoadingHelper;
import colladaLoader.Triangle;
import processing.core.PApplet;
import processing.core.PConstants;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.1
 */
public class ColladaLoader
{
    private static ColladaLoader loader = null;
    private Triangle[] triangles;
    private Line[] lines ;
    private PApplet applet;

    /**
     * is a private constructor...please use getInstance()
     * @param the Filename (either 'foo.dae' or 'foo.kmz')
     * @param the main applet of processing (if you'r shure that your file contains no texture and you won't use the draw-methode then leave it null)
     */
    private ColladaLoader (String filename, PApplet applet)
    {
        this.applet = applet;
        LoadingHelper helper;
        if (applet == null) helper = new LoadingHelper(filename);
        else
            helper = new LoadingHelper(filename, applet);
        triangles = helper.getTriangles();
        lines = helper.getLines();
        helper = null;  //Activate GC to flush
    }


  /**
  * This Methode instantiates the Collada-Loader and prepares the triangles and lines for further use
  * @param the Filename (either 'foo.dae' or 'foo.kmz'
  * @param the main applet of processing (if you'r shure that your file contains no texture and you won't use the draw-methode then leave it null)
  * @return a ColladaLoader
  */
    public synchronized static ColladaLoader getInstance(String filename, PApplet applet)
    {
        if (loader == null) loader = new ColladaLoader(filename, applet);
        return loader;
    }

    public void draw()
    {
      applet.noStroke();

      applet.fill(127);

        for (Triangle tri : triangles)
        {

            if (!tri.containsTexture)
            {
                applet.fill(tri.colour.red*255, tri.colour.green*255, tri.colour.blue*255, tri.colour.transparency * 255);
                applet.beginShape(PConstants.TRIANGLES);

                applet.vertex(tri.A.x, tri.A.y, tri.A.z);
                applet.vertex(tri.B.x, tri.B.y, tri.B.z);
                applet.vertex(tri.C.x, tri.C.y, tri.C.z);

                applet.endShape();

            } else
            {
                applet.beginShape(PConstants.TRIANGLES);
                applet.texture(tri.imageReference);

                applet.vertex(tri.A.x, tri.A.y, tri.A.z, tri.texA.x, tri.texA.y);
                applet.vertex(tri.B.x, tri.B.y, tri.B.z, tri.texB.x, tri.texB.y);
                applet.vertex(tri.C.x, tri.C.y, tri.C.z, tri.texC.x, tri.texC.y);

                applet.endShape();

            }

        }

        for (Line lin : lines)
        {
            applet.stroke(lin.colour.red*255, lin.colour.green*255, lin.colour.blue*255);
            applet.strokeWeight(2);
            applet.line(lin.A.x, lin.A.y, lin.A.z, lin.B.x, lin.B.y, lin.B.z);

        }
    }
/**
 * makes the model smaller or bigger
 * @param factor &lt; 1.0f = smaller, factor &gt; 1.0f = bigger
 */
    public void scale(float factor)
    {
        for (Triangle tri : triangles)
        {
            tri.A.x *= factor; tri.A.y *= factor; tri.A.z *= factor;
            tri.B.x *= factor; tri.B.y *= factor; tri.B.z *= factor;
            tri.C.x *= factor; tri.C.y *= factor; tri.C.z *= factor;
        }

        for (Line lin : lines)
        {
            lin.A.x *= factor; lin.A.y *= factor; lin.A.z *= factor;
            lin.B.x *= factor; lin.B.y *= factor; lin.B.z *= factor;
        }

    }
    /**
     * shifts the model along the axis x,y or z
     * @param length in pixels
     * @param valid charcters for the axis are: 'x','y','z'
     */
    public void shift(float len, char axis)
    {
        for (Triangle tri : triangles)
        {
            if (axis == 'x') tri.A.x += len; if (axis == 'y') tri.A.y += len; if (axis == 'z') tri.A.z += len;
            if (axis == 'x') tri.B.x += len; if (axis == 'y') tri.B.y += len; if (axis == 'z') tri.B.z += len;
            if (axis == 'x') tri.C.x += len; if (axis == 'y') tri.C.y += len; if (axis == 'z') tri.C.z += len;
        }

        for (Line lin : lines)
        {
            if (axis == 'x') lin.A.x += len; if (axis == 'y') lin.A.y += len; if (axis == 'z') lin.A.z += len;
            if (axis == 'x') lin.B.x += len; if (axis == 'y') lin.B.y += len; if (axis == 'z') lin.B.z += len;

        }
    }
    /**
     * Rotates the Model in X,Y,or Z Axis
     * @param radiant from 0 to 2*PI
     * @param valid charcters for the axis are: 'x','y','z'
     */
    public void rotate(float radiant, char axis)
    {
        for (Triangle tri : triangles)
        {
            //Bürglers Matrices
            if (axis == 'z')
            {
                float Ax = (float)(Math.cos(radiant)*tri.A.x - Math.sin(radiant)*tri.A.y);
                float Ay = (float)(Math.sin(radiant)*tri.A.x + Math.cos(radiant)*tri.A.y);
                float Bx = (float)(Math.cos(radiant)*tri.B.x - Math.sin(radiant)*tri.B.y);
                float By = (float)(Math.sin(radiant)*tri.B.x + Math.cos(radiant)*tri.B.y);
                float Cx = (float)(Math.cos(radiant)*tri.C.x - Math.sin(radiant)*tri.C.y);
                float Cy = (float)(Math.sin(radiant)*tri.C.x + Math.cos(radiant)*tri.C.y);
                tri.A.x = Ax; tri.A.y = Ay; tri.B.x = Bx; tri.B.y = By; tri.C.x = Cx; tri.C.y = Cy;
            }

            if (axis == 'y')
            {
                float Ax = (float)(Math.cos(radiant)*tri.A.x + Math.sin(radiant)*tri.A.z); if (axis == 'z')tri.A.x = (float)(Math.cos(radiant)*tri.A.x - Math.sin(radiant)*tri.A.y);
                float Az = (float)(Math.cos(radiant)*tri.A.z - Math.sin(radiant)*tri.A.x); if (axis == 'z')tri.A.y = (float)(Math.sin(radiant)*tri.A.x + Math.cos(radiant)*tri.A.y);
                float Bx = (float)(Math.cos(radiant)*tri.B.x + Math.sin(radiant)*tri.B.z); if (axis == 'z')tri.B.x = (float)(Math.cos(radiant)*tri.B.x - Math.sin(radiant)*tri.B.y);
                float Bz = (float)(Math.cos(radiant)*tri.B.z - Math.sin(radiant)*tri.B.x); if (axis == 'z')tri.B.y = (float)(Math.sin(radiant)*tri.B.x + Math.cos(radiant)*tri.B.y);
                float Cx = (float)(Math.cos(radiant)*tri.C.x + Math.sin(radiant)*tri.C.z); if (axis == 'z')tri.C.x = (float)(Math.cos(radiant)*tri.C.x - Math.sin(radiant)*tri.C.y);
                float Cz = (float)(Math.cos(radiant)*tri.C.z - Math.sin(radiant)*tri.C.x); if (axis == 'z')tri.C.y = (float)(Math.sin(radiant)*tri.C.x + Math.cos(radiant)*tri.C.y);
                tri.A.x = Ax; tri.A.z = Az; tri.B.x = Bx; tri.B.z = Bz; tri.C.x = Cx; tri.C.z = Cz;
            }

            if (axis == 'x')
            {
                float Ay = (float)(Math.cos(radiant)*tri.A.y-Math.sin(radiant)*tri.A.z);
                float Az = (float)(Math.sin(radiant)*tri.A.y+Math.cos(radiant)*tri.A.z);
                float By = (float)(Math.cos(radiant)*tri.B.y-Math.sin(radiant)*tri.B.z);
                float Bz = (float)(Math.sin(radiant)*tri.B.y+Math.cos(radiant)*tri.B.z);
                float Cy = (float)(Math.cos(radiant)*tri.C.y-Math.sin(radiant)*tri.C.z);
                float Cz = (float)(Math.sin(radiant)*tri.C.y+Math.cos(radiant)*tri.C.z);
                tri.A.y = Ay; tri.A.z = Az; tri.B.y = By; tri.B.z = Bz; tri.C.y = Cy; tri.C.z = Cz;
            }


        }

        for (Line lin : lines)
        {
            if (axis == 'z')
            {
                float Ax = (float)(Math.cos(radiant)*lin.A.x - Math.sin(radiant)*lin.A.y);
                float Ay = (float)(Math.sin(radiant)*lin.A.x + Math.cos(radiant)*lin.A.y);
                float Bx = (float)(Math.cos(radiant)*lin.B.x - Math.sin(radiant)*lin.B.y);
                float By = (float)(Math.sin(radiant)*lin.B.x + Math.cos(radiant)*lin.B.y);
                lin.A.x = Ax; lin.A.y = Ay; lin.B.x = Bx; lin.B.y = By;
            }

            if (axis == 'y')
            {
                float Ax = (float)(Math.cos(radiant)*lin.A.x + Math.sin(radiant)*lin.A.z); if (axis == 'z')lin.A.x = (float)(Math.cos(radiant)*lin.A.x - Math.sin(radiant)*lin.A.y);
                float Az = (float)(Math.cos(radiant)*lin.A.z - Math.sin(radiant)*lin.A.x); if (axis == 'z')lin.A.y = (float)(Math.sin(radiant)*lin.A.x + Math.cos(radiant)*lin.A.y);
                float Bx = (float)(Math.cos(radiant)*lin.B.x + Math.sin(radiant)*lin.B.z); if (axis == 'z')lin.B.x = (float)(Math.cos(radiant)*lin.B.x - Math.sin(radiant)*lin.B.y);
                float Bz = (float)(Math.cos(radiant)*lin.B.z - Math.sin(radiant)*lin.B.x); if (axis == 'z')lin.B.y = (float)(Math.sin(radiant)*lin.B.x + Math.cos(radiant)*lin.B.y);
                lin.A.x = Ax; lin.A.z = Az; lin.B.x = Bx; lin.B.z = Bz;
            }

            if (axis == 'x')
            {
                float Ay = (float)(Math.cos(radiant)*lin.A.y-Math.sin(radiant)*lin.A.z);
                float Az = (float)(Math.sin(radiant)*lin.A.y+Math.cos(radiant)*lin.A.z);
                float By = (float)(Math.cos(radiant)*lin.B.y-Math.sin(radiant)*lin.B.z);
                float Bz = (float)(Math.sin(radiant)*lin.B.y+Math.cos(radiant)*lin.B.z);
                lin.A.y = Ay; lin.A.z = Az; lin.B.y = By; lin.B.z = Bz;
            }
        }


    }

    public Line[] getLines() {
        return lines;
    }

    public Triangle[] getTriangles() {
        return triangles;
    }



}
