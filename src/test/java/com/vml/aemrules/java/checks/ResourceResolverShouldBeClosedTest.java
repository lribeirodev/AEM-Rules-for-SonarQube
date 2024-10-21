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
import org.junit.Test;

public class ResourceResolverShouldBeClosedTest extends AbstractBaseTest {

    @Test
    public void checkInjectorNotClosedInFinallyBlock() {
        check = new ResourceResolverShouldBeClosed();
        filename = "src/test/resources/java/SampleServlet.java";
        verify();
    }

    @Test
    public void checkResourceResolverNotClosedInFinallyBlockWhenResourceResolverComesFromDifferentClass() {
        check = new ResourceResolverShouldBeClosed();
        filename = "src/test/resources/java/ResourceResolverConsumer.java";
        verifyNoIssues();
    }

    @Test
    public void checkResourceResolverNotClosedWhenItIsOpenedInActivateAndClosedInDeactivate() {
        check = new ResourceResolverShouldBeClosed();
        filename = "src/test/resources/java/LongSessionService.java";
        verifyNoIssues();
    }

    @Test
    public void checkResourceResolverClosedInDeactivateMethod() {
        check = new ResourceResolverShouldBeClosed();
        filename = "src/test/resources/java/LongSessionEventListener.java";
        verifyNoIssues();
    }

    @Test
    public void checkResourceResolverClosedInDeactivateMethodError() {
        check = new ResourceResolverShouldBeClosed();
        filename = "src/test/resources/java/LongResourceResolverEvenListenerError.java";
        verify();
    }

}
