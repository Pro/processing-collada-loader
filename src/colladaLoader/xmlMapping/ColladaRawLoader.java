package colladaLoader.xmlMapping;
import colladaLoader.ColladaLoaderException;
import colladaLoader.Color;
import colladaLoader.Line;
import colladaLoader.Point2D;
import colladaLoader.Point3D;
import colladaLoader.Triangle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.*;

import colladaLoader.xmlMapping.Geometry;
import colladaLoader.xmlMapping.Library_Geometries;
import colladaLoader.xmlMapping.Lines;
import colladaLoader.xmlMapping.Material;
import colladaLoader.xmlMapping.Triangles;

import processing.xml.*;
import processing.core.PApplet;


/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 * 
 * <p>this class starts the "xml to object-Mapping" and converts it to human handable Shapes.</p>
 * 
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0
 */
public class ColladaRawLoader extends PApplet{


	private static final long serialVersionUID = 1L;
	private Triangle[] triangles;
    private Line[] lines ;
    private String authoring_tool = "unknown";
    private int[] authoring_tool_version = new int[]{0};

    public ColladaRawLoader(String file) throws ColladaLoaderException
    {
       super();

      try {
       //map xml-tags to objects
       Library_Geometries lib_geo = startXmlMapping(file);

       //Mapping finished. Now make them human readable
        assembleTriangles(lib_geo.getGeometries());
       }catch (RuntimeException e) {
           throw new ColladaLoaderException("Couldn't parse COLLADA file. Maybe the authoring tool " + authoring_tool + " isn't supported or the xml format is invalid.", e);
       }
    }
    /**
     * @param args the command line arguments
     */
    private Library_Geometries startXmlMapping(String filename) {
        
        XMLElement root = new XMLElement(this, filename);

        authoring_tool = root.getChild("asset/contributor/authoring_tool").getContent();
        
        LinkedList<String> numbers = new LinkedList<String>();
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(authoring_tool);
        while (m.find()) {
        	numbers.add(m.group());
        }
        authoring_tool_version = new int[numbers.size()];
        for (int i=0; i<numbers.size(); i++)
        {
        	authoring_tool_version[i] = Integer.parseInt(numbers.get(i));
        }
        

        //build images
        XMLElement[] images = root.getChildren("library_images/image");
        Library_Images lib_img = new Library_Images(images);

        //build effects
        XMLElement[] effects = root.getChildren("library_effects/effect");
        Library_Effects lib_eff = new Library_Effects(effects,lib_img);

        //build materials
        XMLElement[] materials = root.getChildren("library_materials/material");
        Library_Materials lib_mat = new Library_Materials(materials,lib_eff);

        //build geometries
        XMLElement[] geometries = root.getChildren("library_geometries/geometry");
        Library_Geometries lib_geo = new Library_Geometries(geometries);

        //material-Mapping
        XMLElement[] scenes = root.getChildren("library_visual_scenes/visual_scene");
        @SuppressWarnings("unused")
		Library_visual_scenes lib_scenes = new Library_visual_scenes(scenes, lib_mat, lib_geo);

        //System.out.println(lib_mat); //useful on debugging

        return lib_geo;
     
    }
    public Triangle[] getTriangles()
    {
        return triangles;
    }

    public Line[] getLines()
    {
        return lines;
    }
    /**
     * returns how the file was made with
     * @return
     */
    public String getAuthoringTool()
    {
        return authoring_tool;
    }
    

    /**
     * returns how the file was made with
     * @return
     */
    public int[] getAuthoringToolVersion()
    {
        return authoring_tool_version;
    }

    /**
     * converts the triangles geometries human readable
     * @param library of geometries
     */
    private void assembleTriangles(Collection<Geometry> geometries)
    {
        ArrayList<Triangle> triangles = new ArrayList<Triangle>();
        ArrayList<Line> lines = new ArrayList<Line>();

        for (Geometry geo : geometries)
        {
            //iterate the triangles-sets in geometry
            if (geo.getTriangles() != null)
            {
                //one triangle-set
                for (Triangles t : geo.getTriangles())
                {
                    Material mat = t.getMaterial();

                    //commons for the set of triangles
                    float[][] vertPoints = t.getVertexSource().getPointMatrix();
                    int[][] indexes = t.getPointIndexMatrix();
                    int vertexOffset = t.getOffsetVertex();
                    float[][] texturePoints = new float[1][1]; //dummy init
                    int textureOffset = 8;                      //dummy init
                    if (t.hasTexture())
                    {
                        texturePoints = t.getTextureSource().getPointMatrix();
                        textureOffset = t.getOffsetTextures();
                    }
                    

                    //one single triangle from the triangle-set
                    for (int i = 0; i < indexes.length; i++)
                    {
                        Triangle tri = new Triangle();

                        //set common set-Globals to the single triangle
                        if (t.hasTexture())
                        {
                            tri.imageFileName = mat.getEffect().getImage().getPicture();
                            tri.containsTexture = true;
                        }
                        else
                        {
                            float[] colors;
                            if (mat == null)
                            {
                            	colors = new float[]{0.5f,0.5f,0.5f,0};
                            }
                            else {
                        		colors = mat.getEffect().getColor();
                        	}
                            tri.colour = new Color(colors[0], colors[1], colors[2], colors[3]);
                        }

                        //set the vertices for Point A
                        tri.A = new Point3D(
                                vertPoints[indexes[i][vertexOffset]][0],
                                vertPoints[indexes[i][vertexOffset]][1],
                                vertPoints[indexes[i][vertexOffset]][2]);

                        //set the texture for Point A
                        if (t.hasTexture())
                        {
                            tri.texA = new Point2D(
                                    texturePoints[indexes[i][textureOffset]][0],
                                    texturePoints[indexes[i][textureOffset]][1]);
                        }

                        i++;

                        //set the vertices for Point B
                        tri.B = new Point3D(
                                vertPoints[indexes[i][vertexOffset]][0],
                                vertPoints[indexes[i][vertexOffset]][1],
                                vertPoints[indexes[i][vertexOffset]][2]);

                        //set the texture for Point B
                        if (t.hasTexture())
                        {
                            tri.texB = new Point2D(
                                    texturePoints[indexes[i][textureOffset]][0],
                                    texturePoints[indexes[i][textureOffset]][1]);
                        }

                        i++;
                        //set the vertices for Point C
                        tri.C = new Point3D(
                                vertPoints[indexes[i][vertexOffset]][0],
                                vertPoints[indexes[i][vertexOffset]][1],
                                vertPoints[indexes[i][vertexOffset]][2]);

                        //set the texture for Point C
                        if (t.hasTexture())
                        {
                            tri.texC = new Point2D(
                                    texturePoints[indexes[i][textureOffset]][0],
                                    texturePoints[indexes[i][textureOffset]][1]);
                        }

                        //add it to the Collection
                        triangles.add(tri);
                    }
                }
            }

            //iterate the lines-sets in geometry
            if (geo.getLines() != null)
            {
                //one line-set
                for (Lines l : geo.getLines())
                {
                    Material mat = l.getMaterial();

                    //commons for the set of lines
                    float[][] vertPoints = l.getVertexSource().getPointMatrix();
                    int[][] indexes = l.getPointIndexMatrix();
                    int vertexOffset = l.getOffsetVertex();

                    //one single line from the line-set
                    for (int i = 0; i < indexes.length; i++)
                    {
                        Line ln = new Line();

                        //set common set-Globals to the single line
                        float[] colors = mat.getEffect().getColor();
                        ln.colour = new Color(colors[0], colors[1], colors[2], colors[3]);

                        //set the vertices for Point A
                        ln.A = new Point3D(
                                vertPoints[indexes[i][vertexOffset]][0],
                                vertPoints[indexes[i][vertexOffset]][1],
                                vertPoints[indexes[i][vertexOffset]][2]);

                        i++;
                        //set the vertices for Point B
                        ln.B = new Point3D(
                                vertPoints[indexes[i][vertexOffset]][0],
                                vertPoints[indexes[i][vertexOffset]][1],
                                vertPoints[indexes[i][vertexOffset]][2]);

                        //add it to the Collection
                        lines.add(ln);
                    }
                }
            }
        }

        //now lets convert Arraylist to a Simple Array (because processing has java 5)

        if (triangles.size() > 0)
        {
	        Triangle[] ptrTriangles = new Triangle[1];
	        this.triangles =  triangles.toArray(ptrTriangles);
        } else {
        	this.triangles = new Triangle[0];
        }

        if (lines.size() > 0)
        {
        	Line[] ptrLines = new Line[1];
        	this.lines =  lines.toArray(ptrLines);
        } else {
        	this.lines = new Line[0];
        }

    }


//    public static void main(String[] args)
//    {
//        ColladaRawLoader m = new ColladaRawLoader("./testfile.dae");
//        Triangle[] t = m.getTriangles();
//        Line[] l = m.getLines();
//        m = null;
//        System.out.println(l[0].A.x);
//    }

}
