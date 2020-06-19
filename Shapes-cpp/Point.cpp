//
//  Point.cpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#include "Point.hpp"
#include <iostream>

using namespace std;

Point::Point(double x, double y)
{
    xCoordinate = x;
    yCoordinate = y;
}

void Point::inputData(double x, double y)
{
    xCoordinate = x;
    yCoordinate = y;
}

void Point::inputData(){
    cout << "Enter x and y values: ";
    cin >> xCoordinate >> yCoordinate;
}

void Point::printData() const
{
    cout << "(" << xCoordinate << ", " << yCoordinate << ")" << endl;
}

double Point::getX() const
{
    return xCoordinate;
}

double Point::getY() const
{
    return yCoordinate;
}

void Point::setX(double x)
{
    xCoordinate = x;
}

void Point::setY(double y)
{
    yCoordinate = y;
}
