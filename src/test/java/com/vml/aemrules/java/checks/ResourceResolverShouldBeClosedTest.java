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

import com.vml.aemrules.java.checks.resourceresolver.close.ResourceResolverShouldBeClosed;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ResourceResolverShouldBeClosedTest extends AbstractBaseTest {

    @ParameterizedTest
    @CsvSource({
            "checkInjectorNotClosedInFinallyBlock,src/test/files/java/SampleServlet.java",
            "checkResourceResolverClosedInDeactivateMethodError,src/test/files/java/LongResourceResolverEvenListenerError.java"
    })
    void detect_1(String description, String fileName) {
        System.out.println(description);
        this.check = new ResourceResolverShouldBeClosed();
        this.filename = fileName;
        verify();
    }

    @ParameterizedTest
    @CsvSource({
            "checkResourceResolverNotClosedInFinallyBlockWhenResourceResolverComesFromDifferentClass,src/test/files/java/ResourceResolverConsumer.java",
            "checkResourceResolverNotClosedWhenItIsOpenedInActivateAndClosedInDeactivate,src/test/files/java/LongSessionService.java",
            "checkResourceResolverClosedInDeactivateMethod,src/test/files/java/LongSessionEventListener.java"
    })
    void detect_2(String description, String fileName) {
        System.out.println(description);
        this.check = new ResourceResolverShouldBeClosed();
        this.filename = fileName;
        verifyNoIssues();
    }

}
