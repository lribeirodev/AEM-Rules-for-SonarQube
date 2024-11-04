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
package com.vml.aemrules.java.rules;

import com.vml.aemrules.version.AemVersion;
import com.vml.aemrules.version.VersionSupportChecker;
import org.sonar.api.config.Configuration;
import org.sonar.plugins.java.api.CheckRegistrar;
import org.sonar.plugins.java.api.JavaCheck;
import org.sonarsource.api.sonarlint.SonarLintSide;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.vml.aemrules.java.Constants.REPOSITORY_KEY;

@SonarLintSide
public class JavaCheckRegistrar implements CheckRegistrar {

    private final String aemVersion;

    public JavaCheckRegistrar(Configuration configuration) {
        this.aemVersion = configuration.get(VersionSupportChecker.VERSION_PROPERTY)
                .orElse(VersionSupportChecker.DEFAULT_AEM_VERSION);
    }

    @Override
    public void register(RegistrarContext registrarContext) {
        registrarContext.registerClassesForRepository(REPOSITORY_KEY, checkClasses(), testCheckClasses());
    }

    private List<Class<? extends JavaCheck>> checkClasses() {
        return JavaRulesList.getJavaChecks().stream()
                .filter(checkClass -> shouldRegister(aemVersion, checkClass))
                .collect(Collectors.toList());
    }

    private List<Class<? extends JavaCheck>> testCheckClasses() {
        return JavaRulesList.getJavaTestChecks().stream()
                .filter(checkClass -> shouldRegister(aemVersion, checkClass))
                .collect(Collectors.toList());
    }

    private boolean shouldRegister(String aemVersion, Class<? extends JavaCheck> checkClass) {
        return Optional.ofNullable(checkClass.getAnnotation(AemVersion.class))
                .map(supportedVersion -> VersionSupportChecker.create(supportedVersion).supports(aemVersion))
                .orElse(true);
    }
}
