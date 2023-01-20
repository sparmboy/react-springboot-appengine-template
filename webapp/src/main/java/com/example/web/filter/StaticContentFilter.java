package com.example.web.filter;

import static com.example.config.SecurityConfig.SWAGGER_ROUTES;
import static com.example.config.WebSocketConfig.WEBSOCKETS_ENDPOINT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.example.config.WebSocketConfig;
import com.google.api.client.util.IOUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

/**
 * Allows you to run a spring REST API whilst also hosting
 * static react pages that also contain routing to pages
 * within the app.
 */
@Component
public class StaticContentFilter implements Filter {

    private List<String> fileExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff","'woff2", "appcache", "jpg", "jpeg", "gif", "ico");

    /**
     * This is the list of all route prefixes within your react routing
     * that a user might directly enter as a URL
     */
    private List<String> ROUTES = Collections.singletonList("/lamps/");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = stripRoutes(request.getServletPath());

        boolean isApi = path.startsWith("/api");
        boolean isSwagger = SWAGGER_ROUTES.stream().anyMatch(path::startsWith);
        boolean isSocket = path.startsWith(WEBSOCKETS_ENDPOINT) || path.startsWith(WebSocketConfig.TOPIC_PREFIX) || path.startsWith(WebSocketConfig.TOPIC_MY_EVENT);
        boolean isResourceFile = !isApi && fileExtensions.stream().anyMatch(path::contains);

        if (isApi || isSocket || isSwagger) {
            chain.doFilter(request, response);
        } else if (isResourceFile) {
            resourceToResponse("" + path, response);
        } else {
            resourceToResponse("index.html", response);
        }
    }

    private String stripRoutes(String servletPath) {
        for( String route : ROUTES) {
            if( servletPath.startsWith(route)) {
                servletPath = servletPath.substring(route.length());
            }
        }
        return servletPath;
    }


    private void resourceToResponse(String resourcePath, HttpServletResponse response) throws IOException {
        try {
            InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();
            response.setHeader("Content-Type", fileExtensionToMimeType(resourcePath.substring(resourcePath.lastIndexOf(".")+1)));
            IOUtils.copy(inputStream,response.getOutputStream());
        } catch (IOException e) {
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
        }
    }

    private String fileExtensionToMimeType(String extension) {
        switch (extension) {
            case "js": return "application/javascript";
            case "map": return "text/css";
            case "css": return "text/css";
            case "html": return "text/html";
            case "json": return "application/json";
            case "ico": return "image/x-icon";
            case "png": return "image/png";
            case "jpeg": return "image/jpeg";
            case "woff": return "text/css";
            case "woff2": return "text/css";

        }
        throw new RuntimeException("File extenstion '." + extension + "' not supported");
    }
}