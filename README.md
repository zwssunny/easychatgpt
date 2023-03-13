# Easy_ChatGPT for Android
1、ChatGPT近期以强大的对话和信息整合能力风靡全网，可以写代码、改论文、讲故事，几乎无所不能。此存储库包含使用 OpenAI API 和 OkHttp 进行 API 集成的聊天应用程序的源代码。 该应用程序允许用户与使用 OpenAI 的 GPT-3.5 语言模型生成响应的 AI 模型聊天。
ChatGPT has recently become popular all over the Internet with its powerful dialogue and information integration capabilities. It can write codes, revise papers, and tell stories, almost omnipotent. This repository contains the source code of a chat application using OpenAI API and OkHttp for API integration. The app allows users to chat with an AI model that generates responses using OpenAI's GPT-3.5 language model.
2、聊天模型使用 OpenAI 的 GPT-3 AI语言模型集成 OkHttp API 调用， 简单且用户友好的界面
Chat with an AI model using OpenAI's GPT-3 language model OkHttp used for API integration Simple and user-friendly interface Requirements
3、要运行此应用程序，您需要： OpenAI API 密钥 Android Studio 或任何其他 Android 开发环境 最低运行 Android 8.0 或更高版本的设备或模拟器
To run this app, you need: An OpenAI API key Android Studio or any other Android development environment A device or emulator running Android 8.0 or higher Getting Started
4、支持会话上下文，会花费更多tokens
Support session context, will cost more
5、在 Android Studio 中打开项目。 将根目录下的文件 config.xml.bak 复制到 res/values/config.xml。 将 config.xml 文件中的 apiKey 替换为您自己的 OpenAI API 密钥（使用科学上网工具到 https://platform.openai.com注册账号，获取apikey）。 在 Android 设备或模拟器上构建并运行应用程序
Open the project in Android Studio. Copy the file config.xml.bak in the root directory to res/values/config.xml. Replace apiKey in the config.xml file with your own OpenAI API key. Build and run the app on an Android device or emulator. 

# 贡献力量 Contributing：
如果您想为这个项目做出贡献，请随时创建拉取请求。 欢迎任何贡献！
If you want to contribute to this project, feel free to create a pull request. Any contributions are welcome!
# 更新日志
>**2023.03.08：** 利用预训练模型实现本地字符的token分解功能

>**2023.03.05：** 利用openAI接口返回的有效tokens来修正会话长度，防止tokens溢出，减少费用.

>**2023.03.04：** 接入[ChatGPT API](https://platform.openai.com/docs/guides/chat) (gpt-3.5-turbo)，默认使用该模型进行对话
