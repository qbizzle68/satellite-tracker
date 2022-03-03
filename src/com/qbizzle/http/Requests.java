/**
 * @file
 * Contains the Requests class which itself contains various methods for sending
 * HTTP requests for information the user may not have available to them and
 * need for finding passes.
 */

package com.qbizzle.http;

import com.qbizzle.orbit.TLE;
import com.qbizzle.tracking.Coordinates;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Vector;

/**
 * The Requests class is a static class that contains methods for handling the
 * specific HTML requests required for a more automated experience using the
 * satellite tracking program.
 */
@SuppressWarnings("unused")
public class Requests {
    /** URL used to access Map Quest's Open Elevation API. */
    static private final String OPEN_MAPQUEST_ELEVATION_URL =
            "http://open.mapquestapi.com/elevation/v1/profile?key=KEY&shapeFormat=raw&latLngCollection=";
    /** URL to access Map Quest's Open Geocoding API. */
    static private final String OPEN_MAPQUEST_GPS_URL =
            "http://open.mapquestapi.com/geocoding/v1/address?key=KEY&location=";
    /** Key used for Map Quest's Open APIs. */
    static private final String OPEN_MAPQUEST_KEY = "vJ1uvrztzo5ctdTt7CgMAjFfZpWjtNxa";
    /** URL to access CelesTrak's GP data. */
    static private final String CELESTRAK_GP_URL =
            "https://celestrak.com/NORAD/elements/gp.php?QUERY=VALUE&FORMAT=TLE";

    /**
     * Method that accesses Map Quest's Open Elevation API and parses the response
     * JSON to retrieve the elevation for a given location.
     * <p>
     * This is a wrapper method equivalent to
     * <blockquote><pre>
     *     return getElevationFromJSON(getElevationJSON(geoPosition));
     * </pre></blockquote>
     * </p>
     *
     * @param geoPosition   The GeoPosition for the elevation being inquired.
     * @return              The elevation at {@code geoPosition}.     * @throws IOException
     * @throws InterruptedException If the thread is interrupted.
     * @throws ParseException       If there is an error parsing the JSON response.
     */
    public static double getElevation(Coordinates geoPosition) throws IOException, InterruptedException, ParseException {
        return getElevationFromJSON(
                getElevationJSON(geoPosition)
        );
    }

    /**
     * Method that accesses Map Quest's Open Elevation API to get GeoPositioning data for
     * a given location.
     * <p>
     * This is a wrapper method equivalent to
     * <blockquote><pre>
     *     return getGeoPositionFromJSON(getGeoPositionJSON(location));
     * </pre></blockquote>
     * </p>
     * @param location  A string representation of the address to search for. To avoid errors
     *                  the address should be as broad as possible, probably just a city, state
     *                  value, unless your location is a large city and needs to be narrowed.
     * @return          The first GeoPosition contained in the response.
     * @throws IOException
     *                  If there is an I/O error while sending or receiving.
     * @throws InterruptedException
     *                  If the HTTP request is interrupted.
     * @throws ParseException
     *                  If there is an error parsing JSON response by json-simple.
     */
    public static Coordinates getGeoPosition(String location) throws IOException, InterruptedException, ParseException {
        return getGeoPositionFromJSON(
                getGeoPositionJSON(location)
        );
    }

    /**
     * Requests Map Quest's Open Elevation API to get Elevation data for a given GeoPosition.
     * <p>
     * This requests the Open version of Map Quest's APIs, therefore the accuracy and probability
     * of finding an answer is lower.
     * @param geoPos    The GeoPosition of where the elevation is being inquired.
     * @return          A String representation of the response which is in JSON form.
     * @throws IOException
     *                  If there is an I/O error while sending or receiving.
     * @throws InterruptedException
     *                  If the HTTP request is interrupted.
     */
    public static String getElevationJSON(Coordinates geoPos) throws IOException, InterruptedException {
        String URL = OPEN_MAPQUEST_ELEVATION_URL
                .replace("KEY", OPEN_MAPQUEST_KEY) +
                geoPos.getLatitude() + "," + geoPos.getLongitude();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Parses the JSON-String returned by {@link #getElevationJSON} to obtain the
     * elevation value.
     * @param jsonString    String representation of the JSON.
     * @return              The elevation.
     * @throws ParseException
     *                      If there is an error parsing the JSON.
     */
    public static double getElevationFromJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONObject elevationProfile = ((JSONObject) ((JSONArray)json.get("elevationProfile")).get(0));
        return ((Number) elevationProfile.get("height")).doubleValue();
    }

    /**
     * Method that accesses Map Quest's Open Geocoding API to get GeoPositioning data for
     * a given location.
     * <p>
     *     This requests the Open version of Map Quest's APIs, therefore the accuracy and
     *     probability of finding an answer is lower.
     * </p>
     * @param location  A string representation of the address to search for. To avoid errors
     *                  the address should be as broad as possible, probably just a city, state
     *                  value, unless your location is a large city and needs to be narrowed.
     * @return          The first GeoPosition contained in the response.
     * @throws IOException
     *                  If there is an I/O error while sending or receiving.
     * @throws InterruptedException
     *                  If the HTTP request is interrupted.
     */
    public static String getGeoPositionJSON(String location) throws IOException, InterruptedException {
        String URL = OPEN_MAPQUEST_GPS_URL
                .replace("KEY", OPEN_MAPQUEST_KEY)
                + location.replace(" ", "%20");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * Parses the JSON-String returned by {@link #getGeoPositionJSON} to obtain the GeoPosition value.
     * @param jsonString    String representation of the JSON.
     * @return              The GeoPosition in a {@link com.qbizzle.tracking.Coordinates} object.
     * @throws ParseException
     *                      If there is an error parsing the JSON.
     */
    public static Coordinates getGeoPositionFromJSON(String jsonString) throws ParseException {
        JSONParser parser = new JSONParser();
        JSONObject json = (JSONObject) parser.parse(jsonString);
        JSONObject resultsObj = (JSONObject)((JSONArray) json.get("results")).get(0);
        JSONObject locationsObj = (JSONObject)((JSONArray) resultsObj.get("locations")).get(0);
        JSONObject latlng = (JSONObject) locationsObj.get("latLng");
        return  new Coordinates(
                ((Number) latlng.get("lat")).doubleValue(),
                ((Number) latlng.get("lng")).doubleValue()
        );
    }

    /**
     * Requests TLEs from Celestrak and packages them in a java.util.Vector.
     * <p>
     * The length of the returned java.util.Vector is significant because it is indicative of
     * the results of the request. If {@code java.util.Vector.size()} returns 0 then the response
     * from Celestrak was "No GP data found". If {@code size()} returns greater than 1 then the
     * search for {@code satName} resulted in more than 1 result.
     * @param satName       The String used to search Celestrak.
     * @return              A java.util.Vector containing the results from the HTTP request.
     * @throws IOException  If there is an I/O error while sending or receiving.
     * @throws InterruptedException
     *                      If the HTTP request is interrupted.
     */
    public static java.util.Vector<TLE> getTLEList(String satName) throws IOException, InterruptedException {
        String URL = CELESTRAK_GP_URL
                .replace("QUERY", "NAME")
                .replace("VALUE", satName.replace(" ", "%20"));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(URL))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        java.util.Vector<TLE> tleList = new Vector<>();
        if (response.body().equals("No GP data found")) return tleList; // maybe throw an exception
        String[] responseArray = response.body().split("\n");
        for (int i = 0; i < responseArray.length; i+=3) {
            TLE tle = new TLE(
                    responseArray[i] + "\n"
                    + responseArray[i+1] + "\n"
                    + responseArray[i+2]
            );
            tleList.add(tle);
        }
        return tleList;
    }

}