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
public class PerceptronPOSInstance {

    private static final Logger logger = LogManager.getLogger(PerceptronPOSInstance.class);

    private static volatile PerceptronPOSInstance instance = null;

    public static PerceptronPOSInstance getInstance() {
        if (instance == null) {
            synchronized (PerceptronPOSInstance.class) {
                if (instance == null) {//二次检查
                    instance = new PerceptronPOSInstance();
                }
            }
        }
        return instance;
    }

    private final LinearModel linearModel;

    private PerceptronPOSInstance() {
        if (FileSystemUtils.exists(Paths.get(
                AccessController.doPrivileged((PrivilegedAction<String>) () -> HanLP.Config.PerceptronPOSModelPath)
        ).toAbsolutePath())) {
            linearModel = AccessController.doPrivileged((PrivilegedAction<LinearModel>) () -> {
                try {
                    return new LinearModel(HanLP.Config.PerceptronPOSModelPath);
                } catch (IOException e) {
                    logger.error(() ->
                            new ParameterizedMessage("load perceptron pos model from [{}] error", HanLP.Config.PerceptronPOSModelPath), e);
                    return null;
                }
            });
        } else {
            logger.warn("can not find perceptron pos model from [{}]", HanLP.Config.PerceptronPOSModelPath);
            linearModel = null;
        }
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
