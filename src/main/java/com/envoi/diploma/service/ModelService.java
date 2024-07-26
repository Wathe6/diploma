package com.envoi.diploma.service;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.api.preprocessor.NormalizerStandardize;
import org.nd4j.linalg.dataset.api.preprocessor.serializer.NormalizerSerializer;
import org.nd4j.linalg.factory.Nd4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Service
public class ModelService
{
    private MultiLayerNetwork model;
    private NormalizerStandardize normalizer;
    private static final String[] KEYWORDS = {"Задание", "Вариант", "Задания", "Варианты"};
    public ModelService() throws Exception
    {
        // Загрузка модели и нормализатора
        File modelFile = new File("neural_model/pdf_page_classifier.zip");
        File normalizerFile = new File("neural_model/normalizer.zip");
        this.model = ModelSerializer.restoreMultiLayerNetwork(modelFile);
        this.normalizer = NormalizerSerializer.getDefault().restore(normalizerFile);
    }

    public int getBestPage(MultipartFile file) {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            int numberOfPages = document.getNumberOfPages();
            double maxProbability = -1;
            int bestPage = -1;

            for (int i = 0; i < numberOfPages; i++) {
                pdfStripper.setStartPage(i + 1);
                pdfStripper.setEndPage(i + 1);
                String pageText = pdfStripper.getText(document);

                // Векторизация текста
                INDArray features = vectorizeTextWithPriority(pageText);
                normalizer.transform(features);

                // Предсказание
                INDArray output = model.output(features);
                double probability = output.getDouble(1);

                if (probability >= maxProbability) {
                    maxProbability = probability;
                    bestPage = i + 1;
                }
            }
            if(bestPage == -1)
                return 1;
            return bestPage;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private INDArray vectorizeTextWithPriority(String text) {
        String[] words = text.split("\\s+");
        HashMap<String, Integer> wordCount = new HashMap<>();

        for (String word : words) {
            word = word.toLowerCase();
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        INDArray vector = Nd4j.zeros(1, 500);
        for (int i = 0; i < KEYWORDS.length; i++) {
            String keyword = KEYWORDS[i];
            int count = wordCount.getOrDefault(keyword.toLowerCase(), 0);
            vector.putScalar(i, count * 10);  // Повышение веса ключевых слов
        }

        int index = KEYWORDS.length;
        for (String word : wordCount.keySet()) {
            if (index >= 500) break;
            vector.putScalar(index, wordCount.get(word));
            index++;
        }

        return vector;
    }
}