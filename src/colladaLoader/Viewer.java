package colladaLoader;
import colladaLoader.ColladaLoader;
import colladaLoader.Line;
import colladaLoader.Triangle;
import processing.core.*;

import peasy.*;

/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
public class Viewer extends PApplet
{

	private static final long serialVersionUID = 1L;
	Triangle[] triangles;
    Line[] lines;
    ColladaLoader model;
    PeasyCam cam;


    public void setup()
    {
      size(500, 500, P3D);
      //model = ColladaLoader.getInstance("testfile2.dae", this);
      //model = ColladaLoader.getInstance("susanOnWall.kmz", this);
      model = ColladaLoader.getInstance("/home/stefan/Dropbox/Coke2.kmz", this);
      triangles = model.getTriangles();
      lines = model.getLines();
      frameRate(10);
      cam = new PeasyCam(this, 0,0,0, 50);
   // values for zooming (min distance to which one can zoom in)
		cam.setMinimumDistance(1);
		cam.setMaximumDistance(500);
		
		cam.setRightDragHandler(cam.getPanDragHandler());
		cam.setLeftDragHandler(cam.getRotateDragHandler());
		cam.setDampingFactor(0.03);
		
		// initialize camera view parameters
		cam.setRotations(1.9074943, -0.6844337, 0.14366905);
    }


    public void draw(){
      background(16);

      lights();
      
//      model.rotate(angle, axis);
//      model.shift(lenX, 'x');
//      model.shift(lenY, 'y');
//      model.shift(lenZ, 'z');
//      lenY =0; lenX = 0; lenZ =0;

      model.draw();
      //drawModel();
      
      stroke(125, 0, 0);
      strokeWeight(1);
      line(0, 0, 0, width, 0, 0);
      stroke(0, 125, 0);
      line(0, 0, 0, 0, 0, -width);
      stroke(0, 0, 125);
      line(0, 0, 0, 0, -height, 0);


    }

    public void drawModel()
    {
      noStroke();

      fill(127);

        for (Triangle tri : triangles)
        {

            if (!tri.containsTexture)
            {
                fill(tri.colour.red*255, tri.colour.green*255, tri.colour.blue*255, tri.colour.transparency * 255);
                beginShape(TRIANGLES);

                vertex(tri.A.x, tri.A.y, tri.A.z);
                vertex(tri.B.x, tri.B.y, tri.B.z);
                vertex(tri.C.x, tri.C.y, tri.C.z);

                endShape();

            } else
            {
                beginShape(TRIANGLES);
                texture(tri.imageReference);

                vertex(tri.A.x, tri.A.y, tri.A.z, tri.texA.x, tri.texA.y);
                vertex(tri.B.x, tri.B.y, tri.B.z, tri.texB.x, tri.texB.y);
                vertex(tri.C.x, tri.C.y, tri.C.z, tri.texC.x, tri.texC.y);

                endShape();

            }

        }

        for (Line lin : lines)
        {
            stroke(lin.colour.red*255, lin.colour.green*255, lin.colour.blue*255);
            strokeWeight(1);
            line(lin.A.x, lin.A.y, lin.A.z, lin.B.x, lin.B.y, lin.B.z);

        }
    }



    public static void main(String[] args)
    {
        PApplet.main(new String[]{"colladaLoader.Viewer"});
    }

}



