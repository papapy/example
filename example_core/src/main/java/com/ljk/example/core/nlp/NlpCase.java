package com.ljk.example.core.nlp;

import opennlp.tools.langdetect.Language;
import opennlp.tools.langdetect.LanguageDetector;
import opennlp.tools.langdetect.LanguageDetectorME;
import opennlp.tools.langdetect.LanguageDetectorModel;

import java.net.URL;
import java.util.Arrays;

/**
 * @author xkey  2019/8/29 17:26
 */
public class NlpCase {


    public static void main(String[] args) throws Exception {
        LanguageDetectorModel languageDetectorModel = new LanguageDetectorModel(new URL("http://opennlp.sourceforge.net/models-1.5/en-parser-chunking.bin"));
        LanguageDetector languageDetector = new LanguageDetectorME(languageDetectorModel);
        Language language = languageDetector.predictLanguage("是电饭锅和健康");

        System.out.println(language.getLang());
        System.out.println(language.getConfidence());
    }
}
