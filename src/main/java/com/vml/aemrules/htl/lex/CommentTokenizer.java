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
package com.vml.aemrules.htl.lex;


import org.sonar.plugins.html.node.CommentNode;
import org.sonar.plugins.html.node.Node;
import org.sonar.sslr.channel.CodeReader;
import org.sonar.sslr.channel.EndMatcher;

import java.util.List;

class CommentTokenizer<T extends List<Node>> extends AbstractTokenizer<T> {

    private final Boolean isHtml;
    private final char[] endChars;

    public CommentTokenizer(String startToken, String endToken, Boolean isHtml) {
        super(startToken, endToken);
        this.isHtml = isHtml;
        this.endChars = endToken.toCharArray();
    }

    @Override
    protected EndMatcher getEndMatcher(CodeReader codeReader) {
        return new EndTokenMatcher(codeReader);
    }

    @Override
    Node createNode() {
        CommentNode node = new CommentNode();
        node.setHtml(isHtml);
        return node;
    }

    private final class EndTokenMatcher implements EndMatcher {

        private final CodeReader codeReader;

        private EndTokenMatcher(CodeReader codeReader) {
            this.codeReader = codeReader;
        }

        @Override
        public boolean match(int endFlag) {
            return equalsIgnoreCase(codeReader.peek(endChars.length), endChars);
        }

    }
}