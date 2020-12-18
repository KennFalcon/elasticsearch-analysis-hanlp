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
public class PerceptronCWSInstance {

    //使用volatile关键字保其可见性
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
            model = new LinearModel(HanLP.Config.PerceptronCWSModelPath);
        } catch (IOException e) {
            model = null;
        }
        linearModel = model;
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
