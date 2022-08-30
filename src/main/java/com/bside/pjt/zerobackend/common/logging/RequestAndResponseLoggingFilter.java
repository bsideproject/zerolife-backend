package com.bside.pjt.zerobackend.common.logging;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Component
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {
    protected static final Logger log = LoggerFactory.getLogger(RequestAndResponseLoggingFilter.class);

    private static final List<Pattern> MASKING_FIELD_NAME_PATTERNS = Arrays.asList(
        Pattern.compile("(\"password\" *: *)(\"[\\w|!@#$%^&*()]+\")"),
        Pattern.compile("(\"proofImageUrl\" *: *)(\"[\\w|!@#$%^&*()]+\")")
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(
                new ContentCachingRequestWrapper(request),
                new ContentCachingResponseWrapper(response),
                filterChain
            );
        }
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response, FilterChain filterChain)
        throws ServletException, IOException {
        try {
            logRequest(request);
            filterChain.doFilter(request, response);
        } finally {
            logResponse(response);
            response.copyBodyToResponse();
        }
    }

    private static void logRequest(ContentCachingRequestWrapper request) throws UnsupportedEncodingException {
        log.info("---------- REQUEST ----------");
        final String method = request.getMethod();
        final String queryParameters = request.getQueryString();
        final String uri = queryParameters == null ? request.getRequestURI() : request.getRequestURI() + queryParameters;
        log.info(String.format(">>> %s %s", method, uri));

        final byte[] content = request.getContentAsByteArray();
        final String encoding = request.getCharacterEncoding();
        if (content.length > 0) {
            log.info(String.format(">>> content-type : %s", request.getContentType()));

            final StringBuilder body = new StringBuilder();
            convert(encoding, body);
            log.info(String.format(">>> content : %s", body));
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response) {
        log.info("---------- RESPONSE ----------");
        final int status = response.getStatus();
        log.info(String.format(">>> status : %d %s", status, HttpStatus.valueOf(status).getReasonPhrase()));

        final byte[] content = response.getContentAsByteArray();
        if (content.length > 0) {
            log.info(String.format(">>> content : %s", new String(content, StandardCharsets.UTF_8)));
        }
    }

    private static void convert(String contentString, StringBuilder builder) {
        Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> {
            if (line.contains("\"password\"") || line.contains("\"proofImageUrl\"")) {
                line = mask(line);
            }
            builder.append(line).append("\n");
        });
    }

    private static String mask(final String target) {
        final var output = new StringBuilder();
        MASKING_FIELD_NAME_PATTERNS.stream()
            .map(pattern -> pattern.matcher(target))
            .filter(Matcher::find)
            .findFirst()
            .ifPresentOrElse(
                matcher -> {
                    output.append(target, 0, matcher.start())
                        .append(matcher.group(1))
                        .append(matcher.group(2).replaceAll("[\\w`~!@#$%^&*()-_=+{};:,.<>/?]+", "*".repeat(8)));
                },
                () -> {
                    output.append(target);
                }
            );

        return output.toString();
    }
}
