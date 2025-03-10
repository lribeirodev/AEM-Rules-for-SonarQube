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
package com.vml.aemrules.htl.checks;

import com.vml.aemrules.htl.AbstractBaseTest;
import org.junit.jupiter.api.Test;

class DisplayContextInConditionalCommentsCheckTest extends AbstractBaseTest {

    @Test
    void checkContextInConditionalComments() {
        this.check = new DisplayContextInConditionalCommentsCheck();
        this.filename = "src/test/files/htl/DisplayContextInConditionalCommentsCheck.html";
        verify();
    }
}
