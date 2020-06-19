//
//  Point.hpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#ifndef Point_hpp
#define Point_hpp

#include <stdio.h>

class Point
{
public:
    Point(double x = 0.0, double y = 0.0);
    void inputData(double x, double y);
    void inputData();
    void printData() const;
    double getX() const;
    double getY() const;
    void setX(double x);
    void setY(double y);
private:
    double xCoordinate;
    double yCoordinate;
};

#endif /* Point_hpp */
