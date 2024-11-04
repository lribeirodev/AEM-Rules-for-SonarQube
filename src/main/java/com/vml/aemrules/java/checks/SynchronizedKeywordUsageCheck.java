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
import org.sonar.plugins.java.api.IssuableSubscriptionVisitor;
import org.sonar.plugins.java.api.tree.BaseTreeVisitor;
import org.sonar.plugins.java.api.tree.MethodTree;
import org.sonar.plugins.java.api.tree.Modifier;
import org.sonar.plugins.java.api.tree.ModifierKeywordTree;
import org.sonar.plugins.java.api.tree.Tree;
import org.sonar.plugins.java.api.tree.Tree.Kind;

import java.util.Arrays;
import java.util.List;

@Rule(key = SynchronizedKeywordUsageCheck.RULE_KEY)
@AemVersion(all = true)
public class SynchronizedKeywordUsageCheck extends IssuableSubscriptionVisitor {

    protected static final String RULE_KEY = "AEM-15";

    protected static final String MESSAGE = "Usage of 'synchronized' keyword should be avoided if possible.";

    private static final List<Kind> ACCEPTED_NODE_KINDS = Arrays.asList(Kind.SYNCHRONIZED_STATEMENT, Kind.METHOD);

    @Override
    public List<Kind> nodesToVisit() {
        return ACCEPTED_NODE_KINDS;
    }

    @Override
    public void visitNode(Tree tree) {
        Kind i = tree.kind();
        if (i == Kind.METHOD) {
            SynchronizedMethodVisitor visitor = new SynchronizedMethodVisitor(this);
            tree.accept(visitor);
        } else if (i == Kind.SYNCHRONIZED_STATEMENT) {
            reportIssue(tree, MESSAGE);
        }
        super.visitNode(tree);
    }

    private static class SynchronizedMethodVisitor extends BaseTreeVisitor {

        private final IssuableSubscriptionVisitor visitor;

        SynchronizedMethodVisitor(IssuableSubscriptionVisitor visitor) {
            this.visitor = visitor;
        }

        @Override
        public void visitMethod(MethodTree tree) {
            List<ModifierKeywordTree> modifiers = tree.modifiers().modifiers();
            for (ModifierKeywordTree modifier : modifiers) {
                if (modifier.modifier() == Modifier.SYNCHRONIZED) {
                    visitor.reportIssue(modifier, MESSAGE);
                }
            }
            super.visitMethod(tree);
        }
    }

}
