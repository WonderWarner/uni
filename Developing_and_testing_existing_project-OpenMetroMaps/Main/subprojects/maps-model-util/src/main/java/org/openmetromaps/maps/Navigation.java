package org.openmetromaps.maps;

import org.openmetromaps.maps.model.Line;
import org.openmetromaps.maps.model.ModelData;
import org.openmetromaps.maps.model.Station;
import org.openmetromaps.maps.model.Stop;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Navigation {
    public enum RoutePreference {
        FEWER_STOPS, FEWER_TRANSFERS
    }

    //Structure to store the data of a Stop
    private static class StopData {
        Stop stop;
        //Distance from the source Station depending on the RoutePreference
        //At the beginning every Station's stop is infinie except the source Station's Stops
        int distance=Integer.MAX_VALUE;
        //If we already reached a Stop in the possible shortest way (distance reached it's minimum value)
        boolean reached=false;
        //Storing where we directly came from to this Stop
        StopData prevStop=null;
        public StopData(Stop s) {
            stop=s;
        }
    }

    //Finding the best route between two Stations depending on the preference
    //Lines can be excluded as well
    public static List<Stop> findPath(ModelData model, Station src, Station dst, RoutePreference preference, Set<Line> excludedLines) {
        ArrayList<Stop> bestRoute=new ArrayList<>();
        ArrayList<StopData> stops=createStartingGraph(model, src, excludedLines);
        
        //if the Preference is FewerStops, then fewerS will be 0, else 1,
        //this will be used to set the distance of the Stops in the same Station as minNotReached
        //the value of fewerT is the contrary, and used to set the distance of Stops in the same line as minNotReached
        int fewerS=0;
        int fewerT=0;
        if(preference==RoutePreference.FEWER_TRANSFERS) fewerS++;
        else fewerT++;
        
        //Dijksra algorythm
        //During every iteration one Stop will be reached as a minimum if possible
        //If not the loop breaks, but in maximum stops.size() iterations every reachable Stop will be reached on the shortest route possible
        for(int n=1; n<stops.size(); n++) {
            //First step: choosing a Stop that is not reachable in a shorter way
            StopData minNotReached=nextReachedStop(stops);
            if(minNotReached==null) {
                break;
            }
            //Then updating it's direct neighbours distance depending on what the preference is
            //First the stops that has the same Station
            setDistanceInSameStation(minNotReached, stops, fewerS);
            //Then the Stops that has the same Line
            setDistanceOnSameLine(minNotReached, stops, fewerT);
            //And then setting the minNotReached Stop to reached
            minNotReached.reached=true;
        }
        //Sum where we came from
        StopData destination=null;
        int distance=Integer.MAX_VALUE;
        for(StopData sd: stops) {
            if(sd.stop.getStation().equals(dst)&&distance>sd.distance&&!(sd.prevStop.stop.getStation().equals(dst))) {
                destination=sd;
                distance=sd.distance;
            }
        }
        while(destination!=null) {
            bestRoute.add(0, destination.stop);
            destination=destination.prevStop;
        }
        return bestRoute;
    }

    //Setting up the model that will store the needed data for the algorythm
    private static ArrayList<StopData> createStartingGraph(ModelData model, Station src, Set<Line> excludedLines) {
        ArrayList<StopData> stops=new ArrayList<>();
        //Excluding the proper lines
        for(Line line: model.lines) {
            if(excludedLines.contains(line)) {
                continue;
            }
            //Adding every needed Stop and setting the distance of the starting stops to 0
            for(Stop stop: line.getStops()) {
                stops.add(new StopData(stop));
                if(stop.getStation().equals(src)) {
                    StopData source=stops.get(stops.size()-1);
                    source.distance=0;
                }
            }
        }
        return stops;
    }

    //Choosing a minimal distance stop that isn't reached yet, but not reachable in a shorter route.
    private static StopData nextReachedStop(ArrayList<StopData> stops) {
        StopData minNotReached=null;
        for(StopData i: stops) {
            if(i.distance!=Integer.MAX_VALUE&&!i.reached&&(minNotReached==null||minNotReached.distance>i.distance)) {
                minNotReached=i;
            }
        }
        return minNotReached;
    }

    //minNotReached will be reached, so it's distance is the possible lowest.
    //Thus the Stops in the same Station might be closer than before (precisely: minNotreached+fewerS)
    private static void setDistanceInSameStation(StopData minNotReached, ArrayList<StopData> stops, int fewerS) {
        for(Stop s: minNotReached.stop.getStation().getStops()) {
            StopData actStopData=null;
            for(StopData j: stops) {
                if(j.stop.equals(s)) {
                    actStopData=j;
                    break;
                }
            }
            if(actStopData!=null&&actStopData.distance>(minNotReached.distance+fewerS)) {
                actStopData.distance=minNotReached.distance+fewerS;
                actStopData.prevStop=minNotReached;
            }
        }
    }

    //minNotReached will be reached, so it's distance is the possible lowest.
    //Thus the 2 Stops before and after on the same Line (if they exist) might be closer than before (precisely: minNotreached+fewerT)
    private static void setDistanceOnSameLine(StopData minNotReached, ArrayList<StopData> stops, int fewerT) {
        //Getting the index of the Stop on its line
        int idx=minNotReached.stop.getLine().getStops().indexOf(minNotReached.stop);
        //If its not the starting Stop, there is a Stop before it
        if(idx>0) setDistanceStopBeforeAfter(minNotReached, stops, fewerT, idx-1);
        //If its not the last Stop, there is a Stop after it
        if(idx<minNotReached.stop.getLine().getStops().size()-1) setDistanceStopBeforeAfter(minNotReached, stops, fewerT, idx+1);
    }

    //Setting the distance of a certain Stop if necessary
    //Can be the Stop before/after minNotReached on the same Line - set in neighbourIdx
    private static void setDistanceStopBeforeAfter(StopData minNotReached, ArrayList<StopData> stops, int fewerT, int neighbourIdx) {
        Stop actStop=minNotReached.stop.getLine().getStops().get(neighbourIdx);
        StopData actStopData=null;
        for(StopData j: stops) {
    		if(j.stop.equals(actStop)) {
        		actStopData=j;
    			break;
            }
        }
    	if(actStopData!=null&&actStopData.distance>(minNotReached.distance+fewerT)) {
    		actStopData.distance=minNotReached.distance+fewerT;
        	actStopData.prevStop=minNotReached;
        }
    }
}