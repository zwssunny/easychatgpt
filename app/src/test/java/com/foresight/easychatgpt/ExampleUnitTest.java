package com.foresight.easychatgpt;


import com.foresight.tokenizers.Constants;
import com.foresight.tokenizers.GPT2Tokenizer;
import com.foresight.tokenizers.TokensCount;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private final String encodingExample = "Hello my name is Kevin.";
    private final List<Integer> decodingExample = Arrays.asList(15496, 616, 1438, 318, 7939, 13);
    private final String encodingLongTextExample = "interesting";
    private final List<Integer> decodingLongTextExample = Arrays.asList(47914);
    GPT2Tokenizer tokenizer;

    private JSONArray buildMessages() throws JSONException {
        JSONArray messages = new JSONArray();
        JSONObject message1 = new JSONObject();
        message1.put("role", "system");
        message1.put("content", "You are a helpful, pattern-following assistant that translates corporate jargon into plain English.");
        messages.put(message1);

        JSONObject message2 = new JSONObject();
        message2.put("role", "system");
        message2.put("name", "example_user");
        message2.put("content", "New synergies will help drive top-line growth.");
        messages.put(message2);

        JSONObject message3 = new JSONObject();
        message3.put("role", "system");
        message3.put("name", "example_assistant");
        message3.put("content", "Things working well together will increase revenue.");
        messages.put(message3);

        JSONObject message4 = new JSONObject();
        message4.put("role", "system");
        message4.put("name", "example_user");
        message4.put("content", "Let's circle back when we have more bandwidth to touch base on opportunities for increased leverage.");
        messages.put(message4);

        JSONObject message5 = new JSONObject();
        message5.put("role", "system");
        message5.put("name", "example_assistant");
        message5.put("content", "Let's talk later when we're less busy about how to do better.");
        messages.put(message5);

        JSONObject message6 = new JSONObject();
        message6.put("role", "user");
        message6.put("content", "This late pivot means we don't have time to boil the ocean for the client deliverable.");
        messages.put(message6);

        return messages;
    }

    @Before
    public void tokenizerFromPretrained() {
        InputStream encoderInputStream;
        InputStream bpeInputStream;
        try {
            String path = "src/main/assets/tokenizers/gpt2";
            encoderInputStream = Files.newInputStream(Paths.get(path + "/" + Constants.ENCODER_FILE_NAME));
            bpeInputStream = Files.newInputStream(Paths.get(path + "/" + Constants.VOCAB_FILE_NAME));
            tokenizer = GPT2Tokenizer.fromPretrained(encoderInputStream, bpeInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testTokenCount() throws JSONException {
        TokensCount tokensCount = new TokensCount(tokenizer);
        JSONArray messages = buildMessages();
        int nums = tokensCount.num_tokens_from_messages(messages);
        Assert.assertEquals(131, nums); //openai（tiktoken）接口是返回是126
    }

    @Test
    public void testEncoding() {
        List<Integer> result = tokenizer.encode(encodingExample);
        Assert.assertEquals(decodingExample, result);
    }

    @Test
    public void testDecoding() {
        String result = tokenizer.decode(decodingExample);
        Assert.assertEquals(encodingExample, result);
    }

    @Test
    public void testLongWord() {
        List<Integer> result = tokenizer.encode(encodingLongTextExample);
        Assert.assertEquals(decodingLongTextExample, result);
    }
}