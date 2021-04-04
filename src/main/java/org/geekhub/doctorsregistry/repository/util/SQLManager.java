package org.geekhub.doctorsregistry.repository.util;

import org.geekhub.doctorsregistry.repository.InitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class SQLManager {

    private static final Logger logger = LoggerFactory.getLogger(SQLManager.class);
    private static final String FILE_EXTENSION = ".sql";

    private final Map<String, String> queries;
    private final String pattern;

    public SQLManager(String pattern) {
        this.queries = new HashMap<>();
        this.pattern = pattern;
    }

    @PostConstruct
    private void loadQueries() {
        ClassLoader classLoader = this.getClass().getClassLoader();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        try {
            Resource[] resources = resolver.getResources(pattern);
            for (Resource resource : resources) {
                try (
                    InputStream inputStream = resource.getInputStream();
                    InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader)
                ) {
                    String query = bufferedReader.lines().collect(Collectors.joining("\n"));
                    queries.put(resource.getFilename(), query);
                }
            }
        } catch (IOException e) {
            logger.error("Failed to load queries from resources", e);
            throw new InitializationException();
        }
    }

    public String getQuery(String fileName) {
        String fileNameWithExtension = fileName + FILE_EXTENSION;
        if (queries.containsKey(fileNameWithExtension)) {
            return queries.get(fileNameWithExtension);
        } else {
            throw new IllegalArgumentException("Could not find requested query");
        }
    }

}
