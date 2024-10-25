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
package com.vml.aemrules.java.checks.resourceresolver.close;

import com.vml.aemrules.version.AemVersion;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.AssignmentExpressionTree;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.LambdaExpressionTree;
import org.sonar.plugins.java.api.tree.TryStatementTree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.ArrayList;
import java.util.List;

@Rule(key = ResourceResolverTryWithResourcesCheck.RULE_KEY)
@AemVersion(all = true)
public class ResourceResolverTryWithResourcesCheck extends BaseTreeVisitor implements
        JavaFileScanner {

    public static final String RULE_KEY = "AEM-20";

    protected static final String RULE_MESSAGE = "ResourceResolver can be closed using try-with-resources Java 7 feature.";

    private static final String SLING_RESOURCE_RESOLVER = "org.apache.sling.api.resource.ResourceResolver";
    private final List<String> resourceResolversInTryWithResources = new ArrayList<>();
    private boolean insideTryStatement = false;
    private boolean insideLambdaExpression = false;
    private JavaFileScannerContext context;

    public void scanFile(JavaFileScannerContext javaFileScannerContext) {
        context = javaFileScannerContext;
        scan(context.getTree());
    }

    @Override
    public void visitVariable(VariableTree tree) {
        if (isResourceResolverUsedProperly(tree)) {
            context.reportIssue(this, tree, RULE_MESSAGE);
        }
        super.visitVariable(tree);
    }

    @Override
    public void visitAssignmentExpression(AssignmentExpressionTree tree) {
        if (insideTryStatement && !insideLambdaExpression && isResourceResolver(tree)) {
            context.reportIssue(this, tree, RULE_MESSAGE);
        }
        super.visitAssignmentExpression(tree);
    }

    @Override
    public void visitTryStatement(TryStatementTree tree) {
        insideTryStatement = true;
        tree.resourceList().stream()
                // We're iterating over a resource specification in a try-with-resource block
                // so we expect variable trees
                .filter(resource -> resource instanceof VariableTree)
                .map(VariableTree.class::cast)
                .map(resource -> resource.simpleName().name())
                .forEach(resourceResolversInTryWithResources::add);

        super.visitTryStatement(tree);
        resourceResolversInTryWithResources.clear();
        insideTryStatement = false;
    }

    @Override
    public void visitLambdaExpression(LambdaExpressionTree lambdaExpressionTree) {
        insideLambdaExpression = true;
        super.visitLambdaExpression(lambdaExpressionTree);
        insideLambdaExpression = false;
    }

    private boolean isResourceResolverUsedProperly(VariableTree tree) {
        return insideTryStatement && !insideLambdaExpression && isResourceResolver(tree)
                && !resourceResolversInTryWithResources
                .contains(getResourceResolver(tree));
    }

    private boolean isResourceResolver(AssignmentExpressionTree tree) {
        return SLING_RESOURCE_RESOLVER.equals(tree.variable().symbolType().fullyQualifiedName());
    }

    private boolean isResourceResolver(VariableTree tree) {
        return SLING_RESOURCE_RESOLVER.equals(tree.type().symbolType().fullyQualifiedName());
    }

    private String getResourceResolver(VariableTree tree) {
        return tree.simpleName().name();
    }
}
