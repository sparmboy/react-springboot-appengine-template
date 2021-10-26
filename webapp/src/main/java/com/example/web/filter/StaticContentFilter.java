package com.example.web.filter;

import com.example.config.WebSocketConfig;
import com.google.api.client.util.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Allows you to run a spring REST API whilst also hosting
 * static react pages that also contain routing to pages
 * within the app.
 */
@Component
public class StaticContentFilter implements Filter {

    private List<String> fileExtensions = Arrays.asList("html", "js", "json", "csv", "css", "png", "svg", "eot", "ttf", "woff", "appcache", "jpg", "jpeg", "gif", "ico");

    /**
     * This is the list of all route prefixes within your react routing
     * that a user might directly enter as a URL
     */
    private List<String> ROUTES = Arrays.asList("/lamps/");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String path = stripRoutes(request.getServletPath());

        boolean isApi = path.startsWith("/api");
        boolean isSocket = path.startsWith(WebSocketConfig.TOPIC_PREFIX) || path.startsWith(WebSocketConfig.TOPIC_SESSION);
        boolean isResourceFile = !isApi && fileExtensions.stream().anyMatch(path::contains);

        if (isApi || isSocket ) {
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
        InputStream inputStream = new ClassPathResource(resourcePath).getInputStream();


        if (inputStream == null) {
            response.sendError(NOT_FOUND.value(), NOT_FOUND.getReasonPhrase());
            return;
        }
        response.setHeader("Content-Type", fileExtensionToMimeType(resourcePath.substring(resourcePath.lastIndexOf(".")+1)));
        IOUtils.copy(inputStream,response.getOutputStream());
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

        }
        throw new RuntimeException("File extenstion '." + extension + "' not supported");
    }
}