# satellite-tracker
The satellite tracker project is an attempt at operating on data in order to track satellite movements and to
predict future or past positions. The main purpose of finding future positions is to plan satellite passes over 
a distinct area for viewing.
Future versions could be capable of detecting satellite collisions with adequate computing power.

The remaining of this document will explain how each class is designed to function and its intended uses.

Obviously, given the fundamentals of object-oriented programming, any methodology pertaining to the use of a
class should be contained to the implementation of that class. Any other class that would like to utilize the 
implementation of another should do so through the latter class. For example, if we wish to instantiate a 
StateVectors object using a TLE instance, the StateVectors constructor should use the COE constructor with 
TLE parameter to then create a set of state vectors from the orbital elements. This separates the code that
pertains to a specific object and keeps the project tidy.

====================================================================================================
============================================= JD Class =============================================
====================================================================================================
The Julian Day class is used to represent time, and easily compute difference in time. It can also easily
determine a date and time given a previous date and the time in which to advance (or go back). The solar 
day is the largest interval of time that remains consistent in length as well as being ideal in magnitude 
for most computations. Months are of different lengths and years also are inconsistent due to leap years.
Fractions of a solar day are also very easy to compute, with easy to remember ratios of the smaller elements
(24 hours, 1440 minutes and 86400 seconds) we can convert from a UTC timestamp to a fractional day and back.
Therefore, most if not all of the instances we want to use a deltaT term (except for small integration steps)
we should aim to use solar days. This is the reason for the JD class, which greatly simplifies the calculations
needed to handle differences in time.

====================================================================================================
=========================================== Vector Class ===========================================
====================================================================================================
The Vector class is self explanatory. The vector is used to represent a 3 dimensional physical quantity
with a direction and a magnitude. In essence, the entire point of this project is to compute as accurate
as position vectors as we can. From there it's up to our imagination as to what we can do with that 
information! The Vector class methods will be the basic operations that can be done with a vector: vector 
addition and subtraction, scalar multiplication, vector dot and cross products and computing the magnitude 
and a normalized version of a vector. A vector can also use methods like addition assign and subtraction 
assign to manipulate a vector with creating a new instance.

