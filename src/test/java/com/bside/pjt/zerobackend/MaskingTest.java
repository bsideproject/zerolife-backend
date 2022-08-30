package com.bside.pjt.zerobackend;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

public class MaskingTest {
    private static final List<Pattern> MASKING_FIELD_NAME_PATTERNS = Arrays.asList(
        Pattern.compile("(\"password\" *: *)(\".*\")"),
        Pattern.compile("(\"proofImageUrl\" *: *)(\".*\")")
    );

    @Test
    public void maskingTest() {
        StringBuilder testBuilder = new StringBuilder();
        String target = "{\n"
            + "\"password\": \"qwertyui\"\n"
            + "\"proofImageUrl\": \"alikfjeoijalk;!@jfo(*& aiwejflkajefoiawejflkaejfoaiejf\"\n"
            + "}";
        convert(target, testBuilder);
        System.out.println(testBuilder);
    }

    private static void convert(String contentString, StringBuilder msg) {
        Stream.of(contentString.split("\r\n|\r|\n")).forEach(line -> {
            if (line.contains("\"password\"") || line.contains("\"proofImageUrl\"")) {
                line = mask(line);
            }
            msg.append(line).append("\n");
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
                        .append(matcher.group(2).replaceAll("[\\w` ~!@#$%^&*()-_=+{};:,.<>/?]+", "*".repeat(8)));
                },
                () -> {
                    output.append(target);
                }
            );

        return output.toString();
    }
}
