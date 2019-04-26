package com.hankcs.cfg;

import com.hankcs.dic.Dictionary;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

/**
 * @project: elasticsearch-analysis-hanlp
 * @description: 配置信息
 * @author: Kenn
 * @create: 2018-12-14 15:10
 */
public class Configuration {

    private Environment environment;

    private Settings settings;

    private boolean enablePorterStemming;

    private boolean enableIndexMode;

    private boolean enableNumberQuantifierRecognize;

    private boolean enableCustomDictionary;

    private boolean enableTranslatedNameRecognize;

    private boolean enableJapaneseNameRecognize;

    private boolean enableOrganizationRecognize;

    private boolean enablePlaceRecognize;

    private boolean enableNameRecognize;

    private boolean enableTraditionalChineseMode;

    private boolean enableStopDictionary;

    private boolean enablePartOfSpeechTagging;

    private boolean enableRemoteDict;

    private boolean enableNormalization;

    private boolean enableOffset;

    @Inject
    public Configuration(Environment env, Settings settings) {
        this.environment = env;
        this.settings = settings;
        this.enablePorterStemming = settings.get("enable_porter_stemming", "false").equals("true");
        this.enableIndexMode = settings.get("enable_index_mode", "false").equals("true");
        this.enableNumberQuantifierRecognize = settings.get("enable_number_quantifier_recognize", "false").equals("true");
        this.enableCustomDictionary = settings.get("enable_custom_dictionary", "true").equals("true");
        this.enableTranslatedNameRecognize = settings.get("enable_translated_name_recognize", "true").equals("true");
        this.enableJapaneseNameRecognize = settings.get("enable_japanese_name_recognize", "false").equals("true");
        this.enableOrganizationRecognize = settings.get("enable_organization_recognize", "false").equals("true");
        this.enablePlaceRecognize = settings.get("enable_place_recognize", "false").equals("true");
        this.enableNameRecognize = settings.get("enable_name_recognize", "true").equals("true");
        this.enableTraditionalChineseMode = settings.get("enable_traditional_chinese_mode", "false").equals("true");
        this.enableStopDictionary = settings.get("enable_stop_dictionary", "false").equals("true");
        this.enablePartOfSpeechTagging = settings.get("enable_part_of_speech_tagging", "false").equals("true");
        this.enableRemoteDict = settings.get("enable_remote_dict", "true").equals("true");
        this.enableNormalization = settings.get("enable_normalization", "false").equals("true");
        this.enableOffset = settings.get("enable_offset", "true").equals("true");
        Dictionary.initial(this);
    }

    public Environment getEnvironment() {
        return this.environment;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public boolean isEnablePorterStemming() {
        return this.enablePorterStemming;
    }

    public Configuration enablePorterStemming(boolean enablePorterStemming) {
        this.enablePorterStemming = enablePorterStemming;
        return this;
    }

    public boolean isEnableIndexMode() {
        return this.enableIndexMode;
    }

    public Configuration enableIndexMode(boolean enableIndexMode) {
        this.enableIndexMode = enableIndexMode;
        return this;
    }

    public boolean isEnableNumberQuantifierRecognize() {
        return this.enableNumberQuantifierRecognize;
    }

    public Configuration enableNumberQuantifierRecognize(boolean enableNumberQuantifierRecognize) {
        this.enableNumberQuantifierRecognize = enableNumberQuantifierRecognize;
        return this;
    }

    public boolean isEnableCustomDictionary() {
        return this.enableCustomDictionary;
    }

    public Configuration enableCustomDictionary(boolean enableCustomDictionary) {
        this.enableCustomDictionary = enableCustomDictionary;
        return this;
    }

    public boolean isEnableTranslatedNameRecognize() {
        return this.enableTranslatedNameRecognize;
    }

    public Configuration enableTranslatedNameRecognize(boolean enableTranslatedNameRecognize) {
        this.enableTranslatedNameRecognize = enableTranslatedNameRecognize;
        return this;
    }

    public boolean isEnableJapaneseNameRecognize() {
        return this.enableJapaneseNameRecognize;
    }

    public Configuration enableJapaneseNameRecognize(boolean enableJapaneseNameRecognize) {
        this.enableJapaneseNameRecognize = enableJapaneseNameRecognize;
        return this;
    }

    public boolean isEnableOrganizationRecognize() {
        return this.enableOrganizationRecognize;
    }

    public Configuration enableOrganizationRecognize(boolean enableOrganizationRecognize) {
        this.enableOrganizationRecognize = enableOrganizationRecognize;
        return this;
    }

    public boolean isEnablePlaceRecognize() {
        return this.enablePlaceRecognize;
    }

    public Configuration enablePlaceRecognize(boolean enablePlaceRecognize) {
        this.enablePlaceRecognize = enablePlaceRecognize;
        return this;
    }

    public boolean isEnableNameRecognize() {
        return this.enableNameRecognize;
    }

    public Configuration enableNameRecognize(boolean enableNameRecognize) {
        this.enableNameRecognize = enableNameRecognize;
        return this;
    }

    public boolean isEnableTraditionalChineseMode() {
        return this.enableTraditionalChineseMode;
    }

    public Configuration enableTraditionalChineseMode(boolean enableTraditionalChineseMode) {
        this.enableTraditionalChineseMode = enableTraditionalChineseMode;
        return this;
    }

    public boolean isEnableStopDictionary() {
        return this.enableStopDictionary;
    }

    public Configuration enableStopDictionary(boolean enableStopDictionary) {
        this.enableStopDictionary = enableStopDictionary;
        return this;
    }

    public boolean isEnablePartOfSpeechTagging() {
        return this.enablePartOfSpeechTagging;
    }

    public Configuration enablePartOfSpeechTagging(boolean enablePartOfSpeechTagging) {
        this.enablePartOfSpeechTagging = enablePartOfSpeechTagging;
        return this;
    }

    public boolean isEnableRemoteDict() {
        return enableRemoteDict;
    }

    public Configuration enableRemoteDict(boolean enableRemoteDict) {
        this.enableRemoteDict = enableRemoteDict;
        return this;
    }

    public boolean isEnableNormalization() {
        return enableNormalization;
    }

    public void setEnableNormalization(boolean enableNormalization) {
        this.enableNormalization = enableNormalization;
    }

    public boolean isEnableOffset() {
        return enableOffset;
    }

    public Configuration enableOffset(boolean enableOffset) {
        this.enableOffset = enableOffset;
        return this;
    }
}
