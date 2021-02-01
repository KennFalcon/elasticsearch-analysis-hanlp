package com.hankcs.model;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.elasticsearch.common.io.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description:
 * Author: Kenn
 * Create: 2020-10-09 09:47
 */
public class PerceptronNERInstance {

    private static final Logger logger = LogManager.getLogger(PerceptronNERInstance.class);

    private static volatile PerceptronNERInstance instance = null;

    public static PerceptronNERInstance getInstance() {
        if (instance == null) {
            synchronized (PerceptronNERInstance.class) {
                if (instance == null) {//二次检查
                    instance = new PerceptronNERInstance();
                }
            }
        }
        return instance;
    }

    private final LinearModel linearModel;

    private PerceptronNERInstance() {
        if (FileSystemUtils.exists(Paths.get(
                AccessController.doPrivileged((PrivilegedAction<String>) () -> HanLP.Config.PerceptronNERModelPath)
        ).toAbsolutePath())) {
            linearModel = AccessController.doPrivileged((PrivilegedAction<LinearModel>) () -> {
                try {
                    return new LinearModel(HanLP.Config.PerceptronNERModelPath);
                } catch (IOException e) {
                    logger.error(() ->
                            new ParameterizedMessage("load perceptron ner model from [{}] error", HanLP.Config.PerceptronNERModelPath), e);
                    return null;
                }
            });
        } else {
            logger.warn("can not find perceptron ner model from [{}]", HanLP.Config.PerceptronNERModelPath);
            linearModel = null;
        }
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
