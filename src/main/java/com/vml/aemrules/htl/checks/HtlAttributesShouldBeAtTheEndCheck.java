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

import com.vml.aemrules.metadata.Metadata;
import com.vml.aemrules.tag.Tags;
import com.vml.aemrules.utils.Comparables;
import com.vml.aemrules.version.AemVersion;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.sling.scripting.sightly.compiler.expression.Expression;
import org.apache.sling.scripting.sightly.impl.compiler.Syntax;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.html.node.Attribute;
import org.sonar.plugins.html.node.TagNode;

import java.util.List;
import java.util.stream.Collectors;

@Rule(
        key = HtlAttributesShouldBeAtTheEndCheck.RULE_KEY,
        name = HtlAttributesShouldBeAtTheEndCheck.RULE_MESSAGE,
        priority = Priority.MINOR,
        tags = Tags.AEM
)
@AemVersion(
        from = "6.0"
)
@Metadata(
        technicalDebt = "5min"
)
public class HtlAttributesShouldBeAtTheEndCheck extends AbstractHtlCheck {

    public static final String RULE_KEY = "HTL-1";

    static final String RULE_MESSAGE = "Always place HTL attributes at the end";

    private static boolean isOrdered(Iterable<Integer> list) {
        return Comparables.isOrdered(list);
    }

    @Override
    public void startHtlElement(List<Expression> expressions, TagNode node) {
        Boolean hasAttributesInWrongOrder = node.getAttributes().stream()
                .map(Attribute::getName)
                .map(Syntax::isPluginAttribute)
                .mapToInt(value -> BooleanUtils.isTrue(value) ? 1 : 0)
                .boxed()
                .collect(Collectors.collectingAndThen(Collectors.toList(), listOfAttributes -> !isOrdered(listOfAttributes)));
        if (Boolean.TRUE.equals(hasAttributesInWrongOrder)) {
            createViolation(node.getStartLinePosition(), "Move HTL Attributes to the end of the tag");
        }
        super.startHtlElement(expressions, node);
    }
}
