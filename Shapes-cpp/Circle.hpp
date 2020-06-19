//
//  Circle.hpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#ifndef Circle_hpp
#define Circle_hpp

#include "Point.hpp"
#include <stdio.h>

class Circle
{
public:
    Circle(double x = 0.0, double y = 0.0, double rad = 0.0);
    Circle(Point, double radius);
    double getRadius();
    Point getCenter();
    void setRadius(double rad);
    void setCenter(double x, double y);
    void printRadius();
    void printCenter();
    const double area() const;
    const double circumference() const;

    void printData() const;

    void inputData();

private:
    double radius;
    Point center;
};

#endif /* Circle_hpp */
