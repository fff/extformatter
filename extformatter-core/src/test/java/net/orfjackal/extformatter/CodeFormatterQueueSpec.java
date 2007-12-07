/*
 * External Code Formatter
 * Copyright (c) 2007 Esko Luontola, www.orfjackal.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orfjackal.extformatter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import static net.orfjackal.extformatter.TestResources.*;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
@RunWith(JDaveRunner.class)
public class CodeFormatterQueueSpec extends Specification<CodeFormatterQueue> {

    public class ACodeFormatterQueue {

        private CodeFormatter formatter;
        private CodeFormatterQueue queue;

        public CodeFormatterQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new CodeFormatterQueue(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(JAVA_FILE); will(returnValue(true));
                allowing(formatter).supportsFileType(XML_FILE); will(returnValue(false));
                allowing(formatter).supportsReformatFile(); will(returnValue(true));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(true));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return queue;
        }

        public void shouldNotCallTheFormatterImmediately() {
            checking(new Expectations() {{
            }});
            queue.reformatFile(FOO_FILE);
        }

        public void shouldCallTheFormatterWhenFlushed() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
            }});
            queue.reformatFile(FOO_FILE);
            queue.flush();
        }

        public void shouldExecuteAllQueuedCommandsWhenFlushed() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
                one(formatter).reformatFile(GAZONK_FILE);
            }});
            queue.reformatFile(FOO_FILE);
            queue.reformatFile(GAZONK_FILE);
            queue.flush();
        }

        public void shouldSupportTheSameOperationsAsTheFormatter() {
            specify(queue.supportsReformatFile(),
                    should.equal(formatter.supportsReformatFile()));
            specify(queue.supportsReformatFiles(),
                    should.equal(formatter.supportsReformatFiles()));
            specify(queue.supportsReformatFilesInDirectory(),
                    should.equal(formatter.supportsReformatFilesInDirectory()));
            specify(queue.supportsReformatFilesInDirectoryRecursively(),
                    should.equal(formatter.supportsReformatFilesInDirectoryRecursively()));
        }

        public void shouldSupportTheSameFileTypesAsTheFormatter() {
            specify(queue.supportsFileType(JAVA_FILE), should.equal(formatter.supportsFileType(JAVA_FILE)));
            specify(queue.supportsFileType(XML_FILE), should.equal(formatter.supportsFileType(XML_FILE)));
        }
    }


}