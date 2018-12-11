#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_satoshi_1maemoto_myapplication_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_satoshi_1maemoto_myapplication_ExampleInstrumentedTest_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++ to InstrumentedTest";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jboolean JNICALL Java_com_example_satoshi_1maemoto_myapplication_ExampleInstrumentedTest_getBufferArray(JNIEnv *env, jobject, jobjectArray array) {
    auto elementCount = env->GetArrayLength(array);
    if (elementCount == 0) {
        return false;
    }

    for (auto index = 0; index < elementCount; index++) {
        jintArray element = (jintArray)env->GetObjectArrayElement(array , index);
        auto buffer = env->GetIntArrayElements(element, nullptr);
        auto bufferCount = env->GetArrayLength(element);

        auto color = ((index % 2) == 0) ? 0x00000000 : 0x00FFFFFF;
        auto step = ((index % 2) == 0) ? 1 : -1;
        for (auto pixel = 0; pixel < bufferCount; pixel++) {
            buffer[pixel] = 0xFF000000 | color;
            color += step;
        }

        env->ReleaseIntArrayElements(element, buffer, 0);
        env->DeleteLocalRef(element);
    }
    return true;
}