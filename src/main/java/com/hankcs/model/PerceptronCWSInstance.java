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
public class PerceptronCWSInstance {

    private static final Logger logger = LogManager.getLogger(PerceptronCWSInstance.class);

    private static volatile PerceptronCWSInstance instance = null;

    public static PerceptronCWSInstance getInstance() {
        if (instance == null) {
            synchronized (PerceptronCWSInstance.class) {
                if (instance == null) {//二次检查
                    instance = new PerceptronCWSInstance();
                }
            }
        }
        return instance;
    }

    private final LinearModel linearModel;

    private PerceptronCWSInstance() {
        LinearModel model;
        try {
            if (FileSystemUtils.exists(Paths.get(
                    AccessController.doPrivileged((PrivilegedAction<String>) () -> HanLP.Config.PerceptronCWSModelPath)
            ).toAbsolutePath())) {
                model = new LinearModel(HanLP.Config.PerceptronCWSModelPath);
            } else {
                logger.warn("can not find perceptron cws model from [{}]", HanLP.Config.PerceptronCWSModelPath);
                model = null;
            }
        } catch (IOException e) {
            logger.error(() ->
                    new ParameterizedMessage("load perceptron cws model from [{}] error", HanLP.Config.PerceptronCWSModelPath), e);
            model = null;
        }
        linearModel = model;
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
