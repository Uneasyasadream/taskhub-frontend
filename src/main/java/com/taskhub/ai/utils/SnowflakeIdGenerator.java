package com.taskhub.ai.utils;

import org.springframework.stereotype.Component;

/*
    这是一个基于 Twitter 开源雪花算法的分布式主键生成器。
    它可以保证在极高并发下，每一微秒生成的 ID 都是全网唯一、且整体递增的 Long 型数字
    核心避坑点：
        网页端的 JavaScript 只能精确表达 16 位数字，而 Java 的 Long 是 19 位。
        如果直接把这个 19 位的 Long 返回给前端，前端拿到的最后三位全会变成 000（这就是大名鼎鼎的精度截断 Bug）
        所以我们在后面的接口传输层（VO）上，必须使用字符串（String）类型把这个 ID 传出去
 */

@Component
public class SnowflakeIdGenerator {
    private final long twepoch = 1685539200000L; // 初始时间戳（2023-06-01）
    private final long workerIdBits = 5L; // 工作节点 ID 占用的位数（5 ）
    private final long datacenterIdBits = 5L; // 数据中心 ID 占用的位数（5)
    private final long maxWorkerId = ~(-1L << workerIdBits); // 最大工作节点 ID（31）
    private final long maxDatacenterId = ~(-1L << datacenterIdBits); // 最大数据中心 ID（31）
    private final long sequenceBits = 12L; // 序列号占用的位数（12 ）
    private final long workerIdShift = sequenceBits; // 工作节点 ID 左移位数（12 ）
    private final long datacenterIdShift = sequenceBits + workerIdBits; // 数据中心 ID 左移位数（17 ）
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits; // 时间戳左移位数（22 ）
    private final long sequenceMask = ~(-1L << sequenceBits); // 序列号掩码（4095）

    private long workerId = 1;  // 当前工作节点 ID（默认 1）
    private long datacenterId = 1;  // 当前数据中心 ID（默认 1）
    private long sequence = 0L; // 当前序列号（默认 0）
    private long lastTimestamp = -1L; // 上次生成 ID 的时间戳（默认 -1）

    public synchronized long nextId() {
        long timestamp = timeGen();
        if(timestamp < lastTimestamp) {
            throw new RuntimeException("时钟回拨，拒绝生成 ID");
        }
        if(lastTimestamp == timestamp) {
            sequence = (sequence + 1) & sequenceMask;
            if(sequence == 0) {
                timestamp = tilNextMill(lastTimestamp);
            }
        }else{
            sequence = 0L;
        }
        lastTimestamp = timestamp;
        return ((timestamp - twepoch) << timestampLeftShift) |
                (datacenterId << datacenterIdShift) |
                (workerId << workerIdShift) |
                sequence;
    }

    private long tilNextMill(long lastTimestamp) {
        long timestamp = timeGen();
        while(timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
