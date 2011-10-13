//this example shows how the datas from the loader will be read and drawn to scetch
import colladaLoader.*;

ColladaLoader model;
Triangle[] triangles;
Line[] lines;

float rotX = -0.6509995f;
float rotY = -0.52000004f;
int x = -15;
int y = 517;
float changeSize = 2.1501188f;


void setup()
{
      size(500, 500, P3D);
      //because the truck has no textures and we draw manually we can set 2nd parameter to null
      //copy truck manually to processing's "data" folder first
      model = ColladaLoader.getInstance("truck5_8.dae", null);
      triangles = model.getTriangles();
      lines = model.getLines();
      frameRate(10);
}

void draw()
{
        background(16);

      lights();
      
      translate(x, y);
      rotateX(rotY);
      rotateY(rotX);
      scale(changeSize);
      
      drawModel();
      
}
   
void drawModel()
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




