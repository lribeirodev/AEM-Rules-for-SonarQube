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

import java.util.regex.Pattern;

@Rule(key = DisplayContextInConditionalCommentsCheck.RULE_KEY)
@AemVersion(from = "6.0")
public class DisplayContextInConditionalCommentsCheck extends AbstractHtlCheck {

    public static final String RULE_KEY = "HTL-14";

    public static final String RULE_MESSAGE = "Please define markup context for HTML conditional comments otherwise automatically implied context will be 'comment'";

    private static final Pattern CONDITIONAL_COMMENT_PATTERN = Pattern.compile("<!--\\[if.*!\\[endif\\]-->");

    @Override
    public void comment(CommentNode node) {
        String code = StringUtils.deleteWhitespace(node.getCode());
        if (isConditionalComment(code)) {
            getExpressions(code).stream()
                    .filter(expression -> !expression.containsOption(Syntax.CONTEXT_OPTION))
                    .forEach(expression -> createViolation(node.getStartLinePosition(), RULE_MESSAGE));
        }

    }

    private boolean isConditionalComment(String code) {
        return CONDITIONAL_COMMENT_PATTERN.matcher(code).matches();
    }
}
