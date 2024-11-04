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


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PreferSlingServletAnnotationTest extends AbstractBaseTest {

    @ParameterizedTest
    @CsvSource({
            "checkLackOfAnnotation,src/test/files/java/SlingServletOne.java",
            "checkMixedAnnotations,src/test/files/java/SlingServletTwo.java",
            "checkRedundantProperties,src/test/files/java/SlingServletThree.java"
    })
    void detect(String description, String fileName) {
        System.out.println(description);
        this.check = new PreferSlingServletAnnotation();
        this.filename = fileName;
        verify();
    }

    @Test
    void checkStandardAnnotations() {
        this.check = new PreferSlingServletAnnotation();
        this.filename = "src/test/files/java/SlingServletWithStandardAnnotations.java";
        verifyNoIssues();
    }
}
