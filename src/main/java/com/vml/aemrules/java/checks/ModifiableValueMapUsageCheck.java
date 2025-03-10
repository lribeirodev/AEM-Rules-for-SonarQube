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

import com.vml.aemrules.version.AemVersion;
import org.sonar.check.Rule;
import org.sonar.plugins.java.api.JavaFileScanner;
import org.sonar.plugins.java.api.JavaFileScannerContext;
import org.sonar.plugins.java.api.tree.Arguments;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.IdentifierTree;
import org.sonar.plugins.java.api.tree.MemberSelectExpressionTree;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.VariableTree;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Rule(key = ModifiableValueMapUsageCheck.RULE_KEY)
@AemVersion(all = true)
public class ModifiableValueMapUsageCheck extends BaseTreeVisitor implements JavaFileScanner {

    protected static final String RULE_KEY = "AEM-17";

    protected static final String RULE_MESSAGE = "No mutator methods invoked on ModifiableValueMap";

    private static final String MODIFIABLE_VALUE_MAP_FULL_NAME = "org.apache.sling.api.resource.ModifiableValueMap";

    private static final Set<String> MUTABLE_METHODS = Stream.of(
            "put",
            "putAll",
            "remove"
    ).collect(Collectors.toSet());

    private JavaFileScannerContext context;

    private boolean isModified;

    @Override
    public void scanFile(JavaFileScannerContext context) {
        this.context = context;
        scan(context.getTree());
    }

    @Override
    public void visitVariable(VariableTree tree) {
        if (MODIFIABLE_VALUE_MAP_FULL_NAME.equals(tree.type().symbolType().fullyQualifiedName())) {
            isModified = false;
            List<IdentifierTree> usagesOfMVM = tree.symbol().usages();
            checkIfMapVariableIsModified(usagesOfMVM);
            if (!isModified) {
                context.reportIssue(this, tree, RULE_MESSAGE);
            }
            super.visitVariable(tree);
        }
    }

    private void checkIfMapVariableIsModified(List<IdentifierTree> usagesOfMVM) {
        for (IdentifierTree modifiableValueMapUsageIdentifier : usagesOfMVM) {
            Tree usageOfMVM = modifiableValueMapUsageIdentifier.parent();
            if (usageOfMVM != null) {
                if (usageOfMVM.is(Tree.Kind.ARGUMENTS)) {
                    visitMethodWithMVM(modifiableValueMapUsageIdentifier, usageOfMVM);
                } else if (usageOfMVM.is(Tree.Kind.MEMBER_SELECT) && isSomeoneCallingMutableMethodsOnMap((MemberSelectExpressionTree) usageOfMVM)) {
                    isModified = true;
                    break;
                }
            }
        }
    }

    private boolean isSomeoneCallingMutableMethodsOnMap(MemberSelectExpressionTree usageOfMVM) {
        return MUTABLE_METHODS.contains(usageOfMVM.identifier().name());
    }

    private void visitMethodWithMVM(IdentifierTree modifiableValueMapUsageIdentifier, Tree usageOfMVM) {
        int argumentNumber = ((Arguments) usageOfMVM).indexOf(modifiableValueMapUsageIdentifier);
        MethodInvocationTree methodInvocationWithMVM = (MethodInvocationTree) usageOfMVM.parent();
        if (methodInvocationWithMVM != null) {
            MethodTree methodWithMVM = methodInvocationWithMVM.methodSymbol().declaration();
            if (methodWithMVM != null && methodWithMVM.is(Tree.Kind.METHOD)) {
                MethodWithMVMVisitor methodWithMVMVisitor = new MethodWithMVMVisitor(this, argumentNumber);
                methodWithMVM.accept(methodWithMVMVisitor);
            }
        }
    }

    private class MethodWithMVMVisitor extends BaseTreeVisitor {

        private final ModifiableValueMapUsageCheck scanner;

        private final int argumentIndex;

        MethodWithMVMVisitor(ModifiableValueMapUsageCheck scanner, int argumentIndex) {
            this.scanner = scanner;
            this.argumentIndex = argumentIndex;
        }

        @Override
        public void visitMethod(MethodTree tree) {
            List<VariableTree> parameters = tree.parameters();
            if (argumentIndex >= 0 && argumentIndex <= (parameters.size() - 1)) {
                VariableTree variableTree = parameters.get(argumentIndex);
                scanner.checkIfMapVariableIsModified(variableTree.symbol().usages());
            }
            super.visitMethod(tree);
        }

    }
}
