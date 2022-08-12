#include <jni.h>
#include <string>
#include "FFmpegDecode.h"
FFmpegDecode *decode;
extern "C" JNIEXPORT jstring JNICALL
Java_com_henry_ffmpegtestff_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    decode->decode();
    return env->NewStringUTF(hello.c_str());
}