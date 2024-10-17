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
package com.vml.aemrules.rules;

import com.vml.aemrules.htl.Htl;
import com.vml.aemrules.htl.rules.HtlCheckClasses;
import com.vml.aemrules.java.rules.JavaCheckClasses;
import org.sonar.api.server.rule.RulesDefinition;

public class AemRulesRulesDefinition implements RulesDefinition {

    private static final RulesLoader rulesLoader = new RulesLoader();

    @Override
    public void define(Context context) {
        defineJavaRepository(context);
        defineHtlRepository(context);
    }

    private void defineHtlRepository(Context context) {
        NewRepository repo = context
                .createRepository(HtlCheckClasses.REPOSITORY_KEY, Htl.KEY)
                .setName(HtlCheckClasses.REPOSITORY_NAME);
        rulesLoader.load(repo, HtlCheckClasses.getCheckClasses());
        repo.done();
    }

    private void defineJavaRepository(Context context) {
        NewRepository repo = context
                .createRepository(JavaCheckClasses.REPOSITORY_KEY, "java")
                .setName(JavaCheckClasses.REPOSITORY_NAME);
        rulesLoader.load(repo, JavaCheckClasses.CHECK_CLASSES);
        repo.done();
    }

}
