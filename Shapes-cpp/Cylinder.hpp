//
//  Cylinder.hpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#ifndef Cylinder_hpp
#define Cylinder_hpp

#include <stdio.h>
#include "Circle.hpp"
#include "Point.hpp"

class Cylinder
{
public:
    Cylinder(double x = 0.0, double y = 0.0, double rad = 0.0, double h = 0.0);
    Cylinder(Circle cir, double h = 0.0);
    double getHeight();
    void setHeight(double h);
    void printHeight();
    void computeAndPrintVolume();
    void computeAndPrintSurfaceArea();

    double surface_area() const;

    double volume() const;

    void printData() const;


    void inputData();

private:
    Circle base;
    double height;
};

#endif /* Cylinder_hpp */
