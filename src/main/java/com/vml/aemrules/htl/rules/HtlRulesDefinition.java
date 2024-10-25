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
package com.vml.aemrules.htl.rules;

import com.vml.aemrules.htl.Htl;
import com.vml.aemrules.rules.RulesLoader;
import org.sonar.api.server.rule.RulesDefinition;

public class HtlRulesDefinition implements RulesDefinition {

    private static final RulesLoader rulesLoader = new RulesLoader();

    @Override
    public void define(Context context) {
        NewRepository repo = context
                .createRepository(HtlRulesList.REPOSITORY_KEY, Htl.KEY)
                .setName(HtlRulesList.REPOSITORY_NAME);
        rulesLoader.load(repo, HtlRulesList.getCheckClasses());
        repo.done();
    }
}
