package com.example.demo.service;

import com.example.demo.config.GeminiConfiguration;
import com.example.demo.util.StateUtils;
import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;

@Service
@RequiredArgsConstructor
public class GeminiService extends StateUtils {

    private final GeminiConfiguration geminiConfiguration;
    private final  Client client = Client.builder().apiKey(dotenv.get("GEMINI_API_KEY")).build();
    public String answerQuestion(String question) {
        try {
            // Sets the safety settings in the config.
            final ImmutableList<SafetySetting> safetySettings = geminiConfiguration.safetySettings();

            // Sets the system instruction in the config.
            final Content systemInstruction = geminiConfiguration.systemInstruction();


            // Sets the Google Search tool in the config.
//            Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder().build()).build();

            GenerateContentConfig config =
                    GenerateContentConfig.builder()
                            .candidateCount(1)
                            .maxOutputTokens(2048)
                            .safetySettings(safetySettings)
                            .systemInstruction(systemInstruction)
                            .build();

            String model = "gemini-2.0-flash-001";


            GenerateContentResponse response = client.models.generateContent(model, question, config);




            logger.log(Level.INFO, response.text());

            return response.text();
        }  catch (HttpException e) {
        logger.log(Level.SEVERE, "HTTP 요청 오류 발생: " + e.getMessage());
        throw new RuntimeException("Gemini API 요청 중 HTTP 오류 발생", e);

    } catch (IOException e) {
        logger.log(Level.SEVERE, "입출력 오류 발생: " + e.getMessage());
        throw new RuntimeException("Gemini API 요청 중 입출력 오류 발생", e);

    } catch (Exception e) {
        logger.log(Level.SEVERE, "알 수 없는 오류 발생: " + e.getMessage());
        throw new RuntimeException("Gemini API 요청 중 예기치 않은 오류 발생", e);

    }

}

}
