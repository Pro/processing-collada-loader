package colladaLoader;
import java.io.*;
import java.util.*;
import java.util.zip.*;

import colladaLoader.ColladaLoaderException;
import colladaLoader.Line;
import colladaLoader.Triangle;
import colladaLoader.xmlMapping.ColladaRawLoader;


import processing.core.PApplet;
import processing.core.PImage;
import processing.xml.XMLElement;
/**
 * <p>Lucerne University of Applied Sciences and Arts <a href="http://www.hslu.ch">http://www.hslu.ch</a></p>
 *
 * <p>This source is free; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License and by nameing of the originally author</p>
 *
 * <p>this class starts the kmz-unzipping, the xml-Loading and loads the texture-images. It will be used only once...</p>
 *
 * @author Markus Zimmermann <a href="http://www.die-seite.ch">http://www.die-seite.ch</a>
 * @version 1.0.1
 */
class LoadingHelper {

    private Triangle[] triangles;
    private Line[] lines ;
    private String pathName = "./";  //dummydefault
    private String fileName = "";


/**
 * use this constructor if you're shure that your export does'nt contain any textures and you have no intension to use the draw-methode
 * @param filename of a .dae or .kmz -endian
 */
    LoadingHelper(String file)
    {
        file = checkIfPathDefined(file);  //belongs to the Processing data-Folder concept
        pathName = ""+file.substring(0,file.lastIndexOf("/")+1);
        fileName = ""+file.substring(file.lastIndexOf("/")+1, file.length());

        ColladaRawLoader rawLoader;
           if (file.endsWith("kmz"))
           {
               unzip();
               useDOCkml(); //reconfigures pathname + filename
               rawLoader = new ColladaRawLoader(pathName+fileName);
           }
           else
               rawLoader = new ColladaRawLoader(pathName+fileName);

       triangles = rawLoader.getTriangles();
       lines = rawLoader.getLines();

       try {recalculateSketchupAxisToProcessingAxis();}catch (RuntimeException e) { throw new ColladaLoaderException("Data read from xml-file are not consistent", e);}
       
       rawLoader = null; //activate GC to flush
    }
/**
 * use this constructor if you think the export might contain any textures
 * @param filename of a .dae or .kmz
 */
    LoadingHelper(String file, PApplet applet)
    {
        this(file);
        try {recalculateSketchupTexturesToProcessing(applet);}catch (RuntimeException e) { throw new ColladaLoaderException("Data read from xml-file might contain bad texture-filenames",e);}
    }

    /**
     * reconfigures pathname and filename using doc.kml inside the kmz-File
     * it adds a subpath to pathname and renames the filename from kmz to dae
     */
    private void useDOCkml()
    {
        PApplet applet = new PApplet();
        XMLElement root = new XMLElement(applet,pathName+"doc.kml");
        XMLElement dae = root.getChild("Folder/Placemark/Model/Link/href");
        String file = dae.getContent();

        pathName += file.substring(0,file.lastIndexOf("/")+1);
        fileName = ""+file.substring(file.lastIndexOf("/")+1, file.length());
    }
    /**
     * Checks if a Path as prefix is defined, if yes the filname will be unchanged
     * otherwise it gets the prefix ./data/
     * @param filename
     * @return the checked Filename
     */
    private String checkIfPathDefined(String filename)
    {
        boolean pathdefined = false;
        pathdefined |= filename.startsWith(".",0); //linux and apple
        pathdefined |= filename.startsWith("/",0); //do
        pathdefined |= filename.startsWith(":",1); //windoze

        if (! pathdefined) filename = System.getProperty("user.dir")+"/data/"+filename;

        return filename;
    }
 /*
  * per definition is:
  * processing x = sketchup x
  * processing y = sketchup -z
  * processing z = sketchup -y
  * let's do the vertices ...
  */
    private void recalculateSketchupAxisToProcessingAxis()
    {
        for (Triangle tri : triangles)
        {
            float AprocX = tri.A.x; float AprocY = -tri.A.z; float AprocZ = -tri.A.y;
            float BprocX = tri.B.x; float BprocY = -tri.B.z; float BprocZ = -tri.B.y;
            float CprocX = tri.C.x; float CprocY = -tri.C.z; float CprocZ = -tri.C.y;

            tri.A.x = AprocX; tri.A.y = AprocY; tri.A.z = AprocZ;
            tri.B.x = BprocX; tri.B.y = BprocY; tri.B.z = BprocZ;
            tri.C.x = CprocX; tri.C.y = CprocY; tri.C.z = CprocZ;
        }

        for (Line lin : lines)
        {
            float AprocX = lin.A.x; float AprocY = -lin.A.z; float AprocZ = -lin.A.y;
            float BprocX = lin.B.x; float BprocY = -lin.B.z; float BprocZ = -lin.B.y;

            lin.A.x = AprocX; lin.A.y = AprocY; lin.A.z = AprocZ;
            lin.B.x = BprocX; lin.B.y = BprocY; lin.B.z = BprocZ;
        }
    }
    /**
     * per definition is:
     * textureProcessing x = textureSketchup x * picture.width
     * textureProcessing y = picture.height - textureSketchup y * picture.height
     * it also creates an PImage to each Triangle (if it contains any Texture)
     */
    private void recalculateSketchupTexturesToProcessing(PApplet applet)
    {
        //load all Texture-Images only once (memory efficiency)
        HashMap<String,PImage> pictures = new HashMap<String, PImage>();
        for (Triangle tri : triangles)
        {
            String texfile = pathName+tri.imageFileName;
            if (tri.containsTexture && pictures.get(texfile)== null)
            {
                PImage tex = applet.loadImage(texfile);
                pictures.put(texfile, tex);
            }
        }


        for (Triangle tri : triangles)
        {
            if (tri.containsTexture)
            {
                String texfile = pathName+tri.imageFileName;
                //PImage tex = applet.loadImage(texfile);
                PImage tex = pictures.get(texfile);
                float AprocX = tri.texA.x * tex.width; float AprocY = tex.height - tri.texA.y * tex.height;
                float BprocX = tri.texB.x * tex.width; float BprocY = tex.height - tri.texB.y * tex.height;
                float CprocX = tri.texC.x * tex.width; float CprocY = tex.height - tri.texC.y * tex.height;

                tri.texA.x = AprocX; tri.texA.y = AprocY;
                tri.texB.x = BprocX; tri.texB.y = BprocY;
                tri.texC.x = CprocX; tri.texC.y = CprocY;

                tri.imageReference = tex;
            }
        }
    }

/**
 * unzips to the same path as the zipfile is
 */
    private void unzip()
    {

        try {
             ZipFile zipfile = new ZipFile(pathName+fileName);
             Enumeration<? extends ZipEntry> e = zipfile.entries();
             while(e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();

                //if creating a directory reqired..
                String zipInternPath = ""+entry.getName().substring(0,entry.getName().lastIndexOf("/")+1);
                if (zipInternPath.length()>0) new File(pathName+zipInternPath).mkdirs();

                //unzip file
                int len;
                byte[] buffer = new byte[16384];
                File outfile = new File(""+pathName+entry.getName());
                if (outfile.exists()) outfile.delete(); //be shure to overwrite an existing file
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(outfile));
                BufferedInputStream bis = new BufferedInputStream(zipfile.getInputStream(entry));
                while ((len = bis.read(buffer)) > 0)
                    bos.write(buffer, 0, len);
                bos.flush();
                bos.close();
                bis.close();

             }
        } catch(Exception e) { throw new RuntimeException(e.getMessage());}

    }

/**
 * returns the prepared Triangeles from the file to use whatever you want
 * @return Triangles
 */
    Triangle[] getTriangles()
    {
        return triangles;
    }
/**
 * returns the prepared Lines from the file to use whatever you want
 * @return the Lines
 */
    Line[] getLines()
    {
        return lines;
    }

}
