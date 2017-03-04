package org.elasticsearch.indices.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;
import org.elasticsearch.index.analysis.PreBuiltTokenizerFactoryFactory;
import org.elasticsearch.index.analysis.TokenizerFactory;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.CRF.CRFSegment;
import com.hankcs.hanlp.seg.Dijkstra.DijkstraSegment;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Other.DoubleArrayTrieSegment;
import com.hankcs.lucene.HanLPAnalyzer;
import com.hankcs.lucene.HanLPCRFAnalyzer;
import com.hankcs.lucene.HanLPDijkstraAnalyzer;
import com.hankcs.lucene.HanLPIndexAnalyzer;
import com.hankcs.lucene.HanLPNLPAnalyzer;
import com.hankcs.lucene.HanLPNShortAnalyzer;
import com.hankcs.lucene.HanLPSpeedAnalyzer;
import com.hankcs.lucene.HanLPStandardAnalyzer;
import com.hankcs.lucene.HanLPTokenizer;

public class HanLPIndicesAnalysis extends AbstractComponent {

	private boolean enablePorterStemming;

	@Inject
	public HanLPIndicesAnalysis(final Settings settings, IndicesAnalysisService indicesAnalysisService,
			Environment env) {
		super(settings);

		this.enablePorterStemming = settings.getAsBoolean("enablePorterStemming", false);

		// Hanlp Basic Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp",
				new PreBuiltAnalyzerProviderFactory("hanlp", AnalyzerScope.GLOBAL, new HanLPAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(HanLP.newSegment(), null, enablePorterStemming);
					}
				}));

		// Hanlp Standard Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_standard", new PreBuiltAnalyzerProviderFactory(
				"hanlp_standard", AnalyzerScope.GLOBAL, new HanLPStandardAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_standard",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_standard";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(HanLP.newSegment(), null, enablePorterStemming);
					}
				}));

		// Hanlp Index Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_index",
				new PreBuiltAnalyzerProviderFactory("hanlp_index", AnalyzerScope.GLOBAL, new HanLPIndexAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_index",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_index";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(HanLP.newSegment().enableIndexMode(true), null, enablePorterStemming);
					}
				}));

		// Hanlp NLP Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_nlp",
				new PreBuiltAnalyzerProviderFactory("hanlp_nlp", AnalyzerScope.GLOBAL, new HanLPNLPAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_nlp",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_nlp";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(
								HanLP.newSegment().enableNameRecognize(true).enableTranslatedNameRecognize(true)
										.enableJapaneseNameRecognize(true).enablePlaceRecognize(true)
										.enableOrganizationRecognize(true).enablePartOfSpeechTagging(true),
								null, enablePorterStemming);
					}
				}));

		// Hanlp N-Short Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_n_short",
				new PreBuiltAnalyzerProviderFactory("hanlp_n_short", AnalyzerScope.GLOBAL, new HanLPNShortAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_n_short",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_n_short";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(new NShortSegment().enableCustomDictionary(false)
								.enablePlaceRecognize(true).enableOrganizationRecognize(true), null,
								enablePorterStemming);
					}
				}));

		// Hanlp Dijkstra Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_dijkstra", new PreBuiltAnalyzerProviderFactory(
				"hanlp_dijkstra", AnalyzerScope.GLOBAL, new HanLPDijkstraAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_dijkstra",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_dijkstra";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(new DijkstraSegment().enableCustomDictionary(false)
								.enablePlaceRecognize(true).enableOrganizationRecognize(true), null,
								enablePorterStemming);
					}
				}));

		// Hanlp CRF Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_crf",
				new PreBuiltAnalyzerProviderFactory("hanlp_crf", AnalyzerScope.GLOBAL, new HanLPCRFAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_crf",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_crf";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(new CRFSegment(), null, enablePorterStemming);
					}
				}));

		// Hanlp Speed Analyzer
		indicesAnalysisService.analyzerProviderFactories().put("hanlp_speed",
				new PreBuiltAnalyzerProviderFactory("hanlp_speed", AnalyzerScope.GLOBAL, new HanLPSpeedAnalyzer()));
		indicesAnalysisService.tokenizerFactories().put("hanlp_speed",
				new PreBuiltTokenizerFactoryFactory(new TokenizerFactory() {
					@Override
					public String name() {
						return "hanlp_speed";
					}

					@Override
					public Tokenizer create() {
						return new HanLPTokenizer(new DoubleArrayTrieSegment().enableCustomDictionary(true), null,
								enablePorterStemming);
					}
				}));
	}

}