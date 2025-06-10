### netty使用说明

* 设置空闲检查代码

  ```java

     List<Integer> heartBeatTimeList = config.getHeartBeatTimeList();
     if (CollUtil.isNotEmpty(heartBeatTimeList) && heartBeatTimeList.size() == HEART_BEAT_TIME_ITEM_COUNT) {
        channelPipeline.addLast(new IdleStateHandler(heartBeatTimeList.get(0), heartBeatTimeList.get(1), heartBeatTimeList.get(2)));
     }
  ```
