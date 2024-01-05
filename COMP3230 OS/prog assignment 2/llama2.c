/*
PLEASE WRITE DOWN FOLLOWING INFO BEFORE SUBMISSION
* FILE NAME: llama2_3035927257.c
* NAME: Hong Yuk Sing
* UID : 3035927257
* Development Platform: CS workbench2
* Remark: (How much you implemented?) 100%
* How to compile: (gcc -o llama2_3035927257 llama2_3035927257.c utilities.c -O2 -pthread -lm)

Please download the model and tokenizer to the same folder:
$ wget -O model.bin https://huggingface.co/huangs0/llama2.c/resolve/main/model.bin
$ wget -O tokenizer.bin https://huggingface.co/huangs0/llama2.c/resolve/main/tokenizer.bin

In compile, remember to add `-pthred` to link library:
$ gcc -o llama2_[UID] llama2_[UID].c utilities.c -O2 -pthread -lm

Then Run with:
$ ./llama2_[UID] <seed> <thr_count>
*/

#define _GNU_SOURCE
#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <sys/time.h>
#include <sys/resource.h>
#include "utilities.h"

/**
 * ----------------------------------------------------------------------------
 * TASK - Optimize Matrix-Vector Multiplication by Multi-Threading
 *
 * Matrix-Vector Multiplication, used in Attention and Feed-Forward Network
 * is the most time-consuming part of GPT. Luckily, most of computation is
 * independent of each row, so we can use Multi-Threading for acceleration.
 *
 * Please use <pthread.h> and your favorite synchronization method,
 * semaphore / mutex lock + conditional variable
 *
 * A sequential version is provided in seq.c, please modify it to parallel version.
*/

// YOUR CODE STARTS HERE

// Addtional Header File Here
#include <pthread.h>
#include <semaphore.h>

struct rusage main_usage;        // get usage for main thread
struct rusage* thread_usage;     // array to store the thread usage data 
pthread_t* thread_list;    
int num_of_thread;              // global variable for the total num of threads 

typedef struct
{
    float* out;
    float* vec;
    float* mat;
    int thread_id;
    int col;
    int start_row;
    int end_row;
    int terminate;
    sem_t start;
    sem_t finish;
} thread_data;          // use to pass the parm for mat_vec_mul for each threads 
thread_data* thread_data_list;

// function declaration 
void* thr_func(void* arg);

int init_mat_vec_mul(int thr_count) {
    num_of_thread = thr_count;

    // initialize the pthread data structure
    thread_list = malloc(sizeof(pthread_t) * thr_count);
    thread_data_list = malloc(sizeof(thread_data) * thr_count);
    thread_usage = malloc(sizeof(main_usage) * thr_count);

    // Create n threads
    for (int i = 0; i < thr_count; i++) {
        // initialize Semaphore
        sem_init(&thread_data_list[i].start, 0, 0);
        sem_init(&thread_data_list[i].finish, 0, 0);

        int rc = pthread_create(&thread_list[i], NULL, thr_func, &thread_data_list[i]);
        
        //error handling 
        if (rc != 0) {
            char error_str[50];
            sprintf(error_str, "Thread %i init error !", i);
            perror(error_str);
        }
    }
    return 0;
}

// multi-threading version of  mat_vec_mul(), which can specific the row that handled by each function call
void mat_vec_mul_range(float* out, float* vec, float* mat, int col, int start_row, int end_row) {
    for (int i = start_row; i < end_row + 1; i++) {
        float val = 0.0f;
        for (int j = 0; j < col; j++) {
            val += mat[i * col + j] * vec[j];
        }
        out[i] = val;
    }
}

void mat_vec_mul(float* out, float* vec, float* mat, int col, int row) {

    // assign new parameters (out, vec, mat, col, row) to threads
    int thr_count = num_of_thread;
    for (int i = 0; i < thr_count; i++) {
        thread_data_list[i].thread_id = i;
        thread_data_list[i].out = out;
        thread_data_list[i].vec = vec;
        thread_data_list[i].mat = mat;
        thread_data_list[i].col = col;
        thread_data_list[i].terminate = 0;

        if (row % thr_count == 0) {     // 𝑑 is divisible by n
            thread_data_list[i].start_row = (int)(i * (row / thr_count));
            thread_data_list[i].end_row = (int)((i + 1) * (row / thr_count) - 1);
        }
        else {                          // if 𝑑 is not divisible by n
            int num_of_row = (int)(row / thr_count) + 1;
            if (i != thr_count - 1) {
                thread_data_list[i].start_row = (int)(i * num_of_row);
                thread_data_list[i].end_row = (int)((i + 1) * num_of_row - 1);
            }
            else {                  // the last row
                if ((int)((i)*num_of_row) < row) {
                    thread_data_list[i].start_row = (int)((i)*num_of_row);
                    thread_data_list[i].end_row = (int)(row - 1);
                }
                else {
                    thread_data_list[i].start_row = (int)(row - 1);
                    thread_data_list[i].end_row = (int)(row - 1);
                }
            }
        }
        // set Semaphore to wake up the thread to do calculation 
        sem_post(&thread_data_list[i].start);
    }

    for (int i = 0; i < thr_count; i++) {
        // wait for each threads to finish the calculation
        sem_wait(&thread_data_list[i].finish);
    }
}

int close_mat_vec_mul() {

    //Wake up threads to collect the system usage (of themselves) and terminates
    int thr_count = num_of_thread;
    for (int i = 0; i < thr_count; i++) {
        // set flag in thread_data_list to tell threads to exit 
        thread_data_list[i].terminate = 1;
        sem_post(&thread_data_list[i].start);
    }
    int iret;
    // join the treads
    for (int i = 0; i < thr_count; i++) {
        iret = pthread_join(thread_list[i], NULL);
        if (iret != 0) {
            char error_str[50];
            sprintf(error_str, "Cannot collect thread %i", i);
            perror(error_str);
        }
    }

    // printing system usage 
    getrusage(RUSAGE_SELF, &main_usage);

    // print the usage data for each thread
    for (int i = 0; i < thr_count; i++) {
        printf("Thread %d has completed - user: %.4f s, system: %.4f s\n",
            i,
            (thread_usage[i].ru_utime.tv_sec + thread_usage[i].ru_utime.tv_usec / 1000000.0),
            (thread_usage[i].ru_stime.tv_sec + thread_usage[i].ru_stime.tv_usec / 1000000.0));
    }

     // print the usage data for the main threads 
    printf("main thread - user: %.4f s, system: %.4f s\n",
        (main_usage.ru_utime.tv_sec + main_usage.ru_utime.tv_usec / 1000000.0),
        (main_usage.ru_stime.tv_sec + main_usage.ru_stime.tv_usec / 1000000.0));


    // clear all resources related with multi-threading, and return

    for (int i = 0; i < thr_count; i++) {
        sem_destroy(&thread_data_list[i].start);
        sem_destroy(&thread_data_list[i].finish);
    }
    free(thread_data_list);
    free(thread_list);
    free(thread_usage);

}

void* thr_func(void* arg) {
    thread_data* data = (thread_data*)arg;

    while (1) {
        // sleep and wait until woke up by the main thread 
        sem_wait(&data->start);
        if (data->terminate) {
            break;
        }
        // pass the arm from threads_data_list to mat_vec_mul_range() function 
        mat_vec_mul_range(data->out, data->vec, data->mat, data->col, data->start_row, data->end_row);
        // sem finsih semaphore when finish 
        sem_post(&data->finish);
    }
    //collect threads usage data before exit 
    getrusage(RUSAGE_THREAD, &thread_usage[data->thread_id]);
    pthread_exit(NULL);
}

// YOUR CODE ENDS HERE

int transformer(int token, int pos, LLMConfig* p, LLMRuntime* s, LLMWeight* w)
{

    // a few convenience variables
    int dim = p->dim, hidden_dim = p->hidden_dim, head_size = p->dim / p->n_heads;

    // copy the token embedding into x
    memcpy(s->x, &(w->token_embedding_table[token * dim]), dim * sizeof(float));

    // forward all the layers
    for (int l = 0; l < p->n_layers; l++) {

        // Attention
        {
            // attention normalization
            normalize(s->xb, s->x, w->rms_att_weight + l * dim, dim);

            // q, k, v = w_q @ x, w_k @ x, w_v @ x, respectively
            mat_vec_mul(s->q, s->xb, w->wq + l * dim * dim, dim, dim);
            mat_vec_mul(s->k, s->xb, w->wk + l * dim * dim, dim, dim);
            mat_vec_mul(s->v, s->xb, w->wv + l * dim * dim, dim, dim);

            // apply positional embedding
            position_embedding(s->q, s->k, w, pos, p->dim, p->n_heads);

            // save intermediate result for later reference
            key_value_cache(l, pos, p, s);

            // attention calculation
            attention(l, pos, p, s, w);

            // wo @ x to get final result
            mat_vec_mul(s->xb2, s->xb, w->wo + l * dim * dim, dim, dim);

            // residual connection back into x
            accum(s->x, s->xb2, dim);
        }

        // Feed-Forward Network: w2 @ (silu(w1 @ x) * (w3 @ x)), * is element-wise multiply
        {
            // FFN Normalization
            normalize(s->xb, s->x, w->rms_ffn_weight + l * dim, dim);

            // w1 @ x
            mat_vec_mul(s->h1, s->xb, w->w1 + l * dim * hidden_dim, dim, hidden_dim);
            // silu(w1 @ x)
            silu(s->h1, hidden_dim);
            // w3 @ x
            mat_vec_mul(s->h2, s->xb, w->w3 + l * dim * hidden_dim, dim, hidden_dim);
            // silu(w1 @ x) * (w3 @ x)
            element_wise_mul(s->h1, s->h2, hidden_dim);
            // w2 @ (silu(w1 @ x) * (w3 @ x))
            mat_vec_mul(s->xb, s->h1, w->w2 + l * dim * hidden_dim, hidden_dim, dim);

            // residual connection
            accum(s->x, s->xb, dim);
        }
    }

    // final normalization
    normalize(s->x, s->x, w->rms_final_weight, dim);
    // classifier into logits
    mat_vec_mul(s->logits, s->x, w->token_embedding_table, p->dim, p->vocab_size);
    // apply the temperature to the logits
    for (int q = 0; q < p->vocab_size; q++) { s->logits[q] /= 0.9f; }
    // apply softmax to the logits to get the probabilities for next token
    softmax(s->logits, p->vocab_size);
    // now sample from this distribution to get the next token
    return sample(s->logits, p->vocab_size);
}

int main(int argc, char* argv[]) {

    unsigned int seed;
    int thr_count;

    if (argc == 3) {
        seed = atoi(argv[1]);
        thr_count = atoi(argv[2]);
    }
    else {
        printf("Usage: ./compiled <seed> <thr_count>\n");
        return 1;
    }

    // Initialize
    srand(seed);
    init_mat_vec_mul(thr_count);

    // load model
    LLMConfig config;
    LLMWeight weights;
    if (load_LLM_Config_Weight(&config, &weights) == 1) { return 1; }

    // load tokenizer
    char** vocab = malloc(config.vocab_size * sizeof(char*));
    if (load_tokenizer(vocab, config.vocab_size) == 1) { return 1; }

    // create and init the application LLMRuntime
    LLMRuntime state;
    malloc_LLMRuntime(&state, &config);

    // the current position we are in
    long start = time_in_ms();

    int next, token = 1, pos = 0; // token = 1 -> <START>
    while (pos < config.seq_len) {

        // forward the transformer to get logits for the next token
        next = transformer(token, pos, &config, &state, &weights);

        printf("%s", vocab[next]);
        fflush(stdout); // force print

        token = next;
        pos++;
    }

    long end = time_in_ms();
    printf("\n\nlength: %d, time: %f s, achieved tok/s: %f\n", config.seq_len, (double)(end - start) / 1000, config.seq_len / (double)(end - start) * 1000);

    // cleanup
    close_mat_vec_mul();
    free_LLMRuntime(&state);
    free_LLMWeight(&weights);
    for (int i = 0; i < config.vocab_size; i++) { free(vocab[i]); }
    free(vocab);
    return 0;
}