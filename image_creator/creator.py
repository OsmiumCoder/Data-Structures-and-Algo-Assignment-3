

from PIL import Image
import numpy as np

pixels1 = [   0,  100,  0,  0,
             120,  200, 200, 200,
             0,  100,  0,  0, 
             255, 50, 255,  255]

pixels2 = [   0,  0,  0,    0,   0,  0,
             0,  0,  0,  0,   0,   0,
             0,  0,  100,  100,  0,  0,
             0,  0,  100,  100,  0,  0,
             0,  0,  0,  0,  0,  0,
             0,  0,  0,  0,  0,  0]

pixels = pixels1

SIZE = 4


EXPANSION = 50
expanded = [None] * SIZE*EXPANSION * SIZE*EXPANSION

for r in range(SIZE):
    for c in range(SIZE):
        for i in range(EXPANSION):
            for j in range(EXPANSION):
                expanded[r*EXPANSION*SIZE*EXPANSION+i*EXPANSION*SIZE + c * EXPANSION + j]   = pixels[r*SIZE + c]    
                
#for i in range(12):
#    for j in range(12):
#        print(expanded[i*12 +j], end=" ")
#    print()


image_out = Image.new("L", (SIZE,SIZE))
image_out.putdata(pixels)
image_out.save('img_out.png')


image_out_lg = Image.new("L", (SIZE*EXPANSION,SIZE*EXPANSION))
image_out_lg.putdata(expanded)
image_out_lg.save('img_out_lg.png')


