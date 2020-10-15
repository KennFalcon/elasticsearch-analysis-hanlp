package com.hankcs.model;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.model.perceptron.model.LinearModel;

import java.io.IOException;

/**
 * Created on 2020/10/09
 *
 * @author Kenn
 */
public class PerceptronCWSInstance {

    //使用volatile关键字保其可见性
    volatile private static PerceptronCWSInstance instance = null;

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
