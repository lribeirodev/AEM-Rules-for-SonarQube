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

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class SessionShouldBeLoggedOutTest extends AbstractBaseTest {

    @ParameterizedTest
    @CsvSource({
            "src/test/files/java/SessionLogoutOne.java,false",
            "src/test/files/java/SessionLogoutTwo.java,false",
            "src/test/files/java/SessionLogoutThree.java,false",
            "src/test/files/java/SessionLogoutFour.java,true",
            "src/test/files/java/SessionLogoutFive.java,true",
            "src/test/files/java/SessionLogoutSix.java,true",
            "src/test/files/java/SessionLogoutSeven.java,true",
            "src/test/files/java/SessionLogoutEight.java,false",
            "src/test/files/java/LongSessionEventListener.java,false",
            "src/test/files/java/LongSessionEventListenerError.java,true"
    })
    void checkInjectorNotClosedInFinallyBlock(String fileName, boolean expectFailure) {
        this.filename = fileName;
        this.check = new SessionShouldBeLoggedOut();
        if (expectFailure) {
            verify();
        } else {
            verifyNoIssues();
        }
    }
}
