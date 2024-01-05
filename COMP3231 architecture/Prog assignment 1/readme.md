# How to run
#### compile 
    nvcc part1.cu -o part1
#### run 
    ./part1 image_grey.txt
    ./part2 image_color.txt

# Python Util code

### Show a txt into png
    python3 txt2imageXXX.py xxxx.txt
### Generate a RGB test set
    python3 image2txt.py xxxx.jpg/png

### Export to profile (before using nvcc) 
    export PATH="$PATH:/usr/local/cuda/bin"

# Best Run-time

### part one

CPU Implementation
Elapsed time: 334.718ms
Saving data to grey_result_CPU.txt... 
5046272 entries saved

GPU Implementation
Kernel Elapsed time: 1.23797ms
Elapsed time: 210.839ms
Saving data to grey_result_GPU.txt... 
5046272 entries saved

### part two

CPU Implementation
Elapsed time: 4127.61ms
Saving data to color_result_CPU.txt... 
5184000 entries saved

GPU Implementation
Kernel Elapsed time: 15.089ms
Elapsed time: 552.648ms
Saving data to color_result_GPU.txt... 
5184000 entries saved