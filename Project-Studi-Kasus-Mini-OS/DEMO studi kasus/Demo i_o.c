#include <stdio.h>
#include <stdint.h>
#include <string.h>
#include <windows.h>

// =============================================
// Timer resolusi tinggi (Windows)
// =============================================
uint64_t now_us() {
    LARGE_INTEGER f, c;
    QueryPerformanceFrequency(&f);
    QueryPerformanceCounter(&c);
    return (uint64_t)(c.QuadPart * 1000000 / f.QuadPart);
}

// =============================================
// Simulasi block device berbasis RAM
// =============================================
#define BLOCK_SIZE 4096
#define BLOCK_COUNT 1000

unsigned char block_device[BLOCK_SIZE * BLOCK_COUNT];

// menulis 1 blok ke device
int write_block(int block, const unsigned char *buf) {
    if (block < 0 || block >= BLOCK_COUNT) return -1;
    memcpy(&block_device[block * BLOCK_SIZE], buf, BLOCK_SIZE);
    return 0;
}

// membaca 1 blok dari device
int read_block(int block, unsigned char *buf) {
    if (block < 0 || block >= BLOCK_COUNT) return -1;
    memcpy(buf, &block_device[block * BLOCK_SIZE], BLOCK_SIZE);
    return 0;
}

// =============================================
// MAIN
// =============================================
int main() {
    unsigned char buf[BLOCK_SIZE];

    // isi buffer dengan pola data (0-255)
    for (int i = 0; i < BLOCK_SIZE; i++)
        buf[i] = (unsigned char)(i % 256);

    printf("=== Simulasi I/O 4KB Block (Windows Baseline) ===\n");

    // -------------------------------------
    // WRITE TEST
    // -------------------------------------
    uint64_t w0 = now_us();
    for (int block = 0; block < BLOCK_COUNT; block++)
        write_block(block, buf);
    uint64_t w1 = now_us();

    // -------------------------------------
    // READ TEST
    // -------------------------------------
    uint64_t r0 = now_us();
    for (int block = 0; block < BLOCK_COUNT; block++)
        read_block(block, buf);
    uint64_t r1 = now_us();

    // -------------------------------------
    // Hasil
    // -------------------------------------
    uint64_t write_time = w1 - w0;
    uint64_t read_time = r1 - r0;

    double MB_total = (BLOCK_SIZE * BLOCK_COUNT) / (1024.0 * 1024.0);

    double write_throughput = MB_total / (write_time / 1e6);
    double read_throughput  = MB_total / (read_time / 1e6);

    printf("Write time: %llu us | Throughput: %.2f MB/s\n", write_time, write_throughput);
    printf("Read  time: %llu us | Throughput: %.2f MB/s\n", read_time,  read_throughput);

    return 0;
}
