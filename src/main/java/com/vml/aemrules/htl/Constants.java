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
package com.vml.aemrules.htl;

public final class Constants {

    public static final String REPOSITORY_KEY = "AEM-HTL";
    public static final String REPOSITORY_NAME = "AEM HTL";

    public static final String FILE_EXTENSIONS_PROP_KEY = "sonar.htl.file.suffixes";
    public static final String FILE_EXTENSIONS_DEF_VALUE = ".html";
    public static final String HTL_FILES_RELATIVE_PATHS_KEY = "sonar.htl.file.paths";
    public static final String HTL_FILES_RELATIVE_PATHS_DEF_VALUE = "src/main/content/jcr_root";

    private Constants() {
        //private constructor
    }

}
