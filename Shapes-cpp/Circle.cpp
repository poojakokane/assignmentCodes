//
//  Circle.cpp
//  MP4
//
//  Created by Daniel Colburn on 6/18/20.
//  Copyright © 2020 Daniel Colburn. All rights reserved.
//

#include "Point.hpp"
#include "Circle.hpp"

#include <iostream>

using namespace std;

Circle::Circle(double x, double y, double rad)
{
    this->center = Point(x, y);
    radius = rad;
}

Circle::Circle(Point p, double rad)
{
    this->center = Point(p.getX(), p.getY());
    this->radius = rad;
}

double Circle::getRadius()
{
    return radius;
}

Point Circle::getCenter()
{
    return center;
}

void Circle::setRadius(double rad)
{
    radius = rad;
}

void Circle::setCenter(double x, double y)
{
    center.setX(x);
    center.setY(y);
}

void Circle::printRadius()
{
    cout << "Radius: " << radius << endl;
}

void Circle::printCenter()
{
    cout << "Center: ";
    center.printData();
}
const double Circle::area() const {
    return (3.1416 * radius * radius);
}

const double Circle::circumference() const {
    return (2.0 * 3.1416 * radius);
}

void Circle::printData() const {
    cout << "Center = ";
    center.printData();
    cout << "Radius = " << radius << endl;
}

void Circle::inputData() {
    center.inputData();
    cout << "Enter radius: ";
    cin >> radius;
}
