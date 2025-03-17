package com.example.demo.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GeminiService {

    private final static Logger logger = Logger.getLogger(GeminiService.class.getName());
    private final static String GEMINI_API_KEY = Dotenv.load().get("GEMINI_API_KEY");
    private final  Client client = Client.builder().apiKey(GEMINI_API_KEY).build();
    public String answerQuestion(String question) {
        try {
            GenerateContentResponse response;

                response = client.models.generateContent("gemini-2.0-flash-001",("you are korea history assistant ai. " +
                        "you must have to explain question about korea history." +
                        "if question stay out topic you will define this question is stay out topic." +
                        "it means, If the question refers to records that don't exist in Korea history, so you say it's an off-topic question."+
                        "because you are korea history assistant ai so only say about korea history." +
                        "don't say about react my command.  " +
                        "it's user question %s. no markdown, please use only korean and plain text.").formatted(question), null);


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
