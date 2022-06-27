package com.corbandalas.shelterbot.utils;

import com.corbandalas.shelterbot.Application;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class ShelterBotUtils {

    public static List<String> getDirectoryFileNames(String path) throws URISyntaxException, IOException {
        URI uri = Application.class.getResource(path).toURI();
        Path dirPath = Paths.get(uri);

        return Files.list(dirPath).map(p -> p.getFileName().toString()).collect(Collectors.toList());
    }

    public static Double haversineDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        final double R = 6371.0088; // Radius of the earth
        Double latDistance = toRad(lat2 - lat1);
        Double lonDistance = toRad(lng2 - lng1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    public static Double toRad(Double value) {
        return value * Math.PI / 180;
    }

    public static Set<Class<?>> getAnnotatedClasses(String packageName, Class<? extends Annotation> annotatedClass) {

        Reflections reflectionPaths = new Reflections(packageName);

        return reflectionPaths.getTypesAnnotatedWith(annotatedClass, true);


    }
}
