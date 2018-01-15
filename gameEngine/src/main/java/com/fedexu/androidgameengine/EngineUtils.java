package com.fedexu.androidgameengine;



import android.graphics.Point;
import android.util.Log;

import com.fedexu.androidgameengine.object.BasicObject;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;


/**
 * Created by fperuzzi on 23/12/2017.
 *
 */

public class EngineUtils {

    public static double normalizeAngle(double angle) {
        if (angle > 0)
            while (angle > 360)
                angle -= 360;
        else
            while (angle < -360)
                angle += 360;
        return angle;
    }

    public static String readJsonfile(InputStream resourceReader){

        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resourceReader, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
        } catch (Exception e) {
            Log.e( "JSONResourceReader Ex" , e.toString());
        } finally {
            try {
                resourceReader.close();
            } catch (Exception e) {
                Log.e("JSONResourceReader Ex" , e.toString());
            }
        }
        return writer.toString();
    }

    public static <T> T parseJsonString(Class<T> type, String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, type);
    }

    public static void pointPercToPointPx(BasicObject b, Point size){
        double[] x = b.getX();
        double[] y = b.getY();

        for(int i = 0; i < b.getX().length; i++){
            x[i] = (x[i]/100) * size.x;
            y[i] = (y[i]/100) * size.y ;
        }
        b.setX(x);
        b.setY(y);

    }

}
/*
    public static Point2D getFirstIntersectionPoint(final Line2D.Double line, final Polygon poly){
        Set<Point2D> intersectionPoints = getIntersections(poly,line);
        Point2D myPosition = new Point2D.Double(poly.getBounds().getCenterX(), poly.getBounds().getCenterY());
        return getNearestPoint(myPosition, intersectionPoints);

    }

    public static Set<Point2D> getIntersections(final Polygon poly, final Line2D.Double line){

        final PathIterator polyIt = poly.getPathIterator(null); //Getting an iterator along the polygon path
        final double[] coords = new double[6]; //Double array with length 6 needed by iterator
        final double[] firstCoords = new double[2]; //First point (needed for closing polygon path)
        final double[] lastCoords = new double[2]; //Previously visited point
        final Set<Point2D> intersections = new HashSet<>(); //List to hold found intersections
        polyIt.currentSegment(firstCoords); //Getting the first coordinate pair
        lastCoords[0] = firstCoords[0]; //Priming the previous coordinate pair
        lastCoords[1] = firstCoords[1];
        polyIt.next();
        while(!polyIt.isDone()) {
            final int type = polyIt.currentSegment(coords);
            switch(type) {
                case PathIterator.SEG_LINETO : {
                    final Line2D.Double currentLine = new Line2D.Double(lastCoords[0], lastCoords[1], coords[0], coords[1]);
                    if(currentLine.intersectsLine(line))
                        intersections.add(getIntersection(currentLine, line));
                    lastCoords[0] = coords[0];
                    lastCoords[1] = coords[1];
                    break;
                }
                case PathIterator.SEG_CLOSE : {
                    final Line2D.Double currentLine = new Line2D.Double(coords[0], coords[1], firstCoords[0], firstCoords[1]);
                    if(currentLine.intersectsLine(line))
                        intersections.add(getIntersection(currentLine, line));
                    break;
                }
                default : {
                    return null;
                    //throw new Exception("Unsupported PathIterator segment type.");
                }
            }
            polyIt.next();
        }
        return intersections;

    }

    public static Point2D getIntersection(final Line2D.Double line1, final Line2D.Double line2){

        final double x1,y1, x2,y2, x3,y3, x4,y4;
        x1 = line1.x1; y1 = line1.y1; x2 = line1.x2; y2 = line1.y2;
        x3 = line2.x1; y3 = line2.y1; x4 = line2.x2; y4 = line2.y2;
        final double x = (
                (x2 - x1)*(x3*y4 - x4*y3) - (x4 - x3)*(x1*y2 - x2*y1)
        ) /
                (
                        (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
                );
        final double y = (
                (y3 - y4)*(x1*y2 - x2*y1) - (y1 - y2)*(x3*y4 - x4*y3)
        ) /
                (
                        (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4)
                );

        return new Point2D.Double(x, y);

    }

    public static Point2D getNearestPoint(Point2D myPosition, Set<Point2D> listOfPoint){
        Point2D nearest = null;
        double distance = Double.MAX_VALUE;
        double newDistance;
        for (Point2D p : listOfPoint ) {
            newDistance = EngineUtils.distance(myPosition, p);
            if (newDistance < distance){
                distance = newDistance;
                nearest = p;
            }
        }
        return nearest;
    }

    // A utility function to find the distance between two points
    public static double distance(Point2D p1, Point2D p2){
        return Math.sqrt( (p1.getX() - p2.getX())*(p1.getX() - p2.getX()) +
                (p1.getY() - p2.getY())*(p1.getY() - p2.getY())
        );
    }*/
