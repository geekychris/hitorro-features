/*
 * Copyright (c) 2006-2025 Chris Collins
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.hitorro.features.extractor;

/**
 * Something that implements this is used as a way to store state between extractors.  It is assumed that any extractor
 * within a thread may pluck a value or values from this and have stable execution. For example, one could imagine a
 * WordCountingResetableContext.  You preconstruct a key (which is an instance of the compContext, but which is not to be
 * used in extraction.  The constructed key could be for body or title, or perhaps both sets of wordcounts.  When any of
 * its get methods (unique words, total words, sentences, etc) is called, either it computes them all in advance, or it
 * computes them demand based.  But the assumption that this RC has the ability to share compContext between extractors.
 * <p/>
 * It further can be used to share compute costs for such things as parsing text.  ResetableContexts can make use of
 * other resetable contexts for this reason (imagine word count uses the compContext of "Inverter"
 */
public interface ResetableContext<T extends ResetableContext> {
    T getInstance(ExtractionContext ec);

    void reset();

    void deinit();
}
