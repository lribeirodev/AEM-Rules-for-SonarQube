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

import com.vml.aemrules.java.checks.AdministrativeAccessUsageCheck;
import com.vml.aemrules.java.checks.AnnotationsConstantsCheck;
import com.vml.aemrules.java.checks.ConstantsCheck;
import com.vml.aemrules.java.checks.ContentResourceShouldBeNullCheckedCheck;
import com.vml.aemrules.java.checks.ModifiableValueMapUsageCheck;
import com.vml.aemrules.java.checks.PreferSlingServletAnnotation;
import com.vml.aemrules.java.checks.SessionShouldBeLoggedOut;
import com.vml.aemrules.java.checks.SynchronizedKeywordUsageCheck;
import com.vml.aemrules.java.checks.ThreadSafeFieldCheck;
import com.vml.aemrules.java.checks.resourceresolver.close.ResourceResolverShouldBeClosed;
import com.vml.aemrules.java.checks.resourceresolver.close.ResourceResolverTryWithResourcesCheck;
import com.vml.aemrules.java.checks.slingmodels.DefaultInjectionStrategyAnnotationCheck;
import com.vml.aemrules.java.checks.slingquery.SlingQueryImplicitStrategyCheck;
import org.sonar.plugins.java.api.JavaCheck;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavaRulesList {

    private JavaRulesList() {
        //private constructor
    }

    public static List<Class<? extends JavaCheck>> getChecks() {
        List<Class<? extends JavaCheck>> checks = new ArrayList<>();
        checks.addAll(getJavaChecks());
        checks.addAll(getJavaTestChecks());
        return Collections.unmodifiableList(checks);
    }

    public static List<Class<? extends JavaCheck>> getJavaChecks() {
        return List.of(AdministrativeAccessUsageCheck.class,
                AnnotationsConstantsCheck.class, ConstantsCheck.class,
                PreferSlingServletAnnotation.class,
                ResourceResolverShouldBeClosed.class,
                SessionShouldBeLoggedOut.class,
                SynchronizedKeywordUsageCheck.class,
                ThreadSafeFieldCheck.class,
                DefaultInjectionStrategyAnnotationCheck.class,
                ModifiableValueMapUsageCheck.class,
                ContentResourceShouldBeNullCheckedCheck.class,
                SlingQueryImplicitStrategyCheck.class,
                ResourceResolverTryWithResourcesCheck.class);
    }

    public static List<Class<? extends JavaCheck>> getJavaTestChecks() {
        return Collections.emptyList();
    }

}
