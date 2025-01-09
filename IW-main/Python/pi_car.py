from gpiozero import LED
from time import sleep


def pi_car(fl_i,bl_i, fr_i, br_i):
    fl= LED(fl_i)
    bl= LED(bl_i)
    fr = LED(fr_i)
    br = LED(br_i)
    l= [fl,bl,fr,br]
    return l;
    
def forward(car):
    car[0].on()
    car[2].on()

def backward(car):
    car[1].on()
    car[3].on()

def left(car):
    car[1].on()
    car[2].on()

def right(car):
    car[0].on()
    car[3].on()
    
def halt(car):
    for i in range(4):
        car[i].off()
    
    


    
