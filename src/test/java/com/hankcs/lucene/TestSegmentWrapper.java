package com.hankcs.lucene;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;
import org.junit.Test;

import java.io.StringReader;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description:
 * @author: Kenn
 * @create: 2019-07-25 16:35
 */
public class TestSegmentWrapper {

    @Test
    public void test1() {
        StringReader reader = new StringReader("张三\n\n\n新买的手机");
        SegmentWrapper wrapper = new SegmentWrapper(reader, HanLP.newSegment().enableOffset(true));
        while (true) {
            Term term = wrapper.next();
            if (term == null) {
                break;
            }
            System.out.println(term.word + "\t" + term.nature + "\t" + term.offset + "\t" + term.length());
        }
    }
}
