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
package com.vml.aemrules.java.checks;

import com.vml.aemrules.matcher.MethodMatcher;
import com.vml.aemrules.matcher.MethodNamePredicate;
import com.vml.aemrules.matcher.OwnerTypePredicate;
import com.vml.aemrules.matcher.ParameterTypePredicate;
import com.vml.aemrules.metadata.Metadata;
import com.vml.aemrules.tag.Tags;
import com.vml.aemrules.version.AemVersion;
import org.sonar.check.Priority;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = AdministrativeAccessUsageCheck.RULE_KEY)
@AemVersion(from = "6.0")
public class AdministrativeAccessUsageCheck extends IssuableSubscriptionVisitor {

    public static final String RULE_KEY = "AEM-11";

    public static final String RULE_MESSAGE = "Do not use deprecated administrative access methods";

    private static final Map<String, String> SUBSTITUTES = Stream.of(
            new AbstractMap.SimpleImmutableEntry<>("loginAdministrative", "loginService"),
            new AbstractMap.SimpleImmutableEntry<>("getAdministrativeResourceResolver", "getServiceResourceResolver")
    ).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    private static final List<MethodMatcher> matchers = Arrays.asList(
            MethodMatcher.create(
                    MethodNamePredicate.is("loginAdministrative"),
                    OwnerTypePredicate.is("org.apache.sling.jcr.api.SlingRepository"),
                    ParameterTypePredicate.is("java.lang.String")),
            MethodMatcher.create(
                    MethodNamePredicate.is("getAdministrativeResourceResolver"),
                    OwnerTypePredicate.is("org.apache.sling.api.resource.ResourceResolverFactory"),
                    ParameterTypePredicate.is("java.util.Map"))
    );

    @Override
    public List<Kind> nodesToVisit() {
        return List.of(Kind.METHOD_INVOCATION);
    }

    @Override
    public void visitNode(Tree tree) {
        matchers.forEach(invocationMatcher -> this.checkInvocation(tree, invocationMatcher));
    }

    private void checkInvocation(Tree tree, MethodMatcher invocationMatcher) {
        if (tree.is(Kind.METHOD_INVOCATION)) {
            MethodInvocationTree methodInvocationTree = (MethodInvocationTree) tree;
            if (invocationMatcher.matches(methodInvocationTree)) {
                this.onMethodInvocationFound(methodInvocationTree);
            }
        }
    }

    private void onMethodInvocationFound(MethodInvocationTree mit) {
        String method = ((MemberSelectExpressionTree) mit.methodSelect()).identifier().name();
        context.reportIssue(this, mit, String.format("Method '%s' is deprecated. Use '%s' instead.", method, SUBSTITUTES.get(method)));
    }
}
