import os
import threading
import time

import torch
import torch.nn as nn
import torch.distributed.autograd as dist_autograd
import torch.distributed.rpc as rpc
import torch.multiprocessing as mp
import torch.optim as optim
from torch.distributed.optim import DistributedOptimizer
from torch.distributed.rpc import RRef
import psutil


num_classes = 1000
image_w = 128
image_h = 128


class Stage0(torch.nn.Module):
    def __init__(self):
        super(Stage0, self).__init__()
        self._lock = threading.Lock()
        self.layer2_0 = torch.nn.Conv2d(3, 64, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer3_0 = torch.nn.ReLU(inplace=True)
        self.layer4_0 = torch.nn.Conv2d(64, 64, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        
        self.layer1_1 = torch.nn.ReLU(inplace=True)
        self.layer2_1 = torch.nn.MaxPool2d(kernel_size=2, stride=2, padding=0, dilation=1, ceil_mode=False)
        self.layer3_1 = torch.nn.Conv2d(64, 128, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer4_1 = torch.nn.ReLU(inplace=True)
        
        self._initialize_weights()

    def forward(self, x_rref):
        x = x_rref.to_here().to("cpu")
        with self._lock:
            
            out2_0 = self.layer2_0(x)
            out3_0 = self.layer3_0(out2_0)
            out4_0 = self.layer4_0(out3_0)
            
            out1_1 = self.layer1_1(out4_0)
            out2_1 = self.layer2_1(out1_1)
            out3_1 = self.layer3_1(out2_1)
            out4_1 = self.layer4_1(out3_1)
        # print("End of stage 0 forward", flush=True)
        return out4_1

    def _initialize_weights(self):
        for m in self.modules():
            if isinstance(m, torch.nn.Conv2d):
                torch.nn.init.kaiming_normal_(m.weight, mode='fan_out', nonlinearity='relu')
                if m.bias is not None:
                    torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.BatchNorm2d):
                torch.nn.init.constant_(m.weight, 1)
                torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.Linear):
                torch.nn.init.normal_(m.weight, 0, 0.01)
                torch.nn.init.constant_(m.bias, 0)

    def parameter_rrefs(self):
        r"""
        Create one RRef for each parameter in the given local module, and return a
        list of RRefs.
        """
        return [RRef(p) for p in self.parameters()]


class Stage1(torch.nn.Module):
    def __init__(self):
        super(Stage1, self).__init__()
        self._lock = threading.Lock()


        self.layer5_1 = torch.nn.Conv2d(128, 128, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer6_1 = torch.nn.ReLU(inplace=True)
        self.layer7_1 = torch.nn.MaxPool2d(kernel_size=2, stride=2, padding=0, dilation=1, ceil_mode=False)
        self.layer8_1 = torch.nn.Conv2d(128, 256, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer9_1 = torch.nn.ReLU(inplace=True)
        
        self.layer1_2 = torch.nn.Conv2d(256, 256, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer2_2 = torch.nn.ReLU(inplace=True)
        self._initialize_weights()
    
    def forward(self, x_rref):
        x = x_rref.to_here().to("cpu")
        with self._lock:
            
            out5_1 = self.layer5_1(x)
            out6_1 = self.layer6_1(out5_1)
            out7_1 = self.layer7_1(out6_1)
            out8_1 = self.layer8_1(out7_1)
            out9_1 = self.layer9_1(out8_1)
            
            out1_2 = self.layer1_2(out9_1)
            out2_2 = self.layer2_2(out1_2)
        # print("End of stage 1 forward", flush=True)
        return out2_2

    def _initialize_weights(self):
        for m in self.modules():
            if isinstance(m, torch.nn.Conv2d):
                torch.nn.init.kaiming_normal_(m.weight, mode='fan_out', nonlinearity='relu')
                if m.bias is not None:
                    torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.BatchNorm2d):
                torch.nn.init.constant_(m.weight, 1)
                torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.Linear):
                torch.nn.init.normal_(m.weight, 0, 0.01)
                torch.nn.init.constant_(m.bias, 0)

    def parameter_rrefs(self):
        r"""
        Create one RRef for each parameter in the given local module, and return a
        list of RRefs.
        """
        return [RRef(p) for p in self.parameters()]


class Stage2(torch.nn.Module):
    def __init__(self):
        super(Stage2, self).__init__()
        self._lock = threading.Lock()

        self.layer3_2 = torch.nn.Conv2d(256, 256, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer4_2 = torch.nn.ReLU(inplace=True)
        self.layer5_2 = torch.nn.MaxPool2d(kernel_size=2, stride=2, padding=0, dilation=1, ceil_mode=False)
        self.layer6_2 = torch.nn.Conv2d(256, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer7_2 = torch.nn.ReLU(inplace=True)
        self.layer8_2 = torch.nn.Conv2d(512, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))

        self._initialize_weights()

    def forward(self, x_rref):
        x = x_rref.to_here().to("cpu")
        with self._lock:
            
            out3_2 = self.layer3_2(x)
            out4_2 = self.layer4_2(out3_2)
            out5_2 = self.layer5_2(out4_2)
            out6_2 = self.layer6_2(out5_2)
            out7_2 = self.layer7_2(out6_2)
            out8_2 = self.layer8_2(out7_2)
        # print("End of stage 2 forward", flush=True)
        return out8_2

    def _initialize_weights(self):
        for m in self.modules():
            if isinstance(m, torch.nn.Conv2d):
                torch.nn.init.kaiming_normal_(m.weight, mode='fan_out', nonlinearity='relu')
                if m.bias is not None:
                    torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.BatchNorm2d):
                torch.nn.init.constant_(m.weight, 1)
                torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.Linear):
                torch.nn.init.normal_(m.weight, 0, 0.01)
                torch.nn.init.constant_(m.bias, 0)

    def parameter_rrefs(self):
        r"""
        Create one RRef for each parameter in the given local module, and return a
        list of RRefs.
        """
        return [RRef(p) for p in self.parameters()]


class Stage3(torch.nn.Module):
    def __init__(self):
        super(Stage3, self).__init__()
        self._lock = threading.Lock()
        
        self.layer9_2 = torch.nn.ReLU(inplace=True)
        self.layer10_2 = torch.nn.Conv2d(512, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer11_2 = torch.nn.ReLU(inplace=True)

        self.layer1_3 = torch.nn.MaxPool2d(kernel_size=2, stride=2, padding=0, dilation=1, ceil_mode=False)
        self.layer2_3 = torch.nn.Conv2d(512, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer3_3 = torch.nn.ReLU(inplace=True)
        self.layer4_3 = torch.nn.Conv2d(512, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer5_3 = torch.nn.ReLU(inplace=True)
        self.layer6_3 = torch.nn.Conv2d(512, 512, kernel_size=(3, 3), stride=(1, 1), padding=(1, 1))
        self.layer7_3 = torch.nn.ReLU(inplace=True)
        self.layer8_3 = torch.nn.MaxPool2d(kernel_size=2, stride=2, padding=0, dilation=1, ceil_mode=False)
        self.layer11_3 = torch.nn.Linear(in_features=8192, out_features=4096, bias=True)
        self.layer12_3 = torch.nn.ReLU(inplace=True)
        self.layer13_3 = torch.nn.Dropout(p=0.5)
        self.layer14_3 = torch.nn.Linear(in_features=4096, out_features=4096, bias=True)
        self.layer15_3 = torch.nn.ReLU(inplace=True)
        self.layer16_3 = torch.nn.Dropout(p=0.5)
        self.layer17_3 = torch.nn.Linear(in_features=4096, out_features=1000, bias=True)
        self._initialize_weights()        
    

    def forward(self, x_rref):
        x = x_rref.to_here().to("cpu")
        with self._lock:
            out9_2 = self.layer9_2(x)
            out10_2 = self.layer10_2(out9_2)
            out11_2 = self.layer11_2(out10_2)
            
            out1_3 = self.layer1_3(out11_2)
            out2_3 = self.layer2_3(out1_3)
            out3_3 = self.layer3_3(out2_3)
            out4_3 = self.layer4_3(out3_3)
            out5_3 = self.layer5_3(out4_3)
            out6_3 = self.layer6_3(out5_3)
            out7_3 = self.layer7_3(out6_3)
            out8_3 = self.layer8_3(out7_3)
            out9_3 = out8_3.size(0)
            out10_3 = out8_3.view(out9_3, -1)
            out11_3 = self.layer11_3(out10_3)
            out12_3 = self.layer12_3(out11_3)
            out13_3 = self.layer13_3(out12_3)
            out14_3 = self.layer14_3(out13_3)
            out15_3 = self.layer15_3(out14_3)
            out16_3 = self.layer16_3(out15_3)
            out17_3 = self.layer17_3(out16_3)
        # print("End of stage 3 forward", flush=True)
        return out17_3

    def _initialize_weights(self):
        for m in self.modules():
            if isinstance(m, torch.nn.Conv2d):
                torch.nn.init.kaiming_normal_(m.weight, mode='fan_out', nonlinearity='relu')
                if m.bias is not None:
                    torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.BatchNorm2d):
                torch.nn.init.constant_(m.weight, 1)
                torch.nn.init.constant_(m.bias, 0)
            elif isinstance(m, torch.nn.Linear):
                torch.nn.init.normal_(m.weight, 0, 0.01)
                torch.nn.init.constant_(m.bias, 0)

    def parameter_rrefs(self):
        r"""
        Create one RRef for each parameter in the given local module, and return a
        list of RRefs.
        """
        return [RRef(p) for p in self.parameters()]


class DistVggNet(nn.Module):
    """
    Assemble two parts as an nn.Module and define pipelining logic
    """
    def __init__(self, num_split, workers, *args, **kwargs):
        super(DistVggNet, self).__init__()
        
        self.num_split = num_split

        # Put the first stage on workers[0]
        self.p1_rref = rpc.remote(
            workers[0],
            Stage0,
            args = args,
            kwargs = kwargs,
            timeout=1000
        )
        # Put the second stage on workers[1]
        self.p2_rref = rpc.remote(
            workers[1],
            Stage1,
            args = args,
            kwargs = kwargs,
            timeout=1000
        )
        # Put the third stage on workers[2]
        self.p3_rref = rpc.remote(
            workers[2],
            Stage2,
            args = args,
            kwargs = kwargs,
            timeout=1000
        )
        # Put the fourth stage on workers[3]
        self.p4_rref = rpc.remote(
            workers[3],
            Stage3,
            args = args,
            kwargs = kwargs,
            timeout=1000
        )

    def forward(self, x):
        out_futures = []
        for x in iter(x.split(self.num_split, dim=0)):
            s1_rref = RRef(x)
            s2_rref = self.p1_rref.remote().forward(s1_rref)
            s3_rref = self.p2_rref.remote().forward(s2_rref)
            s4_rref = self.p3_rref.remote().forward(s3_rref)
            s5_rref = self.p4_rref.rpc_async().forward(s4_rref)
            out_futures.append(s5_rref)

        # collect and cat all output tensors into one tensor.
        return torch.cat(torch.futures.wait_all(out_futures))

    def parameter_rrefs(self):
        remote_params = []
        remote_params.extend(self.p1_rref.remote().parameter_rrefs().to_here())
        remote_params.extend(self.p2_rref.remote().parameter_rrefs().to_here())
        remote_params.extend(self.p3_rref.remote().parameter_rrefs().to_here())
        remote_params.extend(self.p4_rref.remote().parameter_rrefs().to_here())
        return remote_params


#########################################################
#                   Run RPC Processes                   #
#########################################################

num_batches = 4
batch_size = 64  # do not modify me

def run_master(num_split):

    # put the two model parts on worker1 and worker2 respectively
    model = DistVggNet(num_split,["worker1", "worker2", "worker3", "worker4"])
    loss_fn = nn.MSELoss()
    opt = DistributedOptimizer(
        optim.SGD,
        model.parameter_rrefs(),
        lr=0.05,
    )

    one_hot_indices = torch.LongTensor(batch_size) \
                           .random_(0, num_classes) \
                           .view(batch_size, 1)

    for i in range(num_batches):
        print(f"Processing batch {i}", flush=True)
        tik1 = time.time()
        # generate random inputs and labels
        inputs = torch.randn(batch_size, 3, image_w, image_h)
        labels = torch.zeros(batch_size, num_classes) \
                      .scatter_(1, one_hot_indices, 1)

        # The distributed autograd context is the dedicated scope for the
        # distributed backward pass to store gradients, which can later be
        # retrieved using the context_id by the distributed optimizer.
        with dist_autograd.context() as context_id:
            outputs = model(inputs)
            print("Start of backpropagation and optimization", flush=True)
            dist_autograd.backward(context_id, [loss_fn(outputs, labels)])
            opt.step(context_id)
            print("End of backpropagation and optimization", flush=True)
        tok1 = time.time()
        print(f"batch = {i}, execution time = {tok1 - tik1}")


def run_worker(rank, world_size, num_split):
    os.environ['MASTER_ADDR'] = 'localhost'
    os.environ['MASTER_PORT'] = '29500'
    options = rpc.TensorPipeRpcBackendOptions(num_worker_threads=256, rpc_timeout=10000)

    p = psutil.Process()
    
    if rank == 0:
        p.cpu_affinity([0])
        print(f"Child #{rank}: affinity now {p.cpu_affinity()}", flush=True)

        rpc.init_rpc(
            "master",
            rank=rank,
            world_size=world_size,
            rpc_backend_options=options
        )
        run_master(num_split)
    else:
        p.cpu_affinity([rank-1])
        print(f"Child #{rank}: affinity now {p.cpu_affinity()}", flush=True)

        rpc.init_rpc(
            f"worker{rank}",
            rank=rank,
            world_size=world_size,
            rpc_backend_options=options
        )
        pass

    # block until all rpcs finish
    rpc.shutdown()


if __name__=="__main__":
    world_size = 5
    # tik = time.time()
    # mp.spawn(run_worker, args=(world_size,), nprocs=world_size, join=True)
    # tok = time.time()
    # print(f"execution time = {tok - tik}")
    for num_split in [4]:
        tik = time.time()
        mp.spawn(run_worker, args=(world_size, num_split), nprocs=world_size, join=True)
        tok = time.time()
        print(f"number of splits = {num_split}, execution time = {tok - tik}")
