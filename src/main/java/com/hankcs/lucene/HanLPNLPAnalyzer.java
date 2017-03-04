package com.hankcs.lucene;

import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Tokenizer;

import com.hankcs.hanlp.HanLP;

/**
 * NLP分析器
 * @author Kenn
 *
 */
public class HanLPNLPAnalyzer extends Analyzer {

	private boolean enablePorterStemming;
	private Set<String> filter;

	/**
	 * @param filter 停用词
	 * @param enablePorterStemming 是否分析词干（仅限英文）
	 */
	public HanLPNLPAnalyzer(Set<String> filter, boolean enablePorterStemming) {
		this.filter = filter;
		this.enablePorterStemming = enablePorterStemming;
	}

	/**
	 * @param enablePorterStemming 是否分析词干.进行单复数,时态的转换
	 */
	public HanLPNLPAnalyzer(boolean enablePorterStemming) {
		this.enablePorterStemming = enablePorterStemming;
	}

	public HanLPNLPAnalyzer() {
		super();
	}

	/**
	 * 重载Analyzer接口，构造分词组件
	 */
	@Override
	protected TokenStreamComponents createComponents(String fieldName) {
		Tokenizer tokenizer = new HanLPTokenizer(HanLP.newSegment().enableNameRecognize(true).enableTranslatedNameRecognize(true)
	            .enableJapaneseNameRecognize(true).enablePlaceRecognize(true).enableOrganizationRecognize(true)
	            .enablePartOfSpeechTagging(true), filter, enablePorterStemming);
		return new TokenStreamComponents(tokenizer);
	}

}
