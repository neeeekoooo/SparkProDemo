package com.cwq.spark.sparkstream.util

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.apache.spark.{SparkConf, SparkContext}

object PageLogProducer {
  def main(args: Array[String]): Unit = {

    val sparkConf = new SparkConf().setAppName("pageProducer").setMaster("local[*]")
    val ssc = new SparkContext(sparkConf)
    ssc.textFile("D:\\My_pro\\IDEA_Project\\SparkProDemo\\RealTimeModule\\src\\main\\resources\\page.txt")
      .foreachPartition(partitoin => {
        val props = new Properties()
        props.put("bootstrap.servers", "hadoop200:9092,hadoop201:9092,hadoop202:9092")
        props.put("acks", "1")
        props.put("batch.size", "16384")
        props.put("linger.ms", "10")
        props.put("buffer.memory", "33554432")
        props.put("key.serializer","org.apache.kafka.common.serialization.StringSerializer")
        props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer")
        val producer = new KafkaProducer[String, String](props)
        partitoin.foreach(item => {
          val msg = new ProducerRecord[String, String]("page_topic", item)
          producer.send(msg)
        })
        producer.flush()
        producer.close()
      })

  }
}