/*-
 * #%L
 * AEM Rules for SonarQube
 * %%
 * Copyright (C) 2015-2024 VML
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.vml.aemrules.htl.checks;

import com.vml.aemrules.version.AemVersion;
import org.sonar.check.Rule;
import org.sonar.plugins.html.node.TagNode;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = DefaultDisplayContextCheck.RULE_KEY)
@AemVersion(from = "6.0")
public class DefaultDisplayContextCheck extends AbstractHtlCheck {

    public static final String RULE_KEY = "HTL-6";

    public static final String RULE_MESSAGE = "HTL uses uri display context as default for src, poster, manifest, href, formaction, data, cite, action attributes";

    private static final Pattern CONTEXT_URI_DEFINITION = Pattern.compile("context=['\"]uri['\"]");

    private static final String VIOLATION_MESSAGE = "Explicitly using default display context, please remove display context from expression";

    private static final Map<String, List<String>> TAG_ATTRIBUTE_MAPPING = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>("a", List.of("href")),
            new AbstractMap.SimpleImmutableEntry<>("area", List.of("href")),
            new AbstractMap.SimpleImmutableEntry<>("audio", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("base", List.of("href")),
            new AbstractMap.SimpleImmutableEntry<>("blockquote", List.of("cite")),
            new AbstractMap.SimpleImmutableEntry<>("button", List.of("formaction")),
            new AbstractMap.SimpleImmutableEntry<>("del", List.of("cite")),
            new AbstractMap.SimpleImmutableEntry<>("embed", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("form", List.of("action")),
            new AbstractMap.SimpleImmutableEntry<>("html", List.of("manifest")),
            new AbstractMap.SimpleImmutableEntry<>("img", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("ins", List.of("cite")),
            new AbstractMap.SimpleImmutableEntry<>("input", Arrays.asList("formaction", "src")),
            new AbstractMap.SimpleImmutableEntry<>("iframe", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("link", List.of("href")),
            new AbstractMap.SimpleImmutableEntry<>("q", List.of("cite")),
            new AbstractMap.SimpleImmutableEntry<>("object", List.of("data")),
            new AbstractMap.SimpleImmutableEntry<>("video", List.of("poster")),
            new AbstractMap.SimpleImmutableEntry<>("script", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("source", List.of("src")),
            new AbstractMap.SimpleImmutableEntry<>("track", List.of("src"))
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    @Override
    public void startElement(TagNode node) {
        String nodeName = node.getNodeName();
        if (TAG_ATTRIBUTE_MAPPING.containsKey(nodeName)) {
            Collection<String> supportedAttributes = TAG_ATTRIBUTE_MAPPING.get(nodeName);
            node.getAttributes().stream()
                    .filter(attribute -> supportedAttributes.contains(attribute.getName()))
                    .filter(a -> CONTEXT_URI_DEFINITION.matcher(a.getValue()).find())
                    .forEach(a -> createViolation(node.getStartLinePosition(), VIOLATION_MESSAGE));
        }
    }
}
