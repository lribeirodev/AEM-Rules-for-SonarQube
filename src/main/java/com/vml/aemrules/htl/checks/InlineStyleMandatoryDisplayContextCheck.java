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
import org.sonar.plugins.html.node.Attribute;
import org.sonar.plugins.html.node.TagNode;

@Rule(key = InlineStyleMandatoryDisplayContextCheck.RULE_KEY)
@AemVersion(from = "6.0")
public class InlineStyleMandatoryDisplayContextCheck extends AbstractHtlCheck {

    public static final String RULE_KEY = "HTL-9";

    public static final String RULE_MESSAGE = "For inline styles display context is mandatory";

    private static final String VIOLATION_MESSAGE = "Please define display context";

    private static final String STYLE_ATTRIBUTE = "style";

    @Override
    public void startElement(TagNode node) {
        node.getAttributes()
                .stream()
                .filter(attribute -> StringUtils.equals(attribute.getName(), STYLE_ATTRIBUTE))
                .filter(this::notContainsDisplayContext)
                .forEach(attribute -> createViolation(node.getStartLinePosition(), VIOLATION_MESSAGE));
    }

    private boolean notContainsDisplayContext(Attribute attribute) {
        return getExpressions(attribute.getValue()).stream()
                .anyMatch(expression -> !expression.containsOption(Syntax.CONTEXT_OPTION));
    }

}
