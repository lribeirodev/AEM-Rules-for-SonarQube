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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SessionShouldBeLoggedOutTest extends AbstractBaseTest {

    private final boolean expectFailure;

    public SessionShouldBeLoggedOutTest(Object fn, Object expectFailure) {
        filename = (String) fn;
        this.expectFailure = ((Boolean) expectFailure).booleanValue();
    }

    // format is filename, failure expected
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"src/test/files/java/SessionLogoutOne.java", false},
                {"src/test/files/java/SessionLogoutTwo.java", false},
                {"src/test/files/java/SessionLogoutThree.java", false},
                {"src/test/files/java/SessionLogoutFour.java", true},
                {"src/test/files/java/SessionLogoutFive.java", true},
                {"src/test/files/java/SessionLogoutSix.java", true},
                {"src/test/files/java/SessionLogoutSeven.java", true},
                {"src/test/files/java/SessionLogoutEight.java", false},
                {"src/test/files/java/LongSessionEventListener.java", false},
                {"src/test/files/java/LongSessionEventListenerError.java", true}
        });
    }

    @Test
    public void checkInjectorNotClosedInFinallyBlock() {
        check = new SessionShouldBeLoggedOut();
        if (expectFailure) {
            verify();
        } else {
            verifyNoIssues();
        }
    }

}
