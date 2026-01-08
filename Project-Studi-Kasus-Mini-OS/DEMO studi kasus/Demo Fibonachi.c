#include <stdio.h>
#include <windows.h>
#include <stdint.h>


uint64_t now_us() {
    LARGE_INTEGER f, c;
    QueryPerformanceFrequency(&f);
    QueryPerformanceCounter(&c);
    return (uint64_t)(c.QuadPart * 1000000 / f.QuadPart);
}


long long fib(int n) {
    if (n <= 1) return n;
    return fib(n-1) + fib(n-2);
}


DWORD WINAPI task_fib(LPVOID arg) {
    int n = (int)(intptr_t)arg;
    uint64_t t0 = now_us();
    long long r = fib(n);
    uint64_t t1 = now_us();
    printf("Task fib(%d) = %lld | time = %llu us\n", n, r, (t1 - t0));
    return 0;
}


int main() {
    HANDLE T1 = CreateThread(NULL, 0, task_fib, (void*)35, 0, NULL);
    HANDLE T2 = CreateThread(NULL, 0, task_fib, (void*)38, 0, NULL);


    WaitForSingleObject(T1, INFINITE);
    WaitForSingleObject(T2, INFINITE);


    return 0;
}