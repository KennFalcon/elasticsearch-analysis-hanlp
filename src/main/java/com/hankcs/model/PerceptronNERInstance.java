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
public class PerceptronNERInstance {

    //使用volatile关键字保其可见性
    volatile private static PerceptronNERInstance instance = null;

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
            model = new LinearModel(HanLP.Config.PerceptronNERModelPath);
        } catch (IOException e) {
            model = null;
        }
        linearModel = model;
    }

    public LinearModel getLinearModel() {
        return linearModel;
    }
}
