package com.hankcs.model;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;

import java.io.IOException;

/**
 * Project: elasticsearch-analysis-hanlp
 * Description:
 * Author: Kenn
 * Create: 2020-10-09 09:47
 */
public class PerceptronPOSInstance {

    //使用volatile关键字保其可见性
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
        LinearModel model;
        try {
            model = new LinearModel(HanLP.Config.PerceptronPOSModelPath);
        } catch (IOException e) {
            model = null;
        }
        linearModel = model;
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
