package org.openmetromaps.maps;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openmetromaps.gtfs.DraftModel;
import org.openmetromaps.gtfs.GtfsImporter;
import org.openmetromaps.maps.model.Line;
import org.openmetromaps.maps.model.ModelData;
import org.openmetromaps.maps.model.Station;
import org.openmetromaps.maps.model.Stop;
import org.openmetromaps.misc.NameChanger;
import org.openmetromaps.model.gtfs.DraftModelConverter;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.IntStream;

import static org.openmetromaps.maps.Navigation.RoutePreference.FEWER_STOPS;
import static org.openmetromaps.maps.Navigation.RoutePreference.FEWER_TRANSFERS;

@RunWith(Parameterized.class)
public class TestNavigation {

    @Parameterized.Parameter(0)
    public String path;

    @Parameterized.Parameters(name = "ID: {0}")
    public static Collection<Object[]> data() {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        URL url = Objects.requireNonNull(loader.getResource("."));
        return Arrays.stream(
            Objects.requireNonNull(new File(url.getPath()).listFiles()))
                .filter(
                    file -> file.exists() &&
                    file.isDirectory() &&
                    Arrays.stream(Objects.requireNonNull(file.listFiles())).anyMatch(f -> f.getName().equals("input.zip"))
                )
                .map(file -> new Object[] { file.getName() }
        ).toList();
    }

    @Test
    public void testRoutesEqual() throws Exception {
        Path basePath = Path.of("src/test/resources/" + path);
        Path inputPath = basePath.resolve("input.zip");
        Path expectedPath = basePath.resolve("expected.txt");
        Path operationPath = basePath.resolve("operation.txt");

        ModelData inputModel = importModelFromGtfs(inputPath);
        String[] operation = Files.readString(operationPath).replace("\uFEFF", "").split(";");

        String stationNameA = operation[1].trim();
        String stationNameB = operation[2].trim();
        Station stationA = inputModel.stations.stream().filter(s -> s.getName().equals(stationNameA)).findFirst().orElse(null);
        Station stationB = inputModel.stations.stream().filter(s -> s.getName().equals(stationNameB)).findFirst().orElse(null);

        Set<Line> linesToExclude = new HashSet<>();
        for (int i = 3; i < operation.length; i++) {
            String lineName = operation[i].trim();
            if(lineName.isEmpty()) continue;
            Line line = inputModel.lines.stream().filter(l -> l.getName().equals(lineName)).findFirst().orElse(null);
            if (line != null) {
                linesToExclude.add(line);
            } else {
                throw new IllegalArgumentException("Line not found: " + operation[i]);
            }
        }

        if (stationA == null || stationB == null) {
            throw new IllegalArgumentException("Stations not found: " + stationNameA + ", " + stationNameB); // Fail the test if stations are not found
        }

        String[] expectedRoutes = Files.readAllLines(expectedPath).toArray(new String[0]);
        String[][] outputRoute;

        switch (operation[0]) {
            case "FEWER_STOPS":
                outputRoute = Navigation.findPath(inputModel, stationA, stationB, FEWER_STOPS, linesToExclude).stream().map(s -> new String[] { s.getStation().getName(), s.getLine().getName() }).toArray(String[][]::new);
                break;
            case "FEWER_TRANSFERS":
                outputRoute = Navigation.findPath(inputModel, stationA, stationB, FEWER_TRANSFERS, linesToExclude).stream().map(s -> new String[] { s.getStation().getName(), s.getLine().getName() }).toArray(String[][]::new);
                break;
            default:
                throw new IllegalArgumentException("Unsupported route preference: " + operation[0]); // Fail the test for unsupported route preference
        }

        if (expectedRoutes.length != 0){
            for (String expectedRouteString : expectedRoutes) {
                String[][] expectedRoute = Arrays.stream(expectedRouteString.replace("\uFEFF", "").trim().split(";"))
                        .map(String::trim)
                        .map(s -> new String[] { s.split("@")[0].trim(), s.split("@")[1].trim() })
                        .toArray(String[][]::new);

                if(expectedRoute.length == outputRoute.length && IntStream.range(0, expectedRoute.length).allMatch(i -> Arrays.equals(expectedRoute[i], outputRoute[i]))) {
                    return; // The route matches, the test passes
                }
            }
        } else {  // There is no route
            if (outputRoute.length == 0){
                return; // Navigation did not find a route, the test passes
            }
        }
        // If none of the routes matched
        Assert.fail("None of the expected routes matched the output route");
    }



    private ModelData importModelFromGtfs(Path pathInput) throws IOException {
        GtfsImporter importer = new GtfsImporter(
                pathInput,
                new NameChanger(new ArrayList<>(), new ArrayList<>()),
                false
        );
        importer.execute();

        DraftModel draft = importer.getModel();
        ModelData data = new DraftModelConverter().convert(draft);
        return data;
    }
}
