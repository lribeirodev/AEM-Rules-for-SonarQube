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
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.scripting.sightly.impl.compiler.Syntax;
import org.sonar.check.Rule;
import org.sonar.plugins.html.node.CommentNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Rule(key = HtlCommentsCheck.RULE_KEY)
@AemVersion(all = true)
public class HtlCommentsCheck extends AbstractHtlCheck {

    static final String RULE_KEY = "HTL-5";

    static final String RULE_MESSAGE = "Always use HTL style of comments";

    private static final List<String> SSI_ELEMENTS = Arrays.asList("comment", "config", "echo", "exec", "fsize", "flastmod",
            "include", "printenv", "set");

    private static final Pattern CONDITIONAL_COMMENT_PATTERN = Pattern
            .compile("<!--\\[.*]-->");

    private static final Pattern ESI_COMMENT_PATTERN = Pattern
            .compile("<!--esi.*-->");

    private static final List<Pattern> EXCEPTIONS = buildExceptionList();

    private static List<Pattern> buildExceptionList() {
        List<Pattern> result = new ArrayList<>();
        result.add(CONDITIONAL_COMMENT_PATTERN);
        result.add(ESI_COMMENT_PATTERN);
        result.addAll(buildSsiRegex(SSI_ELEMENTS));

        return result;
    }

    private static List<Pattern> buildSsiRegex(List<String> elements) {
        return elements.stream()
                .map(e -> "<!--#" + e + ".*-->")
                .map(Pattern::compile)
                .collect(Collectors.toList());
    }

    @Override
    public void comment(CommentNode node) {
        String code = StringUtils.deleteWhitespace(node.getCode());
        if (!Syntax.isSightlyComment(code) && !isException(code)) {
            createViolation(node.getStartLinePosition(), RULE_MESSAGE);
        }
    }

    private boolean isException(String code) {
        return EXCEPTIONS.stream()
                .map(pattern -> pattern.matcher(code))
                .anyMatch(Matcher::matches);
    }
}