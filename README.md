# HarwareLib  

[![](https://jitpack.io/v/nero21/bec-hardware-library.svg)](https://jitpack.io/#nero21/bec-hardware-library) [![996.ICU](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.

#### 介绍
一个方便使用、扩展性佳的串口、USB设备操作库；
适用于Android，使用Kotlin编写；

#### 软件架构
基于google的 https://github.com/cepr/android-serialport-api 实现了串口控制打印机（目前仅适用于XPrinter）、串口秤数据的读取、解析，并集成芯烨(Xprinter)提供的SDK实现USB、蓝牙、WLAN控制打印机。

#### 使用说明 

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
Step 2. Add the dependency 
  
	dependencies {
	        implementation 'com.github.nero21:bec-hardware-library:1.0.0'
	}

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
