package com.example.demo.service;

import com.google.common.collect.ImmutableList;
import com.google.genai.Client;
import com.google.genai.types.*;
import io.github.cdimascio.dotenv.Dotenv;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class GeminiService {

    private final static Logger logger = Logger.getLogger(GeminiService.class.getName());
    private final static String GEMINI_API_KEY = Dotenv.configure().ignoreIfMissing().load().get("GEMINI_API_KEY");
    private final  Client client = Client.builder().apiKey(GEMINI_API_KEY).build();
    public String answerQuestion(String question) {
        try {
            // Sets the safety settings in the config.
            ImmutableList<SafetySetting> safetySettings =
                    ImmutableList.of(
                            SafetySetting.builder()
                                    .category("HARM_CATEGORY_HATE_SPEECH")
                                    .threshold("BLOCK_ONLY_HIGH")
                                    .build(),
                            SafetySetting.builder()
                                    .category("HARM_CATEGORY_DANGEROUS_CONTENT")
                                    .threshold("BLOCK_LOW_AND_ABOVE")
                                    .build());

            // Sets the system instruction in the config.
            final Content systemInstruction =
                    Content.builder()
                            .parts(ImmutableList.of(Part.builder().text("Your system instruction aims to define clear roles and guidelines for interactions with the AI:\n" +
                                    "\n" +
                                    "Role Definition: The AI is designated as a Korean history assistant, responsible solely for addressing questions related to Korean history.\n" +
                                    "\n" +
                                    "Manager's Role: As the manager, you set the scope of topics the AI can discuss.\n" +
                                    "\n" +
                                    "User's Role: Individuals posing questions are identified as users. If a user's question falls outside the realm of Korean history, the AI should respond by indicating that the question is off-topic.\n" +
                                    "\n" +
                                    "Response Format: The AI should reply exclusively in Korean, using plain text without markdown formatting.\n" +
                                    "\n").build()))
                            .build();

            // Sets the Google Search tool in the config.
            Tool googleSearchTool = Tool.builder().googleSearch(GoogleSearch.builder().build()).build();

            GenerateContentConfig config =
                    GenerateContentConfig.builder()
                            .candidateCount(1)
                            .maxOutputTokens(1024)
                            .safetySettings(safetySettings)
                            .systemInstruction(systemInstruction)
                            .tools(ImmutableList.of(googleSearchTool))
                            .build();

            String model = "gemini-2.0-flash-001";

            if (isJailbreakAttempt(question)) {
                return "이 질문은 시스템 역할을 변경하려는 시도입니다. 한국 역사에 관한 질문만 해 주세요.";
            }

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
    private boolean isJailbreakAttempt(String userPrompt) {
        String lowerCasePrompt = userPrompt.toLowerCase();

        // 시스템 역할을 변경하려는 시도를 감지
        return lowerCasePrompt.contains("you must have to explain") ||
                lowerCasePrompt.contains("you are") ||
                lowerCasePrompt.contains("your role is");
    }

}
