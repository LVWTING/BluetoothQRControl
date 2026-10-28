# 二维码蓝牙控制（Android + 网页）

这是一个最小 Android Studio 项目：网页提供“开启蓝牙 / 关闭蓝牙”按钮，按钮通过 `btcontrol://on` 或 `btcontrol://off` 唤起本地 Android App。

## 重要系统限制
- 普通网页不能直接开关 Android 蓝牙。
- Android 12+ 对蓝牙权限有限制；开启操作使用系统确认 UI。
- 关闭操作打开系统蓝牙设置，用户需要手动关闭。此项目不做静默开关，也不需要 Root。
- 项目 targetSdk 32 是为了兼容旧版蓝牙 API；新系统仍可能弹出权限请求。请勿将其视为绕过 Android 系统限制的方法。

## 在 Android Studio 编译
1. 安装 Android Studio（建议近期稳定版）并安装 Android SDK Platform 35。
2. 解压项目，Android Studio 选择 **Open**，打开 `BluetoothQRControl` 文件夹。
3. 等待 Gradle Sync 完成。
4. 连接 Android 手机，打开开发者选项和 USB 调试。
5. 点击 Run 安装测试，或菜单 **Build > Build APK(s)** 生成 APK。

## 网页
`website/index.html` 是网页控制页。把它部署到支持 HTTPS 的静态网页托管服务，然后把二维码内容设置为网页 URL。

例如二维码内容：
`https://你的域名.example/bluetooth/`

网页按钮使用自定义 URI：
- `btcontrol://on`
- `btcontrol://off`

手机需要先安装本项目生成的 App。部分浏览器可能会阻止自定义 URI 跳转或显示确认；请在实际手机浏览器中测试。

## 测试流程
1. 安装 App。
2. 在浏览器打开 `website/index.html`（部署后用 HTTPS 地址打开）。
3. 点击“开启蓝牙”，系统应显示蓝牙开启确认界面。
4. 点击“关闭蓝牙”，系统蓝牙设置应打开，手动关闭蓝牙。

## 项目结构
- `app/` Android 原生 App
- `app/src/main/assets/index.html` 内置网页副本
- `website/index.html` 可部署的网页
