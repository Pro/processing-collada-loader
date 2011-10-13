// this example shows how to use the model manipulations (this is avaiable from version 1.1)
// instead using papplet's scale()rotate() or translate() on each draw() loop
// you use the model's methods only once and save CPU-Performance on this way
import colladaLoader.*;

    ColladaLoader model;

    float angle = 0.08;
    char axis ='y';
    
void setup()
{
   size(500, 500, P3D);
   //copy duff.kmz manually to processing's "data" folder or choose sketch->add file
   model = ColladaLoader.getInstance("duff.kmz", this);
   //model = ColladaLoader.getInstance(sketchPath("../data/duff.kmz"), this);
   frameRate(20);
   model.scale(3.0f);
}

void draw()
{
    background(16);
    lights(); 

    translate(250, 250);   //goodies only for a better view
    rotateX(-0.32000035f);
    rotateY(-0.048000026f);
    stroke(125, 0, 0);
    strokeWeight(1);
    line(0, 0, 0, width, 0, 0);
    stroke(0, 125, 0);
    line(0, 0, 0, 0, 0, -width);
    stroke(0, 0, 125);
    line(0, 0, 0, 0, -height, 0);    
  
  
    model.rotate(angle, axis); //rotates Model as an animation  
 
    model.draw();
    
}

void mouseDragged()
{
  //scales model bigger or smaller
   model.scale(1+(pmouseY - mouseY) * 0.01f); 
}

void keyPressed() {
  
   //shifts translates model in all 3D axis
   if (keyCode == LEFT) {angle = 0; model.shift(-5.0f, 'x');}
   if (keyCode == RIGHT) {angle=0; model.shift(5.0f, 'x');}
   if (keyCode == UP) {angle = 0; model.shift(-5.0f, 'y');}
   if (keyCode == DOWN) {angle = 0; model.shift(5.0f, 'y');}
   if (key == 'f') {angle = 0; model.shift(-5.0f, 'z');}
   if (key == 'b') {angle = 0; model.shift(5.0f, 'z');}        
   if (key == 'x'||key == 'y'||key == 'z') {axis = key; angle = 0.08f;}
}
