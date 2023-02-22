package com.foresight.easychatgpt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *保存会话过程，进行上下文维护
 * */
public class Session {

    private List<Map<String, String>> session = new ArrayList<>();
    private int maxTokens;
    private String character_desc;

    public Session(int maxTokens, String character_desc) {
        this.maxTokens = maxTokens;
        this.character_desc = character_desc;
    }

    /*
    *
    *   build query with conversation history
        e.g.  Q: xxx
        A: xxx
        Q: xxx
        :param query: query content
        :return: query content with conversaction
    * */
    public String buildSessionQuery(String query) {

        String prompt = this.character_desc;
        if (!prompt.isEmpty()) {
            prompt += "\n\n\n";
        }
        if (session.size() > 0) {
            for (Map<String, String> conversation : session) {
                prompt += "Q: " + conversation.get("question") + "\n\n\nA: " + conversation.get("answer") + "\n";
            }
            prompt += "Q: " + query + "\nA: ";
            return prompt;
        } else {
            return prompt + "Q: " + query + "\nA: ";
        }
    }

    public void saveSession(String query, String answer) {
        Map<String, String> conversation = new HashMap<>();
        conversation.put("question", query);
        conversation.put("answer", answer);
        session.add(conversation);
        // discard exceed limit conversation
        discardExceedConversation(session, maxTokens);
    }

    private void discardExceedConversation(List<Map<String, String>> session, int maxTokens) {
        int count = 0;
        List<Integer> countList = new ArrayList<>();
        for (int i = session.size() - 1; i >= 0; i--) {
            // count tokens of conversation list
            Map<String, String> historyConv = session.get(i);
            count += historyConv.get("question").length() + historyConv.get("answer").length();
            countList.add(count);
        }
        for (int c : countList) {
            if (c > maxTokens) {
                // pop first conversation
                session.remove(0);
            }
        }
    }

    public void clearSession() {
        session.clear();
    }

}
