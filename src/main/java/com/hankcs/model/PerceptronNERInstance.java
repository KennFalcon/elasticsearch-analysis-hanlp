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

    //使用volatile关键字保其可见性
    private static volatile PerceptronNERInstance instance = null;

    public static PerceptronNERInstance getInstance() {
        if(instance == null){
            synchronized (PerceptronNERInstance.class) {
                if(instance == null){//二次检查
                    instance = new PerceptronNERInstance();
                }
            }
        }
        return instance;
    }

    private final LinearModel linearModel;

    private PerceptronNERInstance() {
        LinearModel model;
        try {
            if (FileSystemUtils.exists(Paths.get(
                    AccessController.doPrivileged((PrivilegedAction<String>) () -> HanLP.Config.PerceptronNERModelPath)
            ).toAbsolutePath())) {
                model = new LinearModel(HanLP.Config.PerceptronNERModelPath);
            } else {
                logger.warn("can not find perceptron ner model from [{}]", HanLP.Config.PerceptronNERModelPath);
                model = null;
            }
        } catch (IOException e) {
            logger.error(() -> new ParameterizedMessage("load perceptron ner model from [{}] error", HanLP.Config.PerceptronNERModelPath), e);
            model = null;
        }
        linearModel = model;
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
