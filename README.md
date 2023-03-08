# Easy_ChatGPT for Android
ChatGPT近期以强大的对话和信息整合能力风靡全网，可以写代码、改论文、讲故事，几乎无所不能。此存储库包含使用 OpenAI API 和 OkHttp 进行 API 集成的聊天应用程序的源代码。 该应用程序允许用户与使用 OpenAI 的 GPT-3.5 语言模型生成响应的 AI 模型聊天。

This repository contains the source code for a chat application that uses the OpenAI API and OkHttp for API integration. The app allows users to chat with an AI model that uses OpenAI's GPT-3 language model to generate responses. 
Features

Chat with an AI model using OpenAI's GPT-3 language model OkHttp used for API integration Simple and user-friendly interface Requirements

To run this app, you need: An OpenAI API key Android Studio or any other Android development environment A device or emulator running Android 4.4 or higher Getting Started

Support session context, will cost more

Open the project in Android Studio. Copy the file config.xml.bak to res/values/config.xml. Replace apiKey in the config.xml file with your own OpenAI API key. Build and run the app on an Android device or emulator. Contributing

If you want to contribute to this project, feel free to create a pull request. Any contributions are welcome!
# 更新日志
>**2023.03.08：**利用预训练模型实现本地字符的token分解功能

>**2023.03.05：** 利用openAI接口返回的有效tokens来修正会话长度，防止tokens溢出，减少费用.

>**2023.03.04：** 接入[ChatGPT API](https://platform.openai.com/docs/guides/chat) (gpt-3.5-turbo)，默认使用该模型进行对话
